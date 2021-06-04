package lisa.duterte.hackathon;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Historique extends AppCompatActivity {

    DatabaseReference databaseReference;
    ListView listView;
    ArrayList<String> arrayList = new ArrayList<>();;
    ArrayAdapter<String> arrayAdapter;
    ZoneChaleur zoneChaleur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historique);

        ImageView back = findViewById(R.id.backBtn);
        back.setOnClickListener(v -> finish());

        listView = findViewById(R.id.listView);
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,arrayList);
        listView.setAdapter(arrayAdapter);
        zoneChaleur = new ZoneChaleur();

        databaseReference = FirebaseDatabase.getInstance().getReference("Historique");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    zoneChaleur = child.getValue(ZoneChaleur.class);
                    arrayList.add(zoneChaleur.getDateHeure() +  "  :  " + zoneChaleur.getPosition() + "  temp : " + zoneChaleur.getTemperature());
                }
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}