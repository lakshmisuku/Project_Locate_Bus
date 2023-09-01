package com.example.project_locate_bus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Recycler_view extends AppCompatActivity {
    RecyclerView recyclerview;
    DatabaseReference database;
    MyAdapter myadapter;
    ArrayList<User> list;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);

        recyclerview = findViewById(R.id.userlist);

        database = FirebaseDatabase.getInstance().getReferenceFromUrl("https://locate-my-bus-5e265-default-rtdb.firebaseio.com/");

        sharedPreferences = getSharedPreferences("LocateMyBus",MODE_PRIVATE);

        recyclerview.setHasFixedSize(true);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();

        myadapter = new MyAdapter(this,list);

        recyclerview.setAdapter(myadapter);

        database.child("Fee").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    User user = dataSnapshot.getValue(User.class);
                    if(user.getMobile().equals(sharedPreferences.getString("Mobile",""))) {
                        list.add(user);
                    }
                }
                myadapter.notifyDataSetChanged();
                Log.e("TAG", "onDataChange: "+snapshot );
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Recycler_view.this, error.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}