package com.nsretail.ui.activities.StockDispatch;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;

import com.nsretail.Globals;
import com.nsretail.R;
import com.nsretail.data.api.BaseURL;
import com.nsretail.data.api.StatusAPI;
import com.nsretail.data.model.BranchModel.Branch;
import com.nsretail.databinding.ActivityBranchBinding;
import com.nsretail.ui.Interface.OnItemClickListener;
import com.nsretail.ui.adapter.BranchAdapter;
import com.nsretail.utils.NetworkStatus;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BranchActivity extends AppCompatActivity implements OnItemClickListener {

    ActivityBranchBinding binding;
    List<Branch> branchList;
    BranchAdapter adapter;
    ArrayList<Branch> filteredData;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBranchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.includeBranch.textTitle.setText("Branches");

        binding.includeBranch.searchView.setVisibility(View.VISIBLE);

        DividerItemDecoration horizontalDecoration = new DividerItemDecoration(binding.recyclerViewBranch.getContext(),
                DividerItemDecoration.VERTICAL);
        Drawable divider = ContextCompat.getDrawable(this, R.drawable.divider);
        if (divider != null) {
            horizontalDecoration.setDrawable(divider);
        }
        binding.recyclerViewBranch.addItemDecoration(horizontalDecoration);

        if (NetworkStatus.getInstance(BranchActivity.this).isConnected())
            getBranch();
        else
            Toast.makeText(BranchActivity.this, "No internet Connection", Toast.LENGTH_SHORT).show();

        binding.includeBranch.imageBack.setVisibility(View.VISIBLE);
        binding.includeBranch.imageBack.setOnClickListener(v -> finish());

        binding.includeBranch.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                searchData(newText);
//                adapter.getFilter().filter(newText);
                return false;
            }
        });

    }

    private void getBranch() {
        binding.progressBar.setVisibility(View.VISIBLE);
        branchList = new ArrayList<>();
        StatusAPI branchApi = BaseURL.getStatusAPI();
        Call<List<Branch>> call = branchApi.getBranch("stockdispatch/getbranch", Globals.userResponse.user.get(0).userId);
        call.enqueue(new Callback<List<Branch>>() {
            @Override
            public void onResponse(Call<List<Branch>> call, Response<List<Branch>> response) {
                binding.progressBar.setVisibility(View.GONE);

                if (response.code() == 200) {
                    branchList = response.body();
                    adapter = new BranchAdapter(getApplicationContext(), branchList, BranchActivity.this);
                    binding.recyclerViewBranch.setAdapter(adapter);

                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(BranchActivity.this);
                    try {
                        builder.setMessage(response.errorBody().string())
                                .setCancelable(false)
                                .setPositiveButton("OK", (dialog, id) ->
                                        dialog.cancel());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }

            @Override
            public void onFailure(Call<List<Branch>> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                AlertDialog.Builder builder = new AlertDialog.Builder(BranchActivity.this);
                if (t.getMessage().equalsIgnoreCase("Failed to connect to nsoftsol.com/122.175.62.71:6002")) {
                    builder.setMessage("Network Issue!!").setCancelable(false).setPositiveButton("OK", (dialog, id) -> {
                        finish();
                        dialog.cancel();
                    });
                } else {
                    builder.setMessage(t.getMessage()).setCancelable(false).setPositiveButton("OK", (dialog, id) -> dialog.cancel());
                }
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }

    @Override
    public void onItemClick(int position, View view) {
        Intent h = new Intent(BranchActivity.this, CategoryActivity.class);
        if (filteredData != null) {
            if (filteredData.size() > 0)
                h.putExtra("ToBranchId", filteredData.get(position).branchId);
            else
                h.putExtra("ToBranchId", branchList.get(position).branchId);
        } else {
            h.putExtra("ToBranchId", branchList.get(position).branchId);
        }
        startActivity(h);
        finish();
    }

    public void searchData(String searchText) {
        filteredData = new ArrayList<>();

        for (Branch detail : branchList) {
            if (detail.branchName.toLowerCase().contains(searchText.toLowerCase())) {
                filteredData.add(detail);
            }
        }

        adapter.updateList(filteredData);

    }
}