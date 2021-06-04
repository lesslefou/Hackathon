package lisa.duterte.hackathon;


import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class Statistiques extends AppCompatActivity {

    DatabaseReference databaseReference;
    ArrayList<String> arrayList = new ArrayList<>();;
    ZoneChaleur zoneChaleur;
    Integer[] tabAM = new Integer[12];
    EditText anneeDate ;
    Integer annee = 2021;
    String dateString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistiques);

        ImageView back = findViewById(R.id.backBtn);
        back.setOnClickListener(v -> finish());

        anneeDate = findViewById(R.id.anneeDate);
        if (anneeDate.getText().toString().trim() != null) {
            //dateString = anneeDate.getText().toString().trim();
            dateString = annee.toString();
            System.out.println("text if: " + dateString);
        }
        else {
            dateString = annee.toString();
            System.out.println("text else: " + dateString);
        }
        System.out.println("text : " + dateString);
        annee = Integer.parseInt(dateString);
        System.out.println("annnee : " + annee);

        for (int i=0; i<tabAM.length; i++) {
            tabAM[i] = 0;
        }

        zoneChaleur = new ZoneChaleur();

            databaseReference = FirebaseDatabase.getInstance().getReference("Historique");
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        zoneChaleur = child.getValue(ZoneChaleur.class);

                        arrayList.add(zoneChaleur.getDateHeure());

                    }
                    System.out.println(arrayList);
                    for(int i = 0; i<12 ; i++) {
                        for (int j = 0 ; j<arrayList.size(); j++) {
                            if (annee == Integer.parseInt(arrayList.get(j).substring(0,4))) {
                                if (i == Integer.parseInt(arrayList.get(j).substring(5,7))) {
                                    tabAM[i] += 1;
                                }
                            }
                        }
                    }


                    BarChart barChart = findViewById(R.id.barchart);

                    for (int i=0; i<tabAM.length; i++) {
                        System.out.println(tabAM[i]);
                    }
                    ArrayList<BarEntry> entries = new ArrayList<>();
                    for (int k=0; k<tabAM.length; k++) {
                        entries.add(new BarEntry(tabAM[k], k));
                    }
                    BarDataSet bardataset = new BarDataSet(entries, "Cells");

                    ArrayList<String> labels = new ArrayList<>();
                    labels.add("01");
                    labels.add("02");
                    labels.add("03");
                    labels.add("04");
                    labels.add("05");
                    labels.add("06");
                    labels.add("07");
                    labels.add("08");
                    labels.add("09");
                    labels.add("10");
                    labels.add("11");
                    labels.add("12");

                    BarData data = new BarData(labels, bardataset);
                    barChart.setData(data); // set the data and list of labels into chart

                    barChart.setDescription("Expenditure for the year " + annee);  // set the description

                    bardataset.setColors(ColorTemplate.COLORFUL_COLORS);

                    barChart.animateY(5000);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
    }


}