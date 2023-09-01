package com.example.project_locate_bus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.project_locate_bus.databinding.ActivityAdminHomeBinding;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;

public class Admin_home extends AppCompatActivity {
    ActivityAdminHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminHomeBinding.inflate(getLayoutInflater());
        // setContentView(R.layout.activity_admin_home);
        setContentView(binding.getRoot());

        MaterialToolbar toolbar = findViewById(R.id.topAppBar);
        DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);
        NavigationView navigationView = findViewById(R.id.navigationView);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @org.jetbrains.annotations.NotNull MenuItem item) {
                int id = item.getItemId();
                item.setChecked(true);
                drawerLayout.closeDrawer(GravityCompat.START);

                if (id == R.id.nav_stu_reg) {
                    Intent i = new Intent(Admin_home.this, Registration.class);
                    startActivity(i);
                } else if (id == R.id.nav_staff_reg) {
                    Intent intent = new Intent(Admin_home.this, Staff_Reg.class);
                    startActivity(intent);
                } else if (id == R.id.nav_driver_reg) {
                    Intent intent1 = new Intent(Admin_home.this, Driver_Reg.class);
                    startActivity(intent1);
                } else if (id == R.id.nav_fee) {
                    Intent intent2 = new Intent(Admin_home.this, Fee_payment.class);
                    startActivity(intent2);
                } else if (id == R.id.nav_bus_route1) {
                    Intent intent3 = new Intent(Admin_home.this, MapsActivity.class);
                    startActivity(intent3);
                } else if (id == R.id.nav_log_out) {
                    Intent intent4 = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent4);
                }
                return true;
            }
        });
        binding.bottomNavView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home1:
                    replaceFragment(new HomeFragment());
                    break;
                case R.id.student:
                    replaceFragment(new Student());
                    break;
                case R.id.staff:
                    replaceFragment(new Staff());
                    break;
                case R.id.driver:
                    replaceFragment(new Driver());
                    break;
            }
            return true;
        });
    }
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout,fragment);
        //fragmentTransaction.addToBackStack("null");
        fragmentTransaction.commit();
    }
    @Override
    protected void onResume() {
        super.onResume();
        binding.bottomNavView.setSelectedItemId(R.id.home1);
    }
}