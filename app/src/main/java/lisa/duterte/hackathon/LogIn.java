package lisa.duterte.hackathon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LogIn extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private EditText editTextEmail, editTextPassword;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);


        editTextEmail = findViewById(R.id.edit_email);
        editTextPassword = findViewById(R.id.edit_password);
        progressBar = findViewById(R.id.progressBar);

        mAuth = FirebaseAuth.getInstance();
        //Si l'utilisateur ne s'est pas déconnecté, il ne verra pas cette page et sera redirigé directement à MAinActivity
        if (mAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }

        //Mise à l'écoute des boutons et du texteView
        findViewById(R.id.btn_back).setOnClickListener(this);
        findViewById(R.id.btn_log).setOnClickListener(this);
        findViewById(R.id.forgotPassword).setOnClickListener(this);
    }


    private void userLogin() {
        // Converti les editText en String
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        //Envoi une erreur si la String est nulle
        if (email.isEmpty()) {
            editTextEmail.setError(getString(R.string.notEmail));
            editTextEmail.requestFocus();
            return;
        }

        //Envoi une erreur si la String est nulle
        if (password.isEmpty()) {
            editTextPassword.setError(getString(R.string.notPassword));
            editTextPassword.requestFocus();
            return;
        }

        //ProgressBar active tant que les données ne sont pas chargées
        progressBar.setVisibility(View.VISIBLE);

        //Affichage d'un toast Erreur si toutes les infos ne sont pas remplies ou si elles sont fausses
        //Redirection vers la MainActivity si ok
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(LogIn.this, R.string.welcomeUser, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                }else {
                    Toast.makeText(LogIn.this, R.string.error_log_in, Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }

    //Gestion des cliques sur les 2 boutons + TextView
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                startActivity(new Intent(this, FirstPage.class));
                break;

            case R.id.btn_log:
                userLogin();
                break;

            case R.id.forgotPassword:
                Intent intent = new Intent(this, ResetPassword.class);
                String message = getString(R.string.resetPage);
                intent.putExtra("choix",message);
                startActivity(intent);
        }
    }

}