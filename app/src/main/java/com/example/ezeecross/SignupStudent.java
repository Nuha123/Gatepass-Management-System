package com.example.ezeecross;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignupStudent extends AppCompatActivity {
    private static final String TAG = "SignupStudent";
    Spinner yr;
Button login1;
Spinner gender;
Spinner block;
FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_student);
        yr=findViewById(R.id.yr);
        login1=findViewById(R.id.login);
        final String[] branch= new String[5];
        branch[0]="Select Your Year of study";
        branch[1]="4";
        branch[2]="3";
        branch[3]="2";
        branch[4]="1";
        auth = FirebaseAuth.getInstance();
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        ArrayAdapter<String> adapter=new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,branch);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yr.setAdapter(adapter);

        gender=findViewById(R.id.gender);
        final String[] gd= new String[3];
        gd[0]="Select Gender";
        gd[1]="Male";
        gd[2]="Female";
        ArrayAdapter<String> adapter1=new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,gd);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gender.setAdapter(adapter1);

        final EditText name=findViewById(R.id.name);
        final EditText email=findViewById(R.id.email);
        final EditText reg=findViewById(R.id.reg);
        final EditText phone=findViewById(R.id.number);
        final EditText password=findViewById(R.id.password);
        login1=findViewById(R.id.login);
        Log.d(TAG, "before onclick");
        login1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d(TAG, " entered onClick");
                final String e = email.getText().toString().trim();
                final String p = password.getText().toString().trim();
                final String namestd = name.getText().toString().trim();
                final String regstd = reg.getText().toString().trim();
                final String phonestd = phone.getText().toString().trim();
                final String genderstd = gender.getSelectedItem().toString().trim();
                final String yrstd = yr.getSelectedItem().toString().trim();

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
                if (namestd.equals("")||regstd.equals("")||phonestd.equals("")) {
                    Toast.makeText(getApplicationContext(), "Enter all the details", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(yrstd.equals("Select Your Year of study")){
                    Toast.makeText(getApplicationContext(), "Select Your Year of study", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(genderstd.equals("Select Gender")){
                    Toast.makeText(getApplicationContext(), "Select Gender", Toast.LENGTH_SHORT).show();
                    return;
                }



                auth.createUserWithEmailAndPassword(e,p)
                        .addOnCompleteListener(SignupStudent.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                               // Toast.makeText(SignupStudent.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();

                                if (!task.isSuccessful()) {
                                    Toast.makeText(SignupStudent.this, "Authentication failed." + task.getException(),
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    Map<String, Object> user = new HashMap<>();

                                    user.put("name",namestd);
                                    user.put("email",e);
                                    user.put("reg",regstd);
                                    user.put("stdyear",yrstd);
                                    user.put("gender",genderstd);
                                    user.put("phone",phonestd);
                                    user.put("password",p);

                                    db.collection("student").document(auth.getCurrentUser().getEmail()).set(user);

                                    Map<String, Object> user1 = new HashMap<>();
                                    user1.put("role","student");
                                    db.collection("users").document(e).set(user1);
                                    startActivity(new Intent(SignupStudent.this, MainActivity2.class));
                                    finish();
                                }
                            }
                        });

            }
        });
    }
}
