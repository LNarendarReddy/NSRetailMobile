package com.nsretail.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.nsretail.R;
import com.nsretail.data.api.BaseURL;
import com.nsretail.data.api.StatusAPI;
import com.nsretail.data.model.CountingModel.CountingDetail;
import com.nsretail.databinding.ItemStockEntryBinding;
import com.nsretail.ui.Interface.OnItemClickListener;
import com.nsretail.ui.activities.StockCounting.AddCountingItemActivity;
import com.nsretail.ui.activities.StockCounting.StockCountingActivity;
import com.nsretail.utils.NetworkStatus;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StockCountingAdapter extends RecyclerView.Adapter<StockCountingAdapter.ViewHolder> {

    private ArrayList<CountingDetail> countingDetails;
    private final Context mContext;
    OnItemClickListener listener;

    public StockCountingAdapter(Context context, ArrayList<CountingDetail> countingDetailArrayList, OnItemClickListener listener) {
        this.mContext = context;
        this.countingDetails = countingDetailArrayList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemStockEntryBinding itemBinding = ItemStockEntryBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(itemBinding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.itemStockBinding.textSNO.setText("" + (position + 1) + ") ");

        holder.itemStockBinding.textStock.setText(countingDetails.get(position).itemCode
                + " " + countingDetails.get(position).itemName);

        if (countingDetails.get(position).isOpenItem) {
            holder.itemStockBinding.textPrice.setText("MRP: " + countingDetails.get(position).mrp
                    + ", W/Kgs: " + countingDetails.get(position).weightInKgs);
        } else {
            holder.itemStockBinding.textPrice.setText("MRP: " + countingDetails.get(position).mrp
                    + ", Qty: " + countingDetails.get(position).quantity);
        }

        holder.itemStockBinding.imageDelete.setOnClickListener(view -> {
            if (NetworkStatus.getInstance(mContext).isConnected()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.AlertDialogCustom);
                builder.setMessage("Are you sure you want to delete?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", (dialog, id) -> {
                            deleteItem(position, view);
                            dialog.cancel();
                        }).setNegativeButton("No", (dialogInterface, i) -> dialogInterface.cancel());
                AlertDialog alert = builder.create();
                alert.show();
            } else
                Toast.makeText(mContext, "No internet connection", Toast.LENGTH_SHORT).show();
        });

        holder.itemView.setOnLongClickListener(view -> {

            ((StockCountingActivity) mContext).updateItemData(position);

            return false;
        });

    }

    private void deleteItem(int position, View view) {
        StatusAPI deleteAPI = BaseURL.getStatusAPI();
        Call<ResponseBody> call = deleteAPI.deleteCounting(countingDetails.get(position).stockCountingDetailId);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.AlertDialogCustom);
                    try {
                        builder.setMessage(response.body().string())
                                .setCancelable(false)
                                .setPositiveButton("OK", (dialog, id) -> {
                                    listener.onItemClick(position, view);
                                    dialog.cancel();
                                });
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    AlertDialog alert = builder.create();
                    alert.show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    try {
                        builder.setMessage(response.errorBody().string())
                                .setCancelable(false)
                                .setPositiveButton("OK", (dialog, id) -> {
                                    dialog.cancel();
                                });
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemStockEntryBinding itemStockBinding;

        public ViewHolder(ItemStockEntryBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemStockBinding = itemBinding;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return countingDetails.size();
    }

    public void updateList(ArrayList<CountingDetail> filterCountingDetails) {
        countingDetails = filterCountingDetails;
        notifyDataSetChanged();
    }

}