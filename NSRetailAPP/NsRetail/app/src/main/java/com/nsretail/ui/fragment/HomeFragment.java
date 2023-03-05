package com.nsretail.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.nsretail.Globals;
import com.nsretail.databinding.FragmentHomeBinding;
import com.nsretail.ui.activities.StockEntryActivity;


public class HomeFragment extends Fragment {

    FragmentHomeBinding binding;
    String token;

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);


        if (Globals.userResponse.getFeatureAccess().size() > 0){
            binding.buttonEntry.setEnabled(Globals.userResponse.getFeatureAccess().get(0).isAccessAvailable());
            binding.buttonDispatch.setEnabled(Globals.userResponse.getFeatureAccess().get(1).isAccessAvailable());
            binding.buttonCounting.setEnabled(Globals.userResponse.getFeatureAccess().get(2).isAccessAvailable());
            binding.buttonDetails.setEnabled(Globals.userResponse.getFeatureAccess().get(3).isAccessAvailable());
        }

        binding.buttonEntry.setOnClickListener(view -> {
            startActivity(new Intent(getActivity(), StockEntryActivity.class));
        });

        return binding.getRoot();
    }
}