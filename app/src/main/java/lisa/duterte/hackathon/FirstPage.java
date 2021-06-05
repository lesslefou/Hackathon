package lisa.duterte.hackathon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

public class FirstPage extends AppCompatActivity implements View.OnClickListener {


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menufirst, menu);
        return true;
    }

    //Permet de gérer le clique sur le menu
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.aboutUs:
                Intent i1 = new Intent(FirstPage.this, AboutUs.class);
                startActivity(i1);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_page);

        //Mise à l'écoute des 2 boutons
        View signUp = findViewById(R.id.btn_sign);
        signUp.setOnClickListener(this);
        View logIn = findViewById(R.id.btn_log);
        logIn.setOnClickListener(this);
    }


    //Gestion des cliques sur les 2 boutons
    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sign:
                Intent sign = new Intent(FirstPage.this, SignUp.class);
                startActivity(sign);
                break;
            case R.id.btn_log:
                Intent log = new Intent(FirstPage.this, LogIn.class);
                startActivity(log);
                break;
        }
    }
}
