package com.nsretail.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.nsretail.Globals;
import com.nsretail.R;
import com.nsretail.data.api.BaseURL;
import com.nsretail.data.api.StatusAPI;
import com.nsretail.data.model.StockEntry.StockEntryDetail;
import com.nsretail.databinding.ItemStockEntryBinding;
import com.nsretail.ui.Interface.OnItemClickListener;
import com.nsretail.ui.activities.StockEntry.StockEntryActivity;
import com.nsretail.utils.NetworkStatus;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StockEntryAdapter extends RecyclerView.Adapter<StockEntryAdapter.ViewHolder> {

    private final ArrayList<StockEntryDetail> stockEntryList;
    private final Context mContext;
    OnItemClickListener listener;

    public StockEntryAdapter(Context context, ArrayList<StockEntryDetail> stockList, OnItemClickListener listener) {
        this.mContext = context;
        this.stockEntryList = stockList;
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

        holder.itemStockBinding.textStock.setText(stockEntryList.get(position).itemCode
                + " " + stockEntryList.get(position).itemName);

        if (stockEntryList.get(position).isOpenItem) {
            holder.itemStockBinding.textPrice.setText("MRP: " + stockEntryList.get(position).mrp
                    + ", CP: " + stockEntryList.get(position).costPriceWt
                    + ", W/Kgs: " + stockEntryList.get(position).weightInKgs);
        } else {
            holder.itemStockBinding.textPrice.setText("MRP: " + stockEntryList.get(position).mrp
                    + ", CP: " + stockEntryList.get(position).costPriceWt
                    + ", Qty: " + stockEntryList.get(position).quantity);
        }

        holder.itemStockBinding.imageDelete.setOnClickListener(view -> {
            if (NetworkStatus.getInstance(mContext).isConnected())
                deleteItem(position, view);
            else
                Toast.makeText(mContext, "No internet connection", Toast.LENGTH_SHORT).show();
        });

        holder.itemView.setOnLongClickListener(view -> {

            ((StockEntryActivity) mContext).updateItemData(position);

            return false;
        });

    }

    private void deleteItem(int position, View view) {
        StatusAPI deleteAPI = BaseURL.getStatusAPI();
        Call<ResponseBody> call = deleteAPI.deleteStockEntry(stockEntryList.get(position).stockEntryDetailId,
                Globals.userResponse.user.get(0).userId, true);
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
        return stockEntryList.size();
    }
}