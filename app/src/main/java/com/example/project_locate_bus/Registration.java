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

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Registration extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    TextView Name,Usr,Pswd,CPass,Reg,Adm,Con,Mail,Course,Sem,Batch,Bstop,Bname,Add,Gname,Gcon;
    EditText eName,eUsr,ePswd,eCpass,eReg,eAdm,eCon,eMail,eBatch,eBstop,eAdd,eGname,eGcon;
    Spinner Spin1,Spin2,Spin3;
    String course[] = {"Select Course","AE","CIVIL","MECHANICAL","MBA","MCA","AERONAUTICAL","CS"};
    String sem[] = {"Select Semester","I","II","III","IV","V","VI","VII","VIII"};
    String bname[] = {"Select Bus","Kottarakkara","Haripad","Ranni","Thiruvalla","Mannar","Pathanapuram"};
    String cour,seme,bnam;
    Button btn1;
    DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        addListenerOnButtonClick();
    }
    public void addListenerOnButtonClick() {
        Name = (TextView) findViewById(R.id.textView2);
        Usr = (TextView) findViewById(R.id.textView3);
        Pswd = (TextView) findViewById(R.id.textView4);
        CPass = (TextView) findViewById(R.id.textView17);
        Reg = (TextView) findViewById(R.id.textView5);
        Adm = (TextView) findViewById(R.id.textView6);
        Con = (TextView) findViewById(R.id.textView7);
        Mail = (TextView) findViewById(R.id.textView8);
        Course = (TextView) findViewById(R.id.textView9);
        Sem = (TextView) findViewById(R.id.textView10);
        Batch = (TextView) findViewById(R.id.textView11);
        Bname = (TextView) findViewById(R.id.textView12);
        Bstop = (TextView) findViewById(R.id.textView13);
        Add = (TextView) findViewById(R.id.textView14);
        Gname = (TextView) findViewById(R.id.textView15);
        Gcon = (TextView) findViewById(R.id.textView16);

        eName = (EditText) findViewById(R.id.editText1);
        eUsr = (EditText) findViewById(R.id.editText2);
        ePswd = (EditText) findViewById(R.id.editText3);
        eCpass = (EditText) findViewById(R.id.editText13);
        eReg = (EditText) findViewById(R.id.editText4);
        eAdm = (EditText) findViewById(R.id.editText5);
        eCon = (EditText) findViewById(R.id.editText6);
        eMail = (EditText) findViewById(R.id.editText7);
        eBatch = (EditText) findViewById(R.id.editText8);
        eBstop = (EditText) findViewById(R.id.editText9);
        eAdd = (EditText) findViewById(R.id.editText10);
        eGname = (EditText) findViewById(R.id.editText11);
        eGcon = (EditText) findViewById(R.id.editText12);

        Spin1 = (Spinner) findViewById(R.id.spinner1);
        Spin1.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item,course);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spin1.setAdapter(aa);

        Spin2 = (Spinner) findViewById(R.id.spinner2);
        Spin2.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
        ArrayAdapter ab = new ArrayAdapter(this, android.R.layout.simple_spinner_item,sem);
        ab.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spin2.setAdapter(ab);

        Spin3 = (Spinner) findViewById(R.id.spinner3);
        Spin3.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
        ArrayAdapter ac = new ArrayAdapter(this, android.R.layout.simple_spinner_item,bname);
        ac.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spin3.setAdapter(ac);

        btn1 = (Button) findViewById(R.id.button1);

        databaseReference= FirebaseDatabase.getInstance().getReferenceFromUrl("https://locate-my-bus-5e265-default-rtdb.firebaseio.com/");

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Name = eName.getText().toString();
                String Usr = eUsr.getText().toString();
                String Pass = ePswd.getText().toString();
                String Cpass = eCpass.getText().toString();
                String Reg = eReg.getText().toString();
                String Adms = eAdm.getText().toString();
                String Cont = eCon.getText().toString();
                String Mail = eMail.getText().toString();
                String Batch = eBatch.getText().toString();
                String BStop = eBstop.getText().toString();
                String Addr = eAdd.getText().toString();
                String GName = eGname.getText().toString();
                String GCont = eCon.getText().toString();

                if(Name.isEmpty()||Usr.isEmpty()||Pass.isEmpty()||Cpass.isEmpty()||Reg.isEmpty()||Adms.isEmpty()||Cont.isEmpty()||Mail.isEmpty()||Batch.isEmpty()||BStop.isEmpty()||Addr.isEmpty()||GName.isEmpty()||GCont.isEmpty()){
                    Toast.makeText(Registration.this, "Please enter all details", Toast.LENGTH_SHORT).show();
                }
                else {
                    if(Pass.equals(Cpass))
                    {
                        databaseReference.child("Student").addListenerForSingleValueEvent(new ValueEventListener() {

                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.hasChild(Cont))
                                {
                                    Toast.makeText(Registration.this, "USERNAME is already Registered", Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    databaseReference.child("Student").child(Cont).child("Name").setValue(Name);
                                    databaseReference.child("Student").child(Cont).child("Username").setValue(Usr);
                                    databaseReference.child("Student").child(Cont).child("Password").setValue(Pass);
                                    databaseReference.child("Student").child(Cont).child("RegisterNo").setValue(Reg);
                                    databaseReference.child("Student").child(Cont).child("AdmissionNo").setValue(Adms);
                                    databaseReference.child("Student").child(Cont).child("Contact").setValue(Cont);
                                    databaseReference.child("Student").child(Cont).child("Email").setValue(Mail);
                                    databaseReference.child("Student").child(Cont).child("Course").setValue(cour);
                                    databaseReference.child("Student").child(Cont).child("Semester").setValue(seme);
                                    databaseReference.child("Student").child(Cont).child("Batch").setValue(Batch);
                                    databaseReference.child("Student").child(Cont).child("BusName").setValue(bnam);
                                    databaseReference.child("Student").child(Cont).child("BusStop").setValue(BStop);
                                    databaseReference.child("Student").child(Cont).child("Address").setValue(Addr);
                                    databaseReference.child("Student").child(Cont).child("GuardianName").setValue(GName);
                                    databaseReference.child("Student").child(Cont).child("GuardianContact").setValue(GCont);

                                    Toast.makeText(Registration.this, "User Successfully Registered", Toast.LENGTH_SHORT).show();
                                    finish();
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(Registration.this, "error"+error.getMessage().toString(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    else
                    {
                        Toast.makeText(Registration.this, "Password Doesn't Match", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        cour = Spin1.getSelectedItem().toString();
        seme = Spin2.getSelectedItem().toString();
        bnam = Spin3.getSelectedItem().toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}