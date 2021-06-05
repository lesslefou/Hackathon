package lisa.duterte.hackathon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPassword extends AppCompatActivity {
    String data = "";
    EditText emailText;
    Button reset;
    ProgressBar progressBar;
    FirebaseAuth mAuth;
    TextView maintitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        maintitle = findViewById(R.id.title);

        //Récupération de la donnée envoyée via la page Setting ou LogIn pour permettre de modifier le type de phrase à afficer
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            data= extras.getString("choix","defaultValue");
        }
        maintitle.setText(data);

        progressBar = findViewById(R.id.progressBar);
        reset = findViewById(R.id.resetBtn);
        emailText = findViewById(R.id.emailField);
        mAuth = FirebaseAuth.getInstance();

        //Envoie d'un mail pour réinitialiser ou modifier le mot de passe
        reset.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            mAuth.sendPasswordResetEmail(emailText.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    progressBar.setVisibility(View.GONE);
                    if (task.isSuccessful()){
                        Toast.makeText(ResetPassword.this,R.string.resetbyEmail,Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(ResetPassword.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                    }
                }
            });
        });
    }
}