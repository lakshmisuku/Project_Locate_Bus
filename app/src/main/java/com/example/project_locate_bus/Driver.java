package com.example.project_locate_bus;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Driver#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Driver extends Fragment implements AdapterView.OnItemSelectedListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Spinner spinn1,spinn2;
    String driver,busnm;
    String driv[] = {"1","2","3","4"};
    String bus[] = {"Select Bus","Kottarakkara","Haripad","Ranni","Thiruvalla","Mannar","Pathanapuram"};
    Button assign;
    DatabaseReference databaseReference;
    public Driver() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Driver.
     */
    // TODO: Rename and change types and number of parameters
    public static Driver newInstance(String param1, String param2) {
        Driver fragment = new Driver();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_driver, container, false);

        spinn1 = (Spinner) v.findViewById(R.id.spinner2D);
        spinn1.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
        ArrayAdapter aa = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item,driv);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinn1.setAdapter(aa);

        spinn2 = (Spinner) v.findViewById(R.id.spinner3D);
        spinn2.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
        ArrayAdapter ab = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item,bus);
        ab.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinn2.setAdapter(ab);

        assign = (Button) v.findViewById(R.id.asign);

        databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://locate-my-bus-5e265-default-rtdb.firebaseio.com/Driver");

        assign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s1 = spinn1.getSelectedItem().toString();
                String s2 = spinn2.getSelectedItem().toString();

                databaseReference.child("Driver").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        databaseReference.child("Driver").child("BusName").setValue(spinn1);
                        databaseReference.child("Driver").child("Number").setValue(spinn2);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {


                    }
                });


            }
        });

        return v;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        driver = spinn1.getSelectedItem().toString();
        busnm = spinn2.getSelectedItem().toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}