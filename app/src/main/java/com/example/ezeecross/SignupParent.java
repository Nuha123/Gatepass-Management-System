package com.example.ezeecross;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;



public class SignupParent extends AppCompatActivity {
    Spinner yr;
    Spinner gender;
    Button login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_parent);
        yr=findViewById(R.id.yr);

        login=findViewById(R.id.login);
        final FirebaseAuth auth;

        final EditText email=findViewById(R.id.email);
        final EditText password=findViewById(R.id.password);

        auth = FirebaseAuth.getInstance();
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        final String[] branch= new String[5];
        branch[0]="Select Your Student's Year";
        branch[1]="4";
        branch[2]="3";
        branch[3]="2";
        branch[4]="1";
        ArrayAdapter<String> adapter=new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,branch);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yr.setAdapter(adapter);

        gender=findViewById(R.id.gender);
        final String[] gd= new String[3];
        gd[0]="Select Your Student's Gender";
        gd[1]="Male";
        gd[2]="Female";
        ArrayAdapter<String> adapter1=new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,gd);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gender.setAdapter(adapter1);

        login=findViewById(R.id.login);

        final EditText pname=findViewById(R.id.pname);
        final EditText stdname=findViewById(R.id.stdname);
        final EditText stdmail=findViewById(R.id.stdmail);
        final EditText reg=findViewById(R.id.reg);
        final EditText number=findViewById(R.id.number);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String e = email.getText().toString().trim();
                final String p = password.getText().toString().trim();
                final String namep=pname.getText().toString().trim();
                final String namestd=stdname.getText().toString();
                final String regstd=reg.getText().toString();
                final String pnumber=number.getText().toString();
                final String year= yr.getSelectedItem().toString().trim();
                final String gen= gender.getSelectedItem().toString().trim();
                final String mailstd=stdmail.getText().toString();
                if (TextUtils.isEmpty(e)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(p)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (p.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (namep.equals("")||namestd.equals("")||regstd.equals("")||pnumber.equals("")||mailstd.equals("")) {
                    Toast.makeText(getApplicationContext(), "Enter all the details", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(year.equals("Select Your Student's Year")){
                    Toast.makeText(getApplicationContext(), "Select your student's year", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(gen.equals("Select Your Student's Gender")){
                    Toast.makeText(getApplicationContext(), "Select Your Student's Gender", Toast.LENGTH_SHORT).show();
                    return;
                }

                //create user
                auth.createUserWithEmailAndPassword(e,p)
                        .addOnCompleteListener(SignupParent.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                //Toast.makeText(SignupParent.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();

                                if (!task.isSuccessful()) {
                                    Toast.makeText(SignupParent.this, "Authentication failed." + task.getException(),
                                            Toast.LENGTH_SHORT).show();
                                } else {

                                    Map<String, Object> user = new HashMap<>();

                                    user.put("name",namep);
                                    user.put("email",e);
                                    user.put("stdname",namestd);
                                    user.put("stdmail",mailstd);
                                    user.put("reg",regstd);
                                    user.put("stdyear",year);
                                    user.put("gender",gen);
                                    user.put("phone",pnumber);
                                    user.put("password",p);
                                    db.collection("parent").document(e).set(user);

                                    Map<String, Object> user1 = new HashMap<>();
                                    user1.put("role","parent");
                                    db.collection("users").document(e).set(user1);
                                    startActivity(new Intent(SignupParent.this, MainActivity1.class));
                                    finish();
                                }
                            }
                        });

            }
        });

    }
}
