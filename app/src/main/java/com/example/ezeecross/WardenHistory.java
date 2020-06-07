package com.example.ezeecross;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class WardenHistory extends Fragment {
    RecyclerView recyclerView;
    String d,s,name;
    int i=0;
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        final View root = inflater.inflate(R.layout.warden_history, container, false);

        recyclerView= root.findViewById(R.id.recycler_view);
        final String[] rnum=new String[200];
        final String[] date=new String[200];
        final String[] outdate=new String[200];
        final String[] outtime=new String[200];
        final String[] indate=new String[200];
        final String[] intime=new String[200];
        final String[] reason=new String[200];

        final FirebaseAuth auth;
        auth = FirebaseAuth.getInstance();
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        name=auth.getCurrentUser().getEmail();
        db.collection("student").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                d=document.getId();
                                db.collection("student").document(d).collection("gatepass").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (QueryDocumentSnapshot dc : task.getResult()) {
                                                s = dc.getId();
                                                if ((dc.exists())) {
                                                    if (dc.get("warden").toString().equals(name)) {
                                                        if (dc.get("acceptw").toString().equals("true")) {
                                                            outdate[i] = dc.get("outdate").toString();
                                                            outtime[i] = dc.get("outtime").toString();
                                                            indate[i] = dc.get("indate").toString();
                                                            intime[i] = dc.get("intime").toString();
                                                            reason[i] = dc.get("reason").toString();
                                                            date[i] = dc.getId();
                                                            rnum[i] = dc.get("regnum").toString();
                                                            i++;
                                                        }
                                                    }


                                                }
                                            }
                                        }
                                    }
                                });

                            }
                        }
                    }
                });

        RecViewAdapWarden rva =new RecViewAdapWarden(root.getContext(),rnum,outdate,outtime,indate,intime,reason,date);
        recyclerView.setAdapter(rva);

        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(root.getContext());
        recyclerView.setLayoutManager(layoutManager);

        return root;
    }



}
