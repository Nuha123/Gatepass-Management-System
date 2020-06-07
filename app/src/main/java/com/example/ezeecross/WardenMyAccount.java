package com.example.ezeecross;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

public class WardenMyAccount extends Fragment {
    Spinner yr;
    TextView name;
    TextView email;
    TextView gender;
    EditText number;
    Spinner block;
    RelativeLayout sc;
    RelativeLayout changepwd;
    Button savechanges;
    EditText passwordnew;
    EditText password2;
    EditText Password;

    FirebaseAuth auth;

    final FirebaseFirestore db = FirebaseFirestore.getInstance();
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        final View root = inflater.inflate(R.layout.warden_my_account, container, false);
        yr=root.findViewById(R.id.yr);
        auth = FirebaseAuth.getInstance();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String[] branch= new String[4];
        branch[0]="4";
        branch[1]="3";
        branch[2]="2";
        branch[3]="1";
        ArrayAdapter<String> adapter=new ArrayAdapter<>(root.getContext(),android.R.layout.simple_spinner_item,branch);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yr.setAdapter(adapter);
        sc=root.findViewById(R.id.sc);
        name=root.findViewById(R.id.name);
        email=root.findViewById(R.id.email);
        gender=root.findViewById(R.id.gender);
        number=root.findViewById(R.id.number);
        savechanges=root.findViewById(R.id.savechanges);
        changepwd=root.findViewById(R.id.parent);
        passwordnew= root.findViewById(R.id.password1);
        changepwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder= new AlertDialog.Builder(root.getContext());
                builder.setCancelable(false);
                final View view= LayoutInflater.from(root.getContext()).inflate(R.layout.changepassword,null);
                Button buttonYes=view.findViewById(R.id.savechanges);
                Button buttonNo=view.findViewById(R.id.cancel);
                builder.setView(view);
                final AlertDialog dialog=builder.create();

                buttonYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText password1=view.findViewById(R.id.password1);
                        EditText password2=view.findViewById(R.id.password2);
                        final String  newpass=password1.getText().toString().trim();

                        if(newpass.equals(password2.getText().toString().trim())) {
                            user.updatePassword(newpass).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {


                                        Toast.makeText(root.getContext(), "Password Changed", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                    }
                                }
                            });
                        }
                        else{
                            Toast.makeText(root.getContext(), "Enter Password again. Both the fields do not match!", Toast.LENGTH_SHORT).show();
                        }




                    }
                });
                buttonNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });




        DocumentReference docRef = db.collection("warden").document(auth.getCurrentUser().getEmail());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document=task.getResult();
                    name.setText(document.get("name").toString());
                    email.setText(document.get("email").toString());
                    gender.setText(document.get("gender").toString());
                    number.setText(document.get("phone").toString());
                    String s= document.get("year").toString();
                    for(int i=1;i<4;i++){
                        if(branch[i].equals(s)){
                            String t= branch[0];
                            branch[0]=branch[i];
                            branch[i]=t;
                        }
                    }
                    ArrayAdapter<String> adapter=new ArrayAdapter<>(root.getContext(),android.R.layout.simple_spinner_item,branch);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    yr.setAdapter(adapter);
                    sc.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            db.collection("warden").document(auth.getCurrentUser().getEmail()).update("phone",number.getText().toString());
                            db.collection("warden").document(auth.getCurrentUser().getEmail()).update("year",yr.getSelectedItem().toString());
                            Toast.makeText(getContext(), "Changes have been Made", Toast.LENGTH_SHORT).show();

                        }
                    });

                }
            }
        });


        return root;
    }
}
