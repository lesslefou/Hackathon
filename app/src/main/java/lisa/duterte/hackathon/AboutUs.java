package lisa.duterte.hackathon;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;


public class AboutUs extends FragmentActivity implements OnMapReadyCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        Button back = findViewById(R.id.backBtn);
        back.setOnClickListener(v -> finish());

        //Obtention  du SupportMapFragment avec notification quand la map est prête à être utilisée
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        //applique la localisation de l'ISEN
        LatLng isen = new LatLng(43.12064347637604, 5.939666736157341);
        googleMap.addMarker(new MarkerOptions()
                .position(isen)
                .title("ISEN Toulon"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(isen, 12));
    }

    //Permet de gérer les cliques sur le menu
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                logout();
                return true;
            case R.id.setting:
                Intent i = new Intent(AboutUs.this, Setting.class);
                startActivity(i);
                return true;
            case R.id.aboutUs:
                Intent i1 = new Intent(AboutUs.this, AboutUs.class);
                startActivity(i1);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //Déconnection de l'utilisateur
    public void logout() {
        FirebaseAuth.getInstance().signOut();
        Intent i = new Intent(getApplicationContext(), FirstPage.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }
}