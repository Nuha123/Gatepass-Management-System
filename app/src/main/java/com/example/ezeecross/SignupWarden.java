package com.example.ezeecross;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignupWarden extends AppCompatActivity {
Spinner yr;
Spinner gender;
Spinner block;
FirebaseAuth auth;
Button wlogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_warden);
        yr=findViewById(R.id.yr);
        wlogin= findViewById(R.id.wlogin);
        final String[] branch= new String[5];
        branch[0]="Select the Year Handled By You";
        branch[1]="4";
        branch[2]="3";
        branch[3]="2";
        branch[4]="1";
        auth = FirebaseAuth.getInstance();
        ArrayAdapter<String> adapter=new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,branch);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yr.setAdapter(adapter);
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        gender=findViewById(R.id.gender);
        final String[] gd= new String[3];
        gd[0]="Select Gender";
        gd[1]="Male";
        gd[2]="Female";
        ArrayAdapter<String> adapter1=new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,gd);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gender.setAdapter(adapter1);

        final EditText wname=findViewById(R.id.wname);
        final EditText wemail=findViewById(R.id.wemail);
        final EditText wnumber=findViewById(R.id.wnumber);
        final EditText wpassword=findViewById(R.id.wpassword);

        wlogin=findViewById(R.id.wlogin);
        wlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String e = wemail.getText().toString().trim();
                final String p =wpassword.getText().toString().trim();
                final String namew=wname.getText().toString().trim();
                final String phone=wnumber.getText().toString().trim();
                final String gen=gender.getSelectedItem().toString().trim();
                final String year=yr.getSelectedItem().toString().trim();

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
                if (namew.equals("")||phone.equals("")) {
                    Toast.makeText(getApplicationContext(), "Enter all the details", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(year.equals("Select the Year Handled By You")){
                    Toast.makeText(getApplicationContext(), "Select the Year Handled By You", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(gen.equals("Select Gender")){
                    Toast.makeText(getApplicationContext(), "Select Gender", Toast.LENGTH_SHORT).show();
                    return;
                }


                //create user
                auth.createUserWithEmailAndPassword(e,p)
                        .addOnCompleteListener(SignupWarden.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                               // Toast.makeText(SignupWarden.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                if (!task.isSuccessful()) {
                                    Toast.makeText(SignupWarden.this, "Authentication failed." + task.getException(),
                                            Toast.LENGTH_SHORT).show();
                                } else {

                                    Map<String, Object> user = new HashMap<>();

                                    user.put("name",namew);
                                    user.put("email",e);
                                    user.put("year",year);
                                    user.put("gender",gen);
                                    user.put("phone",phone);
                                    user.put("password",p);

                                    db.collection("warden").document(e).set(user);

                                    Map<String, Object> user1 = new HashMap<>();
                                    user1.put("role","warden");
                                    db.collection("users").document(e).set(user1);
                                    startActivity(new Intent(SignupWarden.this, MainActivity.class));
                                    finish();
                                }
                            }
                        });

            }
        });
    }
}
