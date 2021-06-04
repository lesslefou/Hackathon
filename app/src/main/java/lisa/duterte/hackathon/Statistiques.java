package lisa.duterte.hackathon;


import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
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
    TextView okTxtV,progressDialog;
    EditText anneeDate ;
    Integer annee ;
    String dateString = "2021";
    ProgressBar progressBar;
    com.github.mikephil.charting.charts.BarChart barChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistiques);

        ImageView back = findViewById(R.id.backBtn);
        back.setOnClickListener(v -> finish());

        okTxtV = findViewById(R.id.ok);
        anneeDate = findViewById(R.id.anneeDate);

        barChart = findViewById(R.id.barchart);
        progressBar = findViewById(R.id.progressBar);
        progressDialog = findViewById(R.id.progressMessage);


        //check si la date est changé lorsque le bouton Ok est cliqué
        okTxtV.setOnClickListener(v -> {
            if (!anneeDate.getText().toString().trim().equals("")) {
                dateString = anneeDate.getText().toString().trim();
            }
            else {
                dateString = annee.toString();
            }
            calcul_design_graph();
        });

        calcul_design_graph();

    }

    private void calcul_design_graph () {
        System.out.println("text : " + dateString);
        annee = Integer.parseInt(dateString);
        System.out.println("annnee : " + annee);

        for (int i=0; i<tabAM.length; i++) {
            tabAM[i] = 0;
        }

        //connection à la bdd firebase pour obtenir les données d'historiques
        zoneChaleur = new ZoneChaleur();
        databaseReference = FirebaseDatabase.getInstance().getReference("Historique");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    zoneChaleur = child.getValue(ZoneChaleur.class);
                    //sauvegarde des différentes dateHeure de la bdd dans un arraylist
                    arrayList.add(zoneChaleur.getDateHeure());

                    //Affiche le graphique et cache le message de téléchagement
                    barChart.setVisibility((View.VISIBLE));
                    progressBar.setVisibility(View.INVISIBLE);
                    progressDialog.setVisibility(View.INVISIBLE);
                }
                //On comptabilise dans un tableau le nombre d'incendie par mois en fonction de l'année choisie par l'utilisateur
                //Par défaut l'année 2021
                for(int i = 0; i<12 ; i++) {
                    for (int j = 0 ; j<arrayList.size(); j++) {
                        if (annee == Integer.parseInt(arrayList.get(j).substring(0,4))) {
                            if (i == Integer.parseInt(arrayList.get(j).substring(5,7))) {
                                tabAM[i] += 1;
                            }
                        }
                    }
                }


                //Design du graph stat
                BarChart barChart = findViewById(R.id.barchart);
                ArrayList<BarEntry> entries = new ArrayList<>();
                for (int k=0; k<tabAM.length; k++) {
                    entries.add(new BarEntry(tabAM[k], k));
                }
                BarDataSet bardataset = new BarDataSet(entries, "Cells");
                bardataset.setColors(ColorTemplate.COLORFUL_COLORS);

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
                barChart.animateY(5000);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}