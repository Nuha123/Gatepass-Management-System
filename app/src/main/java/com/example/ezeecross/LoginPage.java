package com.example.ezeecross;

import android.app.ActionBar;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginPage extends AppCompatActivity {
    Button login;
    int w = 0, r = 0;

    final FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        final FirebaseAuth auth;
        auth = FirebaseAuth.getInstance();

        RelativeLayout warden = findViewById(R.id.warden);
        final RelativeLayout parent = findViewById(R.id.parent);
        RelativeLayout student = findViewById(R.id.student);
        final EditText email = findViewById(R.id.email);
        final EditText password = findViewById(R.id.password);
        login = findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String e = email.getText().toString().trim();
                final String p = password.getText().toString().trim();
                if(e.equals(null)||e.equals("")||p.equals(null)||p.equals("")){
                    Toast.makeText(getApplicationContext(),"Please fill in all the details",Toast.LENGTH_LONG).show();
                }else {
                    auth.signInWithEmailAndPassword(e, p)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getApplicationContext(), "Login successful!", Toast.LENGTH_LONG).show();

                                        DocumentReference docRef = db.collection("users").document(e);
                                        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    DocumentSnapshot document = task.getResult();
                                                    if (document.get("role").toString().equals("warden")) {
                                                        Intent intent = new Intent(LoginPage.this, MainActivity.class);
                                                        startActivity(intent);
                                                        finish();
                                                    } else if (document.get("role").equals("parent")) {
                                                        Intent intent = new Intent(LoginPage.this, MainActivity1.class);
                                                        startActivity(intent);
                                                        finish();
                                                    } else {
                                                        Intent intent = new Intent(LoginPage.this, MainActivity2.class);
                                                        startActivity(intent);
                                                        finish();
                                                    }
                                                }
                                            }
                                        });
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Login failed! Wrong Mail ID or password", Toast.LENGTH_LONG).show();

                                    }
                                }
                            });
                }
            }
        });
        warden.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignupWarden.class);
                startActivity(intent);
                finish();
            }
        });

        parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignupParent.class);
                startActivity(intent);
                finish();
            }
        });

        student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignupStudent.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
