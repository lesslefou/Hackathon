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

        //Connection à la bdd + service d'identification
        mAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference();
        progressBar = findViewById(R.id.progressBar);

        //Mise à l'écoute des 2 boutons
        findViewById(R.id.btn_back).setOnClickListener(this);
        findViewById(R.id.btn_validate).setOnClickListener(this);
    }

    private void registerUser() {
        // Converti les editText en String
        final String name = editTextName.getText().toString().trim();
        final String surname = editTextSurname.getText().toString().trim();
        final String confPassword = editTextConfPassword.getText().toString().trim();
        final String email = editTextEmail.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();
        final String key = editTextKey.getText().toString().trim();


        //Envoi une erreur si la String est nulle
        if (name.isEmpty() ){
            editTextName.setError(getString(R.string.notName));
            editTextName.requestFocus();
        }
        //Envoi une erreur si la String est nulle
        if (surname.isEmpty() ){
            editTextSurname.setError(getString(R.string.notSurname));
            editTextSurname.requestFocus();
        }
        //Envoi une erreur si la String est nulle
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

        //Vérifie que le mot de passe contient bien une lettre en majuscule, en minuscule et un chiffre
        for (int i = 0; i < password.length(); i++) {
            if (Character.isDigit(password.charAt(i))) {
                hasNumbers = true;
            } else if (Character.isLowerCase(password.charAt(i))) {
                hasLowerCase = true;
            } else if (Character.isUpperCase(password.charAt(i))) {
                hasUpperCase = true;
            }
        }
        //Vérifie en plus que le mot de passe possède au moins 8 charactères
        if (hasNumbers && hasLowerCase && hasUpperCase && (password.length() >= 8)) {
            validPassword = true;
        }

        //Envoi une erreur si le mot de passe ne possède pas toutes les conditions à respecter
        if (!validPassword) {
            editTextPassword.setError((getString(R.string.wrongPassword)));
            editTextPassword.requestFocus();
        }

        //Envoi une erreur si confPassword est nul
        if (confPassword.isEmpty() ){
            editTextConfPassword.setError(getString(R.string.notConfPassword));
            editTextConfPassword.requestFocus();
        }

        //Envoi une erreur si confPassword n'est pas égale à password
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

        //Envoi une erreur si la String est nulle
        if (key.isEmpty() ){
            editTextKey.setError(getString(R.string.notKey));
            editTextKey.requestFocus();
        }
        else {
            //Vérifie qu'il s'agit de la bonne clé de confirmation
            if (key.equals(confirmationKey)) {
                validKey = true;
            }
            else {
                editTextKey.setError(getString(R.string.notGoodKey));
                validKey = false;
            }
        }

        //Si toute les conditions sont respectés alors on enregistre l'utilisateur et on le redirige sur la page de connection
        if (!name.isEmpty() && !surname.isEmpty() && validConfPassword && !email.isEmpty() && validPassword && validKey) {

            //ProgressBar active tant que les données ne sont pas chargées
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
                    Toast.makeText(SignUp.this, R.string.needEmailVerification, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SignUp.this, LogIn.class));
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


    //Gestion des cliques sur les 2 boutons
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
