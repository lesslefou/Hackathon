package lisa.duterte.hackathon;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class Map extends AppCompatActivity implements OnMapReadyCallback {

    Double longitude=0.0, latitude=0.0;
    DatabaseReference mReference;
    TextView coordDrone;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

   @Override
    protected void onCreate(Bundle savedInstanceState) {
       longitude = 0.0;
       latitude = 0.0;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        coordDrone = findViewById(R.id.coordDrone);

        ImageView back = findViewById(R.id.backBtn);
        back.setOnClickListener(v -> finish());

       //Obtention  du SupportMapFragment avec notification quand la map est prête à être utilisée
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mReference = FirebaseDatabase.getInstance().getReference("CoordonneesDrone");
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Double long2 = 0.0, lat2 = 0.0;
                //on récupère la position du drone via la bdd
                try {
                    longitude = (Double) dataSnapshot.child("longitude").getValue();
                    latitude = (Double) dataSnapshot.child("latitude").getValue();
                }
                catch (ClassCastException e) {
                    Log.v("erreur de cast long to double","");
                }

                //Permet de ne pas avoir de doublons de curseur drone
                if( lat2 != latitude || long2 != longitude)
                {
                    googleMap.clear();
                }

               String coordonnee = "(" + latitude + "," + longitude + ")";
                coordDrone.setText(coordonnee);

                //Applique les coordonnées du drone
                LatLng isen = new LatLng(latitude, longitude);
                googleMap.addMarker(new MarkerOptions()
                        .position(isen)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.drone_logo))
                        .title(String.valueOf(R.string.position_drone)));

                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(isen, 14));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
                Intent i = new Intent(this, Setting.class);
                startActivity(i);
                return true;
            case R.id.aboutUs:
                Intent i1 = new Intent(this, AboutUs.class);
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