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
import android.widget.TextView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Fee_payment extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    TextView name,phn,depat,month,amount,date,dues;
    EditText ename,ephn,eamount,edate,edues;
    Spinner spinner1,spinner2;
    String mon[] = {"Select Month","January","February","March","April","May","June","July","August","September","October","November","December"};
    String deprt[] = {"Select Course","AE","CIVIL","MECHANICAL","MBA","MCA","AERONAUTICAL","CS"};
    String depmt,mont;
    DatabaseReference databaseReference;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fee_payment);

        addListenerOnButtonClick();
    }
    public void addListenerOnButtonClick() {
        name = (TextView) findViewById(R.id.textview2);
        phn = (TextView) findViewById(R.id.textview8);
        depat = (TextView) findViewById(R.id.textview3);
        month = (TextView) findViewById(R.id.textview4);
        amount = (TextView) findViewById(R.id.textview5);
        date = (TextView) findViewById(R.id.textview6);
        dues = (TextView) findViewById(R.id.textview7);

        ename = (EditText) findViewById(R.id.edittext1);
        ephn = (EditText) findViewById(R.id.edittext5);
        eamount = (EditText) findViewById(R.id.edittext2);
        edate = (EditText) findViewById(R.id.edittext3);
        edues = (EditText) findViewById(R.id.edittext4);

        spinner1 = (Spinner) findViewById(R.id.spinner1F);
        spinner1.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
        ArrayAdapter af = new ArrayAdapter(this, android.R.layout.simple_spinner_item,deprt);
        af.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(af);

        spinner2 = (Spinner) findViewById(R.id.spinner2F);
        spinner2.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
        ArrayAdapter ag = new ArrayAdapter(this, android.R.layout.simple_spinner_item,mon);
        ag.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(ag);

        databaseReference= FirebaseDatabase.getInstance().getReferenceFromUrl("https://locate-my-bus-5e265-default-rtdb.firebaseio.com/");

        btn = (Button) findViewById(R.id.button1);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Name = ename.getText().toString();
                String Mobile = ephn.getText().toString();
                String Amount = eamount.getText().toString();
                String Date = edate.getText().toString();
                String Due = edues.getText().toString();

                if(Name.isEmpty()||Mobile.isEmpty()||Amount.isEmpty()||Date.isEmpty()||Due.isEmpty()) {
                    Toast.makeText(Fee_payment.this, "Please enter all details", Toast.LENGTH_SHORT).show();
                }
                else {
                    databaseReference.child("Fee").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChild(Name)) {
                                Toast.makeText(Fee_payment.this, "Already updated", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                databaseReference.child("Fee").child(Name).child("Name").setValue(Name);
                                databaseReference.child("Fee").child(Name).child("Mobile").setValue(Mobile);
                                databaseReference.child("Fee").child(Name).child("Department").setValue(depmt);
                                databaseReference.child("Fee").child(Name).child("Month").setValue(mont);
                                databaseReference.child("Fee").child(Name).child("Amount").setValue(Amount);
                                databaseReference.child("Fee").child(Name).child("Date").setValue(Date);
                                databaseReference.child("Fee").child(Name).child("Dues").setValue(Due);

                                Toast.makeText(Fee_payment.this, "Successfully Registered", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(Fee_payment.this, "Error"+error.getMessage().toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        depmt = spinner1.getSelectedItem().toString();
        mont = spinner2.getSelectedItem().toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}