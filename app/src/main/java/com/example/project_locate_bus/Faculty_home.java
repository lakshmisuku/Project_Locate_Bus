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
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.example.project_locate_bus.databinding.ActivityFacultyHomeBinding;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;

import org.jetbrains.annotations.NotNull;

public class Faculty_home extends AppCompatActivity {
    ActivityFacultyHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFacultyHomeBinding.inflate(getLayoutInflater());
        //setContentView(R.layout.activity_faculty_home);
        setContentView(binding.getRoot());

        MaterialToolbar toolbar = findViewById(R.id.topAppBar3);
        DrawerLayout drawerLayout = findViewById(R.id.drawerLayout3);
        NavigationView navigationView = findViewById(R.id.navigationView3);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                int id = item.getItemId();
                item.setChecked(true);
                drawerLayout.closeDrawer(GravityCompat.START);

                if (id == R.id.nav_stu_detail) {
                    Intent i = new Intent(getApplicationContext(), Recycler_view.class);
                    startActivity(i);
                } else if (id == R.id.fac_log_out) {
                    Intent ib = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(ib);
                }
                return true;
            }
        });
        binding.bottomNavView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home3:
                    replaceFragment(new FacultyHome());
                    break;
                case R.id.bus_route3:
                    Intent itt = new Intent(Faculty_home.this, MapsActivity.class);
                    startActivity(itt);
                    break;
            }
            return true;
        });

    }
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout3,fragment);
        //fragmentTransaction.addToBackStack("null");
        fragmentTransaction.commit();
    }
    @Override
    protected void onResume() {
        super.onResume();
        binding.bottomNavView.setSelectedItemId(R.id.home3);
    }
}