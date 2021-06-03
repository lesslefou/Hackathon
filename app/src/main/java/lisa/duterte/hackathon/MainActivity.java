package lisa.duterte.hackathon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener  {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.maprelative).setOnClickListener(this);
        findViewById(R.id.historiqueRelative).setOnClickListener(this);
        findViewById(R.id.statrelative).setOnClickListener(this);


        AsyncJsonData task = new AsyncJsonData(MainActivity.this);
        task.execute("cse-in/forestDrone/droneData/environement");


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                logout();
                return true;
            case R.id.setting:
                Intent i = new Intent(MainActivity.this, Setting.class);
                startActivity(i);
                return true;
            case R.id.aboutUs:
                Intent i1 = new Intent(MainActivity.this, AboutUs.class);
                startActivity(i1);
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void logout() {
        FirebaseAuth.getInstance().signOut();
        Intent i = new Intent(getApplicationContext(),FirstPage.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.maprelative:
                startActivity(new Intent(this, Map.class));
                break;

            case R.id.historiqueRelative:
                startActivity(new Intent(this, Historique.class));
                break;

            case R.id.statrelative:
                startActivity(new Intent(this, Statistiques.class));
        }
    }
}