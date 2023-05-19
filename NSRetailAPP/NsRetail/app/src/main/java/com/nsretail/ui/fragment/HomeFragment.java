package com.nsretail.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.nsretail.Globals;
import com.nsretail.data.api.BaseURL;
import com.nsretail.data.api.StatusAPI;
import com.nsretail.data.model.CountingModel.Counting;
import com.nsretail.data.model.DispatchModel.Dispatch;
import com.nsretail.databinding.FragmentHomeBinding;
import com.nsretail.ui.activities.ItemDetailActivity;
import com.nsretail.ui.activities.StockCounting.CountingBranchActivity;
import com.nsretail.ui.activities.StockCounting.StockCountingActivity;
import com.nsretail.ui.activities.StockDispatch.BranchActivity;
import com.nsretail.ui.activities.StockDispatch.StockDispatchActivity;
import com.nsretail.ui.activities.StockEntry.SupplierActivity;
import com.nsretail.utils.NetworkStatus;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeFragment extends Fragment {

    FragmentHomeBinding binding;

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);


        if (Globals.userResponse.getFeatureAccess().size() > 0) {
            binding.buttonEntry.setEnabled(Globals.userResponse.getFeatureAccess().get(0).isAccessAvailable());
            binding.buttonDispatch.setEnabled(Globals.userResponse.getFeatureAccess().get(1).isAccessAvailable());
            binding.buttonCounting.setEnabled(Globals.userResponse.getFeatureAccess().get(2).isAccessAvailable());
            binding.buttonDetails.setEnabled(Globals.userResponse.getFeatureAccess().get(3).isAccessAvailable());
        }

        binding.buttonEntry.setOnClickListener(view -> {
            startActivity(new Intent(getActivity(), SupplierActivity.class));
        });

        binding.buttonDispatch.setOnClickListener(view -> {
            if (NetworkStatus.getInstance(getActivity()).isConnected())
                getDispatch();
            else
                Toast.makeText(getActivity(), "No internet Connection", Toast.LENGTH_SHORT).show();
        });

        binding.buttonCounting.setOnClickListener(view -> {
            if (NetworkStatus.getInstance(getActivity()).isConnected())
                getCounting();
            else
                Toast.makeText(getActivity(), "No internet Connection", Toast.LENGTH_SHORT).show();
        });

        binding.buttonDetails.setOnClickListener(view -> {
            startActivity(new Intent(getActivity(), ItemDetailActivity.class));
        });

        return binding.getRoot();
    }

    private void getDispatch() {
        binding.progressBar.setVisibility(View.VISIBLE);
        StatusAPI planAPI = BaseURL.getStatusAPI();
        Call<Dispatch> call = planAPI.getDispatch(Globals.userResponse.user.get(0).categoryId, Globals.userResponse.user.get(0).userId
                , true);

        call.enqueue(new Callback<Dispatch>() {
            @Override
            public void onResponse(Call<Dispatch> call, Response<Dispatch> response) {
                binding.progressBar.setVisibility(View.GONE);
                if (response.code() == 200) {
                    if (response.message().equalsIgnoreCase("DISPATCH DOES NOT EXISTS")) {
                        startActivity(new Intent(getActivity(), BranchActivity.class));
                    } else {
                        startActivity(new Intent(getActivity(), StockDispatchActivity.class));
                    }
                }
                if (response.code() == 404) {
                    Log.v("rsponse >>>", response.message());
                    try {
                        if (response.errorBody().string().equalsIgnoreCase("DISPATCH DOES NOT EXISTS")) {
                            startActivity(new Intent(getActivity(), BranchActivity.class));
                        } else {
                            startActivity(new Intent(getActivity(), StockDispatchActivity.class));
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(Call<Dispatch> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);

            }
        });
    }

    private void getCounting() {
        binding.progressBar.setVisibility(View.VISIBLE);
        StatusAPI planAPI = BaseURL.getStatusAPI();
        Call<Counting> call = planAPI.getCounting(Globals.userResponse.user.get(0).userId);

        call.enqueue(new Callback<Counting>() {
            @Override
            public void onResponse(Call<Counting> call, Response<Counting> response) {
                binding.progressBar.setVisibility(View.GONE);
                if (response.code() == 200) {
                    if (response.message().equalsIgnoreCase("No counting data found")) {
                        startActivity(new Intent(getActivity(), CountingBranchActivity.class));
                    } else {
                        startActivity(new Intent(getActivity(), StockCountingActivity.class));
                    }
                }
                if (response.code() == 404) {
                    Log.v("rsponse cunty >>>", response.message());
                    try {
                        if (response.errorBody().string().equalsIgnoreCase("No counting data found")) {
                            startActivity(new Intent(getActivity(), CountingBranchActivity.class));
                        } else {
                            startActivity(new Intent(getActivity(), StockCountingActivity.class));
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(Call<Counting> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);

            }
        });
    }
}