package com.example.ezeecross;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ServerTimestamp;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class StudentForm extends Fragment {
    FirebaseAuth auth;

    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        final View root = inflater.inflate(R.layout.student_form, container, false);
        final TextView name=root.findViewById(R.id.name);
        final Spinner warden =root.findViewById(R.id.warden);
        final TextView rnum=root.findViewById(R.id.rnum);
        final TextView yr=root.findViewById(R.id.yr);
        final EditText reg=root.findViewById(R.id.reg);
        final EditText outdate=root.findViewById(R.id.outdate);
        final EditText outtime=root.findViewById(R.id.outtime);
        final EditText indate=root.findViewById(R.id.indate);
        final EditText intime=root.findViewById(R.id.intime);
        Button sendform=root.findViewById(R.id.sendform);

        final String[] wardens= new String[8];
        wardens[0]="Select Your Warden";
        wardens[1]="latha@gmail.com";
        wardens[2]="devaki@gmail.com";
        wardens[3]="parvathi@gmail.com";
        wardens[4]="shiny@yahoo.com";
        wardens[5]="laxman@gmail.com";
        wardens[6]="venkappa@rediff.com";
        wardens[7]="kantaRaju@gmail.com";
        final ArrayAdapter<String> adapter=new ArrayAdapter<>(root.getContext(),android.R.layout.simple_spinner_item,wardens);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        warden.setAdapter(adapter);

        db.collection("student").document(auth.getCurrentUser().getEmail()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot documentSnapshot=task.getResult();
                    name.setText(documentSnapshot.get("name").toString());
                    rnum.setText(documentSnapshot.get("reg").toString());
                    yr.setText(documentSnapshot.get("stdyear").toString());
                }
            }
        });

        sendform.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                final String freg=reg.getText().toString().trim();
                final String foutdate=outdate.getText().toString().trim();
                final String fouttime=outtime.getText().toString().trim();
                final String findate=indate.getText().toString().trim();
                final String fintime=intime.getText().toString().trim();
                final String fwarden=warden.getSelectedItem().toString().trim();
                final String user=auth.getCurrentUser().getEmail().trim();

                if (freg.equals("")||foutdate.equals("")||fouttime.equals("")||findate.equals("")||fintime.equals("")) {
                    Toast.makeText(root.getContext(), "Enter all the details", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(fwarden.equals("Select Your Warden")) {
                    Toast.makeText(root.getContext(), "Select Your Warden", Toast.LENGTH_SHORT).show();
                    return;
                }


                Map<String, Object> form = new HashMap<>();
                form.put("user",user);
                form.put("name",name.getText().toString());
                form.put("regnum",rnum.getText().toString());
                form.put("year",yr.getText().toString());
                form.put("warden",fwarden);
                form.put("reason",freg);
                form.put("outdate",foutdate);
                form.put("outtime",fouttime);
                form.put("indate",findate);
                form.put("intime",fintime);
                form.put("acceptw","false");
                form.put("acceptp","false");
                SimpleDateFormat s=new SimpleDateFormat("dd-MM-yyyy");
                String format = s.format(new Date());
                db.collection("student").document(auth.getCurrentUser().getEmail()).collection("gatepass").document(format).set(form);
                Toast.makeText(root.getContext(),"Your request has been sent to your parent", Toast.LENGTH_LONG).show();
                startActivity(new Intent(root.getContext(), MainActivity2.class));

            }
        });
        return root;


    }
}