package com.blackwizard.datesome;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Main2Activity extends AppCompatActivity {

    private EditText Email;
    private EditText Username;
    private EditText Password;
    private Button Register;
    private FirebaseAuth auth;
   private FirebaseAuth.AuthStateListener firebaseAuthStateListener; //to also be removed
    private ProgressBar progressBar;
    private RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        auth = FirebaseAuth.getInstance();

        Email = findViewById(R.id.email);
        Username = findViewById(R.id.name);
        Password = findViewById(R.id.password);
        Register = findViewById(R.id.btn_register);
        progressBar = findViewById(R.id.progressBar);
        radioGroup = findViewById(R.id.radioGroup);

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //code to handle radioGroup
                int selectId = radioGroup.getCheckedRadioButtonId();
                final RadioButton radioButton = findViewById(selectId);
                if (radioButton.getText() == null){
                    return;
                }

                final String email = Email.getText().toString().trim();
                final String password = Password.getText().toString().trim();
                final String name = Username.getText().toString();

                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null){
                    Intent intent = new Intent(Main2Activity.this, Main3Activity.class);
                    startActivity(intent);
                    finish();
                    return;
                }

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                //create user
                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(Main2Activity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Toast.makeText(Main2Activity.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {
                                    Toast.makeText(Main2Activity.this, "Sign up error" + task.getException(),
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    String userId = auth.getCurrentUser().getUid();
                                    DatabaseReference currentUserDb = FirebaseDatabase.getInstance().getReference().child("Users").child(radioButton.getText().toString()).child(userId).child("name");
                                    currentUserDb.setValue(name);
                                }
                            }
                        });

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }

}


