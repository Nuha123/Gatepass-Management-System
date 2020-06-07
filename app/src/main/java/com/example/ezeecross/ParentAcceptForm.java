package com.example.ezeecross;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class ParentAcceptForm extends Fragment {
String format, phone, s="";
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        final View root = inflater.inflate(R.layout.parent_accept_form, container, false);
        final TextView reason = root.findViewById(R.id.reason);
        final TextView indate = root.findViewById(R.id.indate);
        final TextView intime = root.findViewById(R.id.intime);
        final TextView outdate = root.findViewById(R.id.outdate);
        final TextView outtime = root.findViewById(R.id.outtime);
        final Button accept = root.findViewById(R.id.accept);
        SimpleDateFormat sf=new SimpleDateFormat("dd-MM-yyyy");
        format = sf.format(new Date());
        final FirebaseAuth auth;
        auth = FirebaseAuth.getInstance();
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("parent").document(auth.getCurrentUser().getEmail());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document=task.getResult();
                    s=document.get("stdmail").toString();
                    db.collection("student").document(s).collection("gatepass").document(format).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if(!document.exists()){

                                }else {
                                    if (document.get("acceptp").toString().equals("false")) {
                                        reason.setText(document.get("reason").toString());
                                        indate.setText(document.get("indate").toString());
                                        intime.setText(document.get("intime").toString());
                                        outdate.setText(document.get("outdate").toString());
                                        outtime.setText(document.get("outtime").toString());
                                    }
                                }
                       }
                        }
                    });
                }
            }
        });

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DocumentReference docRef = db.collection("parent").document(auth.getCurrentUser().getEmail());
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot document=task.getResult();
                            phone=document.get("phone").toString();
                            s=document.get("stdmail").toString();
                            db.collection("student").document(s).collection("gatepass").document(format).update("acceptp","true");
                            Toast.makeText(root.getContext(),"Request sent to warden",Toast.LENGTH_LONG).show();

                        }
                    }
                });
                Intent i = new Intent(root.getContext(),MainActivity1.class);
                startActivity(i);

            }
        });

        return root;
    }
}
