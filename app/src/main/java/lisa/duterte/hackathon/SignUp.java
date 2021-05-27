package lisa.duterte.hackathon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Objects;

public class SignUp extends AppCompatActivity implements View.OnClickListener{
    String confirmationKey = "HACKATHON15";

    EditText editTextEmail, editTextPassword, editTextName, editTextSurname, editTextConfPassword, editTextKey;

    boolean validPassword = false, validConfPassword = false, validKey = false;
    boolean hasNumbers = false;
    boolean hasLowerCase = false;
    boolean hasUpperCase = false;


    FirebaseAuth mAuth;
    FirebaseFirestore fstore;
    FirebaseDatabase mDatabase;
    DatabaseReference mReference;
    User user;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        editTextName =findViewById(R.id.edit_name);
        editTextSurname =findViewById(R.id.edit_surname);
        editTextConfPassword =findViewById(R.id.edit_confPassword);
        editTextEmail =findViewById(R.id.edit_email);
        editTextPassword =findViewById(R.id.edit_password);
        editTextKey = findViewById(R.id.edit_key);

        mAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference();
        progressBar = findViewById(R.id.progressBar);

        findViewById(R.id.btn_back).setOnClickListener(this);
        findViewById(R.id.btn_validate).setOnClickListener(this);
    }

    private void registerUser() {
        final String name = editTextName.getText().toString().trim();
        final String surname = editTextSurname.getText().toString().trim();
        final String confPassword = editTextConfPassword.getText().toString().trim();
        final String email = editTextEmail.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();
        final String key = editTextKey.getText().toString().trim();

        if (name.isEmpty() ){
            editTextName.setError(getString(R.string.notName));
            editTextName.requestFocus();
        }
        if (surname.isEmpty() ){
            editTextSurname.setError(getString(R.string.notSurname));
            editTextSurname.requestFocus();
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError(getString(R.string.wrongEmail));
            editTextEmail.requestFocus();
        }

        validKey=false;
        validConfPassword = false;
        validPassword = false;
        hasNumbers = false;
        hasLowerCase = false;
        hasUpperCase = false;

        for (int i = 0; i < password.length(); i++) {
            if (Character.isDigit(password.charAt(i))) {
                hasNumbers = true;
            } else if (Character.isLowerCase(password.charAt(i))) {
                hasLowerCase = true;
            } else if (Character.isUpperCase(password.charAt(i))) {
                hasUpperCase = true;
            }
        }
        if (hasNumbers && hasLowerCase && hasUpperCase && (password.length() >= 8)) {
            validPassword = true;
        }

        if (!validPassword) {
            editTextPassword.setError((getString(R.string.wrongPassword)));
            editTextPassword.requestFocus();
        }

        if (confPassword.isEmpty() ){
            editTextConfPassword.setError(getString(R.string.notConfPassword));
            editTextConfPassword.requestFocus();
        }

        if (!confPassword.isEmpty()) {
            if (!confPassword.equals(password)) {
                editTextConfPassword.setError(getString(R.string.notGoodConfPassword));
                editTextConfPassword.requestFocus();
                validConfPassword = false;
            }
            else {
                validConfPassword = true;
            }
        }

        if (key.isEmpty() ){
            editTextKey.setError(getString(R.string.notKey));
            editTextKey.requestFocus();
        }
        else {
            if (key.equals(confirmationKey)) {
                validKey = true;
            }
            else {
                editTextKey.setError(getString(R.string.notGoodKey));
                validKey = false;
            }
        }

        if (!name.isEmpty() && !surname.isEmpty() && validConfPassword && !email.isEmpty() && validPassword && validKey) {

            progressBar.setVisibility(View.VISIBLE);

            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {

                    FirebaseUser fuser = mAuth.getCurrentUser();
                    Objects.requireNonNull(fuser).sendEmailVerification().addOnSuccessListener(aVoid -> Toast.makeText(SignUp.this,R.string.emailSent,Toast.LENGTH_SHORT).show()).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("Sign_Up", "onFailure: Email not sent" + e.getMessage());
                        }
                    });

                    String userId = mAuth.getCurrentUser().getUid();
                    user = new User();
                    mReference = FirebaseDatabase.getInstance().getReference("User").child(userId);
                    user.setName(name);
                    user.setSurname(surname);
                    user.setEmail(email);

                    mReference.child("member").setValue(user);
                    startActivity(new Intent(SignUp.this, MainActivity.class));
                } else {
                    Toast.makeText(SignUp.this, R.string.error + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            });
        }
        else {
            Toast.makeText(SignUp.this,R.string.notInfo,Toast.LENGTH_LONG).show();
        }
    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_validate:
                registerUser();
                break;

            case R.id.btn_back:
                finish();
                startActivity(new Intent(this, FirstPage.class));
                break;
        }
    }



}
