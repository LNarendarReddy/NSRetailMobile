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
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.nsretail.Globals;
import com.nsretail.R;
import com.nsretail.databinding.ActivityMainBinding;
import com.nsretail.ui.fragment.HomeFragment;

import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setUpNavigationView();

        if (Globals.userResponse.getUser().size() > 0) {
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

    @Override
    public void onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            int count = getSupportFragmentManager().getBackStackEntryCount();
            List<Fragment> frags = getSupportFragmentManager().getFragments();
            Fragment lastFrag = getLastNotNull(frags);
            if (count == 0) {
                MainActivity.super.onBackPressed();
            } else {
                if (lastFrag != null) {
                    if (lastFrag.getTag() != null) {
                        if (count == 1) {
                            if (lastFrag.getTag().equalsIgnoreCase("Home")) {
                                showFinish();
                            }
                        }
                    }
                }

            }
        }
    }

    private Fragment getLastNotNull(List<Fragment> list) {
        if (list.size() > 1) {
            for (int i = list.size() - 1; i >= 0; i--) {
                if (list.get(i) != null) {
                    try {
                        if (!Objects.requireNonNull(list.get(i).getTag()).equalsIgnoreCase("com.bumptech.glide.manager")) {
                            Fragment frag = list.get(i);
                            if (frag != null) {
                                return frag;
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            return list.get(0);
        }
        return null;
    }

    public void showFinish() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", (dialog, id) -> MainActivity.this.finish())
                .setNegativeButton("No", (dialog, id) -> dialog.cancel());
        android.app.AlertDialog alert = builder.create();
        if (!alert.isShowing())
            alert.show();
    }

}