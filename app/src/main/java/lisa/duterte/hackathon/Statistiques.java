package lisa.duterte.hackathon;


import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

/*import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Column;
import com.anychart.enums.Anchor;
import com.anychart.enums.HoverMode;
import com.anychart.enums.Position;
import com.anychart.enums.TooltipPositionMode;

import java.util.ArrayList;
import java.util.List;*/

public class Statistiques extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistiques);

        /*AnyChartView anyChartView = findViewById(R.id.any_chart_view);
        anyChartView.setProgressBar(findViewById(R.id.progress_bar));

        Cartesian cartesian = AnyChart.column();

        List<DataEntry> data = new ArrayList<>();
        data.add(new ValueDataEntry("Janvier", 2));
        data.add(new ValueDataEntry("Fevrier", 3));
        data.add(new ValueDataEntry("Mars", 4));
        data.add(new ValueDataEntry("Avril", 5));
        data.add(new ValueDataEntry("Mai", 6));
        data.add(new ValueDataEntry("Juin", 7));
        data.add(new ValueDataEntry("Juillet", 8));
        data.add(new ValueDataEntry("Aout", 9));
        data.add(new ValueDataEntry("Septembre", 8));
        data.add(new ValueDataEntry("Octobre", 5));
        data.add(new ValueDataEntry("Novembre", 4));
        data.add(new ValueDataEntry("Decemebre", 1));

        Column column = cartesian.column(data);

        column.tooltip()
                .titleFormat("{%X}")
                .position(Position.CENTER_BOTTOM)
                .anchor(Anchor.CENTER_BOTTOM)
                .offsetX(0d)
                .offsetY(5d)
                .format("${%Value}{groupsSeparator: }");

        cartesian.animation(true);
        cartesian.title("Nombre de sources de chaleurs détectées / mois");

        cartesian.yScale().minimum(0d);

        cartesian.yAxis(0).labels().format("${%Value}{groupsSeparator: }");

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
        cartesian.interactivity().hoverMode(HoverMode.BY_X);

        cartesian.xAxis(0).title("Mois");
        cartesian.yAxis(0).title("Nombre");

        anyChartView.setChart(cartesian);*/
    }
}