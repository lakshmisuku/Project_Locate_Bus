package com.example.project_locate_bus;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    TextView forget;
    String uname,upwd;
    EditText usrnm,pswrd;
    Button btn;
    DatabaseReference databaseReference;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://locate-my-bus-5e265-default-rtdb.firebaseio.com/");

        sharedPreferences = getSharedPreferences("LocateMyBus",MODE_PRIVATE);

        editor = sharedPreferences.edit();

        addListenerOnButtonClick();
    }
    public void addListenerOnButtonClick() {
        forget = (TextView) findViewById(R.id.textView2);
        usrnm = (EditText) findViewById(R.id.editText1);
        pswrd = (EditText) findViewById(R.id.editText2);
        btn = (Button) findViewById(R.id.button1);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uname = usrnm.getText().toString();
                upwd = pswrd.getText().toString();

                if (uname.isEmpty() && upwd.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please enter all details", Toast.LENGTH_SHORT).show();
                }
                else {
                    if (uname.equals("Admin")) {

                        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.hasChild(uname)) {
                                    final String getPass = snapshot.child(uname).child("Password").getValue(String.class);

                                    if (getPass.equals(upwd)) {
                                        Intent i = new Intent(getApplicationContext(), Admin_home.class);
                                        i.putExtra("Username", uname);
                                        startActivity(i);
                                        Toast.makeText(MainActivity.this, "Admin Login Successful", Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        Toast.makeText(MainActivity.this, "Wrong Password", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else {
                                    Log.d(TAG, "onDataChange: "+snapshot.toString());
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(MainActivity.this, error.getMessage().toString(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    else {
                        databaseReference.child("Student").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                if(snapshot.hasChild(uname))
                                {
                                    final String getPass = snapshot.child(uname).child("Password").getValue(String.class);
                                    final String getName = snapshot.child(uname).child("Name").getValue(String.class);
                                    final String getBusName = snapshot.child(uname).child("BusName").getValue(String.class);
                                    Log.e(TAG, "onDataChange: "+getName );

                                    if (getPass.equals(upwd))
                                    {
                                        editor.putString("Mobile",uname);
                                        editor.putString("BusName",getBusName);
                                        editor.commit();

                                        Intent i = new Intent(getApplicationContext(),Student_home.class);
                                        i.putExtra("username",uname);
                                        startActivity(i);
                                        Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        Toast.makeText(MainActivity.this, "Wrong Password", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else {
                                    databaseReference.child("Faculty").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                                            if(snapshot.hasChild(uname))
                                            {
                                                final String getPass = snapshot.child(uname).child("Password").getValue(String.class);

                                                if (getPass.equals(upwd))
                                                {
                                                    Intent i = new Intent(getApplicationContext(),Faculty_home.class);
                                                    i.putExtra("username",uname);
                                                    startActivity(i);
                                                    Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                                }
                                                else {
                                                    Toast.makeText(MainActivity.this, "Wrong Password", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        }
                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                            Toast.makeText(MainActivity.this, error.getMessage().toString(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(MainActivity.this, error.getMessage().toString(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }
        });
        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getApplicationContext(),Forget_Password.class);
                startActivity(in);
            }
        });
    }
}