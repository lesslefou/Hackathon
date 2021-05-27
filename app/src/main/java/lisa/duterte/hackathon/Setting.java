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
    Button back, unsubscribe;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        nameT = findViewById(R.id.edit_name);
        surnameT = findViewById(R.id.edit_surname);
        emailT = findViewById(R.id.edit_email);
        passwordT = findViewById(R.id.edit_password);


        findViewById(R.id.btn_back_setting).setOnClickListener(this);
        findViewById(R.id.btn_unsubscribe).setOnClickListener(this);
        findViewById(R.id.changePassword).setOnClickListener(this);

        //Set all the information of the user from the database on the screen
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            userId = firebaseUser.getUid();
            Log.v("userId = ",userId);

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
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            /*//Go to the previous activity and close this page
            back = findViewById(R.id.btn_back);
            back.setOnClickListener(v -> finish());

            //Display dialog information
            unsubscribe = findViewById(R.id.btn_unsubscribe);
            unsubscribe.setOnClickListener(v -> showInformationSavedDialog());*/


       }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

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

    //Delete the user and its information from the database
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


    public void logout() {
        FirebaseAuth.getInstance().signOut();
        Intent i = new Intent(getApplicationContext(), FirstPage.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }
    //Go on the good page
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
                startActivity(new Intent(this, ResetPassword.class));
        }
    }
}
