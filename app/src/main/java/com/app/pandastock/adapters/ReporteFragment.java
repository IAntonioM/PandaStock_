package com.app.pandastock.adapters;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.charts.Cartesian;
import com.anychart.charts.Pie;
import com.anychart.core.cartesian.series.Bar;
import com.anychart.core.cartesian.series.Line;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.data.Mapping;
import com.anychart.data.Set;
import com.anychart.enums.Anchor;
import com.anychart.enums.LabelsOverlapMode;
import com.anychart.enums.ScaleStackMode;
import com.anychart.enums.TooltipPositionMode;
import com.app.pandastock.R;
import com.app.pandastock.firebase.ReporteFirebase;

import java.util.ArrayList;
import java.util.List;

public class ReporteFragment extends Fragment {
    private static final String ARG_POSITION = "position";
    private AnyChartView anyChartView;
    private ReporteFirebase reporteFirebase;

    public static ReporteFragment newInstance(int position) {
        ReporteFragment fragment = new ReporteFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_reporte, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        anyChartView = view.findViewById(R.id.any_chart_view);
        int position = getArguments().getInt(ARG_POSITION);
        reporteFirebase = new ReporteFirebase();
        loadChartData(position);
    }

    private void loadChartData(int position) {
        List<DataEntry> dataEntries = new ArrayList<>();

        switch (position) {
            case 0:
                // Datos falsos para Productos Vendidos
                dataEntries.add(new ValueDataEntry("Producto A", 100));
                dataEntries.add(new ValueDataEntry("Producto B", 120));
                dataEntries.add(new ValueDataEntry("Producto C", 80));
                setupBarChart(dataEntries, "Productos Vendidos");
                break;
            case 1:
                // Datos falsos para Tipo Producto Vendido
                dataEntries.add(new ValueDataEntry("Laptop", 50));
                dataEntries.add(new ValueDataEntry("Mouse", 75));
                dataEntries.add(new ValueDataEntry("Monitor", 30));
                setupPieChart(dataEntries, "Tipo Producto Vendido");
                break;
            case 2:
                // Datos falsos para Ventas x Tiempo
                dataEntries.add(new ValueDataEntry("01/01", 200));
                dataEntries.add(new ValueDataEntry("02/01", 150));
                dataEntries.add(new ValueDataEntry("03/01", 180));
                setupLineChart(dataEntries, "Ventas x Tiempo");
                break;
            case 3:
                // Datos falsos para Movimiento de Inventario
                dataEntries.add(new CustomDataEntry("Producto A", 90, 60));
                dataEntries.add(new CustomDataEntry("Producto B", 70, 50));
                dataEntries.add(new CustomDataEntry("Producto C", 80, 40));
                setupInventoryMovementChart(dataEntries, "Movimiento de Inventario");
                break;
        }
    }

    private void setupBarChart(List<DataEntry> dataEntries, String title) {
        Cartesian barChart = AnyChart.bar();
        barChart.data(dataEntries);
        barChart.title(title);
        anyChartView.setChart(barChart);
    }

    private void setupPieChart(List<DataEntry> dataEntries, String title) {
        Pie pieChart = AnyChart.pie();
        pieChart.data(dataEntries);
        pieChart.title(title);
        anyChartView.setChart(pieChart);
    }

    private void setupLineChart(List<DataEntry> dataEntries, String title) {
        Cartesian cartesian = AnyChart.line();

        cartesian.animation(true);
        cartesian.padding(10d, 20d, 5d, 20d);
        cartesian.crosshair().enabled(true);
        cartesian.crosshair().yLabel(true);

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
        cartesian.title(title);

        cartesian.yAxis(0).title("Sales");
        cartesian.xAxis(0).labels().padding(5d, 5d, 5d, 5d);

        Line line = cartesian.line(dataEntries);
        line.name("Sales");

        anyChartView.setChart(cartesian);
    }
    private void setupInventoryMovementChart(List<DataEntry> dataEntries, String title) {
        Cartesian barChart = AnyChart.bar();
        barChart.animation(true);
        barChart.padding(10d, 20d, 5d, 20d);

        barChart.yScale().stackMode(ScaleStackMode.VALUE);
        barChart.yAxis(0).title("Cantidad");

        barChart.xAxis(0).overlapMode(LabelsOverlapMode.ALLOW_OVERLAP);
        barChart.title(title);

        Set set = Set.instantiate();
        set.data(dataEntries);
        Mapping series1Mapping = set.mapAs("{ x: 'x', value: 'value' }");
        Mapping series2Mapping = set.mapAs("{ x: 'x', value: 'value2' }");

        Bar series1 = barChart.bar(series1Mapping);
        series1.name("Entradas").color("green");
        series1.tooltip().position("right").anchor(Anchor.LEFT_CENTER);

        Bar series2 = barChart.bar(series2Mapping);
        series2.name("Salidas").color("red");
        series2.tooltip().position("left").anchor(Anchor.RIGHT_CENTER);

        barChart.legend().enabled(true);
        barChart.legend().fontSize(13d);
        barChart.legend().padding(0d, 0d, 20d, 0d);
        anyChartView.setChart(barChart);
    }
    private class CustomDataEntry extends ValueDataEntry {
        CustomDataEntry(String x, Number value, Number value2) {
            super(x, value);
            setValue("value2", value2);
        }
    }
}
