package com.nsretail.ui.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.nsretail.Globals;
import com.nsretail.R;
import com.nsretail.data.api.BaseURL;
import com.nsretail.data.api.StatusAPI;
import com.nsretail.data.model.SupplierModel.Supplier;
import com.nsretail.databinding.ActivityMainBinding;
import com.nsretail.ui.fragment.HomeFragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setUpNavigationView();

        if (Globals.userResponse.getUser().size() > 0){
            binding.textUserName.setText(Globals.userResponse.getUser().get(0).userName);
            binding.textFullName.setText(Globals.userResponse.getUser().get(0).fullName);
            binding.textBranchName.setText(Globals.userResponse.getUser().get(0).branchName);
            binding.textCategoryName.setText(Globals.userResponse.getUser().get(0).categoryName);
        }

        binding.cardLogout.setOnClickListener(view -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Are you sure you want to Logout?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", (dialog, id) -> {
                        SharedPreferences preferences = getSharedPreferences("login", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor1 = preferences.edit();
                        editor1.clear();
                        editor1.apply();
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        finish();
                    })
                    .setNegativeButton("No", (dialog, id) -> dialog.cancel());
            AlertDialog alert = builder.create();
            alert.show();

        });

        homeFragment();

    }

    @SuppressLint("SetTextI18n")
    private void homeFragment() {
        binding.includeMain.toolbarTitle.setText("HOME");
        Fragment fragment = new HomeFragment();
        replaceFragment(fragment, "Home");
    }

    private void replaceFragment(Fragment fragment, String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.frame, fragment, tag)
                .addToBackStack(tag)
                .commit();
    }

    private void setUpNavigationView() {
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, binding.drawerLayout
                , binding.includeMain.toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }
        };

        binding.drawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
//        toolbar.setNavigationIcon(R.drawable.naviicon);
    }

}