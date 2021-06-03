package lisa.duterte.hackathon;


import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;


public class Statistiques extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistiques);

        Button back = findViewById(R.id.backBtn);
        back.setOnClickListener(v -> finish());
        BarChart barChart = findViewById(R.id.barchart);

        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(8f, 0));
        entries.add(new BarEntry(2f, 1));
        entries.add(new BarEntry(5f, 2));
        entries.add(new BarEntry(20f, 3));
        entries.add(new BarEntry(15f, 4));
        entries.add(new BarEntry(19f, 5));
        entries.add(new BarEntry(8f, 6));
        entries.add(new BarEntry(2f, 7));
        entries.add(new BarEntry(5f, 8));
        entries.add(new BarEntry(20f, 9));
        entries.add(new BarEntry(15f, 10));
        entries.add(new BarEntry(19f, 11));

        BarDataSet bardataset = new BarDataSet(entries, "Cells");

        ArrayList<String> labels = new ArrayList<>();
        labels.add(String.valueOf(R.string.janvier));
        labels.add(String.valueOf(R.string.fevrier));
        labels.add(String.valueOf(R.string.mars));
        labels.add(String.valueOf(R.string.avril));
        labels.add(String.valueOf(R.string.mai));
        labels.add(String.valueOf(R.string.juin));
        labels.add(String.valueOf(R.string.juillet));
        labels.add(String.valueOf(R.string.aout));
        labels.add(String.valueOf(R.string.septembre));
        labels.add(String.valueOf(R.string.octobre));
        labels.add(String.valueOf(R.string.novembre));
        labels.add(String.valueOf(R.string.decembre));

        BarData data = new BarData(labels, bardataset);
        barChart.setData(data); // set the data and list of labels into chart

        barChart.setDescription("Expenditure for the year 2018");  // set the description

        bardataset.setColors(ColorTemplate.COLORFUL_COLORS);

        barChart.animateY(5000);
    }
}