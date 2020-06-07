package com.example.ezeecross;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class WardenAccept extends Fragment {
TextView name;
TextView rnum;
TextView yr;
TextView reason;
TextView outdate;
TextView outtime;
TextView indate;
TextView intime;
Button accept;
String format,s;
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        final View root = inflater.inflate(R.layout.warden_accept, container, false);
        name=root.findViewById(R.id.name);
        rnum=root.findViewById(R.id.rnum);
        yr=root.findViewById(R.id.yr);
        reason=root.findViewById(R.id.reason);
        outdate=root.findViewById(R.id.outdate);
        outtime=root.findViewById(R.id.outtime);
        indate=root.findViewById(R.id.indate);
        intime=root.findViewById(R.id.intime);
        accept=root.findViewById(R.id.accept);

        SimpleDateFormat sf=new SimpleDateFormat("dd-MM-yyyy");
        format = sf.format(new Date());
        final FirebaseAuth auth;
        auth = FirebaseAuth.getInstance();
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("warden").document(auth.getCurrentUser().getEmail()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document=task.getResult();
                    db.collection("student").get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            s=document.getId().toString();
                                           db.collection("student").document(s).collection("gatepass").document(format).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                               @Override
                                               public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                   if(task.isSuccessful()){
                                                       DocumentSnapshot dc=task.getResult();
                                                       if((dc.exists())) {
                                                           if (dc.get("warden").toString().equals(auth.getCurrentUser().getEmail())) {
                                                               if (dc.get("acceptw").toString().equals("false") && dc.get("acceptp").toString().equals("true")) {
                                                                   reason.setText(dc.get("reason").toString());
                                                                   indate.setText(dc.get("indate").toString());
                                                                   intime.setText(dc.get("intime").toString());
                                                                   outdate.setText(dc.get("outdate").toString());
                                                                   outtime.setText(dc.get("outtime").toString());
                                                                   name.setText(dc.get("name").toString());
                                                                   rnum.setText(dc.get("regnum").toString());
                                                                   yr.setText(dc.get("year").toString());
                                                                   s = dc.get("user").toString();

                                                               }
                                                               accept.setOnClickListener(new View.OnClickListener() {
                                                                   @Override
                                                                   public void onClick(View view) {
                                                                       Toast.makeText(root.getContext(), "Request Accepted", Toast.LENGTH_LONG).show();
                                                                       DocumentReference docRef = db.collection("warden").document(auth.getCurrentUser().getEmail());
                                                                       docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                           @Override
                                                                           public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                               if (task.isSuccessful()) {
                                                                                   DocumentSnapshot document = task.getResult();
                                                                                   db.collection("student").document(s).collection("gatepass").document(format).update("acceptw", "true");

                                                                               }
                                                                           }
                                                                       });

                                                                       Intent i = new Intent(root.getContext(), MainActivity.class);
                                                                       startActivity(i);


                                                                   }
                                                               });
                                                           }
                                                       }
                                                   }
                                               }
                                           });

                                        }
                                    }
                                }
                            });
                }
            }
        });




        return root;

    }
}
