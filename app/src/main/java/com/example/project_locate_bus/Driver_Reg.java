package com.example.project_locate_bus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Driver_Reg extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    TextView Name,Cont,Add,BName,Pasw;
    EditText ename,econt,eadd,epsw;
    Spinner spinn1;
    Button butn;
    String bname;
    String bName[] = {"Select Bus","Kottarakkara","Haripad","Ranni","Thiruvalla","Mannar","Pathanapuram"};
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_reg);

        addListenerOnButtonClick();
    }
    public void addListenerOnButtonClick() {
        Name = (TextView) findViewById(R.id.textView2);
        Cont = (TextView) findViewById(R.id.textView3);
        Add = (TextView) findViewById(R.id.textView4);
        BName = (TextView) findViewById(R.id.textView5);
        Pasw = (TextView) findViewById(R.id.textView6);

        ename = (EditText) findViewById(R.id.editText1);
        econt = (EditText) findViewById(R.id.editText2);
        eadd = (EditText) findViewById(R.id.editText3);
        epsw = (EditText) findViewById(R.id.editText4);

        butn = (Button) findViewById(R.id.button1);

        spinn1 = (Spinner) findViewById(R.id.spinner1D);
        spinn1.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
        ArrayAdapter ae = new ArrayAdapter(this, android.R.layout.simple_spinner_item,bName);
        ae.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinn1.setAdapter(ae);

        databaseReference= FirebaseDatabase.getInstance().getReferenceFromUrl("https://locate-my-bus-5e265-default-rtdb.firebaseio.com/");

        butn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = ename.getText().toString();
                String cont = econt.getText().toString();
                String add = eadd.getText().toString();
                String numb = epsw.getText().toString();

                if(name.isEmpty()||cont.isEmpty()||add.isEmpty()||numb.isEmpty()) {
                    Toast.makeText(Driver_Reg.this, "Please enter all details", Toast.LENGTH_SHORT).show();
                }
                else {
                    databaseReference.child("Driver").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChild(name)) {
                                Toast.makeText(Driver_Reg.this, "Already Registered", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                databaseReference.child("Driver").child(cont).child("Name").setValue(name);
                                databaseReference.child("Driver").child(cont).child("Contact").setValue(cont);
                                databaseReference.child("Driver").child(cont).child("Address").setValue(add);
                                databaseReference.child("Driver").child(cont).child("BusName").setValue(bname);
                                databaseReference.child("Driver").child(cont).child("Password").setValue(numb);

                                Toast.makeText(Driver_Reg.this, "Successfully Registered", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(Driver_Reg.this, "Error"+error.getMessage().toString(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        bname = spinn1.getSelectedItem().toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}