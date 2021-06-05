package lisa.duterte.hackathon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;


public class Setting extends AppCompatActivity implements View.OnClickListener {

    DatabaseReference mReference;
    String userId;
    TextView nameT, surnameT, emailT, passwordT;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        nameT = findViewById(R.id.edit_name);
        surnameT = findViewById(R.id.edit_surname);
        emailT = findViewById(R.id.edit_email);
        passwordT = findViewById(R.id.edit_password);

        // Mise à l'écoute des 2 boutons + TextView
        findViewById(R.id.btn_back_setting).setOnClickListener(this);
        findViewById(R.id.btn_unsubscribe).setOnClickListener(this);
        findViewById(R.id.changePassword).setOnClickListener(this);

        //Affiche les données de l'utilisateur récupérer via la bdd à l'écran
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            userId = firebaseUser.getUid();
            mReference = FirebaseDatabase.getInstance().getReference("User").child(userId).child("member");
            mReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String name = Objects.requireNonNull(dataSnapshot.child("name").getValue()).toString();
                    String surname = Objects.requireNonNull(dataSnapshot.child("surname").getValue()).toString();
                    String email = Objects.requireNonNull(dataSnapshot.child("email").getValue()).toString();
                    Log.v("name= ", name);
                    nameT.setText(name);
                    surnameT.setText(surname);
                    emailT.setText(email);
                    passwordT.setText("*******");
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {}
            });
       }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    //Message de confirmation à la désinscription à l'application
    protected void showInformationSavedDialog() {
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(this, R.style.MyDialogTheme);
        builder.setMessage(R.string.dialogue_message_unsubscribe);
        builder.setCancelable(false);
        builder.setNegativeButton(R.string.no_answer, (dialog, which) -> dialog.cancel());
        builder.setPositiveButton(R.string.yes_answer, (dialog, which) -> {
            deleteUSer();
            dialog.cancel();
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    //Supprime l'utilisateur et ses informations de la bdd + renvoie sur la page d'accueil de l'application
    protected void deleteUSer() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseUser.delete().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                deleteUserInformation();
                Toast.makeText(Setting.this, R.string.account_deleted, Toast.LENGTH_SHORT).show();
                Intent i = new Intent(Setting.this, FirstPage.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            } else {
                Toast.makeText(Setting.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    protected void deleteUserInformation() {
        DatabaseReference databaseReferenceUser = FirebaseDatabase.getInstance().getReference("User").child(userId);
        databaseReferenceUser.removeValue();
    }


    //Permet de gérer le clique sur le menu
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                logout();
                return true;
            case R.id.setting:
                Intent i = new Intent(Setting.this, Setting.class);
                startActivity(i);
                return true;
            case R.id.aboutUs:
                Intent i1 = new Intent(Setting.this, AboutUs.class);
                startActivity(i1);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Déconnection de l'utilisateur
    public void logout() {
        FirebaseAuth.getInstance().signOut();
        Intent i = new Intent(getApplicationContext(), FirstPage.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }

    //Gestion des cliques sur les 2 boutons + TextView
    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back_setting:
                finish();
                break;

            case R.id.btn_unsubscribe:
                showInformationSavedDialog();
                break;

            case R.id.changePassword:
                Intent intent = new Intent(this, ResetPassword.class);
                String message = getString(R.string.changePassword);
                intent.putExtra("choix",message);
                startActivity(intent);
        }
    }
}
