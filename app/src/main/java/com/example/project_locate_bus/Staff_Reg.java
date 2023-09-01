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

public class Staff_Reg extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    TextView Name,Usr,Pass,CPass,Id,Mob,Mail,Addr,Dept,Bname,Bstop;
    EditText ename,eusr,epass,ecpass,eid,emob,email,eaddr,ebstop;
    Spinner spin1,spin2;
    String dept[] = {"Select Course","AE","CIVIL","MECHANICAL","MBA","MCA","AERONAUTICAL","CS"};
    String bname[] = {"Select Bus","Kottarakkara","Haripad","Ranni","Thiruvalla","Mannar","Pathanapuram"};
    DatabaseReference databaseReference;
    String depnt,bus;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_reg);

        addListenerOnButtonClick();
    }
    public void addListenerOnButtonClick() {
        Name = (TextView) findViewById(R.id.textView2);
        Usr = (TextView) findViewById(R.id.textView3);
        Pass = (TextView) findViewById(R.id.textView4);
        CPass = (TextView) findViewById(R.id.textView12);
        Id = (TextView) findViewById(R.id.textView5);
        Mob = (TextView) findViewById(R.id.textView6);
        Mail = (TextView) findViewById(R.id.textView7);
        Addr = (TextView) findViewById(R.id.textView8);
        Dept = (TextView) findViewById(R.id.textView9);
        Bname = (TextView) findViewById(R.id.textView10);
        Bstop = (TextView) findViewById(R.id.textView11);

        ename = (EditText) findViewById(R.id.editText1);
        eusr = (EditText) findViewById(R.id.editText2);
        epass = (EditText) findViewById(R.id.editText3);
        ecpass = (EditText) findViewById(R.id.editText9);
        eid = (EditText) findViewById(R.id.editText4);
        emob = (EditText) findViewById(R.id.editText5);
        email = (EditText) findViewById(R.id.editText6);
        eaddr = (EditText) findViewById(R.id.editText7);
        ebstop = (EditText) findViewById(R.id.editText8);

        spin1 = (Spinner) findViewById(R.id.spinner1S);
        spin1.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
        ArrayAdapter sa = new ArrayAdapter(this, android.R.layout.simple_spinner_item,dept);
        sa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin1.setAdapter(sa);

        spin2 = (Spinner) findViewById(R.id.spinner2S);
        spin2.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
        ArrayAdapter sb = new ArrayAdapter(this, android.R.layout.simple_spinner_item,bname);
        sb.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin2.setAdapter(sb);

        btn = (Button) findViewById(R.id.button1);

        databaseReference= FirebaseDatabase.getInstance().getReferenceFromUrl("https://locate-my-bus-5e265-default-rtdb.firebaseio.com/");

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Name = ename.getText().toString();
                String User = eusr.getText().toString();
                String Pass = epass.getText().toString();
                String Cpass = ecpass.getText().toString();
                String Id = eid.getText().toString();
                String Mob = emob.getText().toString();
                String Email = email.getText().toString();
                String Add = eaddr.getText().toString();
                String Bstop = ebstop.getText().toString();

                if(Name.isEmpty()||User.isEmpty()||Pass.isEmpty()||Cpass.isEmpty()||Id.isEmpty()||Mob.isEmpty()||Email.isEmpty()||Bstop.isEmpty()||Add.isEmpty()) {
                    Toast.makeText(Staff_Reg.this, "Please enter all details", Toast.LENGTH_SHORT).show();
                }

                if(Pass.equals(Cpass))
                {
                    databaseReference.child("Faculty").addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.hasChild(Mob))
                            {
                                Toast.makeText(Staff_Reg.this, "USERNAME is already Registered", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                databaseReference.child("Faculty").child(Mob).child("Name").setValue(Name);
                                databaseReference.child("Faculty").child(Mob).child("Username").setValue(User);
                                databaseReference.child("Faculty").child(Mob).child("Password").setValue(Pass);
                                databaseReference.child("Faculty").child(Mob).child("FacultyId").setValue(Id);
                                databaseReference.child("Faculty").child(Mob).child("Contact").setValue(Mob);
                                databaseReference.child("Faculty").child(Mob).child("Email").setValue(Email);
                                databaseReference.child("Faculty").child(Mob).child("Department").setValue(depnt);
                                databaseReference.child("Faculty").child(Mob).child("BusName").setValue(bus);
                                databaseReference.child("Faculty").child(Mob).child("BusStop").setValue(Bstop);
                                databaseReference.child("Faculty").child(Mob).child("Address").setValue(Add);

                                Toast.makeText(Staff_Reg.this, "User Successfully Registered", Toast.LENGTH_SHORT).show();
                                finish();
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(Staff_Reg.this, "error"+error.getMessage().toString(), Toast.LENGTH_SHORT).show();

                        }
                    });
                }
                else
                {
                    Toast.makeText(Staff_Reg.this, "Password Doesn't Match", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        depnt = spin1.getSelectedItem().toString();
        bus = spin2.getSelectedItem().toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}