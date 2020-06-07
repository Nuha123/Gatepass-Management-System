package com.example.ezeecross;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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


public class StudentHistory extends Fragment {
    RecyclerView recyclerView;
    int i;
    String s,d;
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        final View root = inflater.inflate(R.layout.student_history, container, false);
        //final TextView textView = root.findViewById(R.id.section_label);;

        recyclerView= root.findViewById(R.id.recycler_view);
        final String[] outdate=new String[200];
        final String[] outtime=new String[200];
        final String[] indate=new String[200];
        final String[] intime=new String[200];
        final String[] reason=new String[200];
        final String[] date = new String[200];

        final FirebaseAuth auth;
        auth = FirebaseAuth.getInstance();
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
                    db.collection("student").document(auth.getCurrentUser().getEmail()).collection("gatepass").orderBy("outdate", Query.Direction.DESCENDING).get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            d=document.getId();
                                            if(document.exists()) {
                                                db.collection("student").document(auth.getCurrentUser().getEmail()).collection("gatepass").document(d).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                        if (task.isSuccessful()) {

                                                            DocumentSnapshot dc = task.getResult();
                                                            if ((dc.exists())) {
                                                                if (dc.get("acceptw").toString().equals("true")) {
                                                                    outdate[i] = dc.get("outdate").toString();
                                                                    outtime[i] = dc.get("outtime").toString();
                                                                    indate[i] = dc.get("indate").toString();
                                                                    intime[i] = dc.get("intime").toString();
                                                                    reason[i] = dc.get("reason").toString();
                                                                    date[i] = dc.getId();
                                                                    i++;
                                                                    // Toast.makeText(getContext(), reason[i]+i, Toast.LENGTH_SHORT).show();


                                                                }
                                                            }
                                                        }
                                                    }
                                                });
                                            }

                                        }
                                    }
                                }
                            });




        RecViewAdapStudPar rva =new RecViewAdapStudPar(root.getContext(),outdate,outtime,indate,intime,reason,date);
        recyclerView.setAdapter(rva);

        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(root.getContext());
        recyclerView.setLayoutManager(layoutManager);
        return root;
    }



}
