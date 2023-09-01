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

import com.example.project_locate_bus.databinding.ActivityStudentHomeBinding;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;

public class Student_home extends AppCompatActivity {

    ActivityStudentHomeBinding stud_binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        stud_binding = ActivityStudentHomeBinding.inflate(getLayoutInflater());
        //setContentView(R.layout.activity_student_home);
        setContentView(stud_binding.getRoot());

        MaterialToolbar toolbar = findViewById(R.id.topAppBar2);
        DrawerLayout drawerLayout = findViewById(R.id.drawerLayout2);
        NavigationView navigationView = findViewById(R.id.navigationView2);

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

                if (id == R.id.nav_stu_list) {
                    Intent i = new Intent(Student_home.this, Recycler_view.class);
                    startActivity(i);
                } else if (id == R.id.nav_stu_log) {
                    Intent it = new Intent(Student_home.this, MainActivity.class);
                    startActivity(it);
                }
                return true;
            }
        });
        stud_binding.bottomNavView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home2:
                    replaceFragment(new StudentHome());
                    break;
                case R.id.bus_route2:
                    Intent itt = new Intent(Student_home.this, MapsActivity.class);
                    startActivity(itt);
                    break;
            }
            return true;
        });



    }
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout2, fragment);
        //fragmentTransaction.addToBackStack("null");
        fragmentTransaction.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        stud_binding.bottomNavView.setSelectedItemId(R.id.home2);
    }
}