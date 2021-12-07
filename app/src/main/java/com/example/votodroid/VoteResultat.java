package com.example.votodroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.votodroid.bd.BD;
import com.example.votodroid.modele.VDVote;
import com.example.votodroid.service.ServiceImplementation;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.DefaultAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class VoteResultat extends AppCompatActivity {
    BarChart chart;
    TextView moyenne;
    TextView ecartType;

    private ServiceImplementation service;
    private BD maBD;
    private Long Id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote_resultat);
        setTitle("Résultats");

        chart = findViewById(R.id.chart);
        moyenne = findViewById(R.id.moyenne);
        ecartType = findViewById(R.id.ecartType);

        String question = getIntent().getStringExtra("questionPose");
        TextView textView = findViewById(R.id.text_questionResultat);
        textView.setText(question);

        /* Settings for the graph - Change me if you want*/
        chart.setMaxVisibleValueCount(6);
        chart.setPinchZoom(false);
        chart.setDrawGridBackground(false);

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1); // only intervals of 1 day
        xAxis.setLabelCount(7);
        xAxis.setValueFormatter(new DefaultAxisValueFormatter(0));

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setLabelCount(8, false);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setGranularity(1);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setValueFormatter(new DefaultAxisValueFormatter(0));
        chart.getDescription().setEnabled(false);
        chart.getAxisRight().setEnabled(false);

        maBD =  Room.databaseBuilder(getApplicationContext(), BD.class, "BDQuestions")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();
        service = ServiceImplementation.getInstance(maBD);
        Id = this.getIntent().getLongExtra("questionID", 0);

        //region remplissageGraph
        int star0 = 0;
        int star1 = 0;
        int star2 = 0;
        int star3 = 0;
        int star4 = 0;
        int star5 = 0;

        for (VDVote v: service.toutLesVotes()){
            if(v.vote == 0) {star0++;}
            if(v.vote == 1) {star1++;}
            if(v.vote == 2) {star2++;}
            if(v.vote == 3) {star3++;}
            if(v.vote == 4) {star4++;}
            if(v.vote == 5) {star5++;}
        }

        int finalStar0 = star0;
        int finalStar1 = star1;
        int finalStar2 = star2;
        int finalStar3 = star3;
        int finalStar4 = star4;
        int finalStar5 = star5;
        //endregion

        /* Data and function call to bind the data to the graph */
        Map<Integer, Integer> dataGraph = new HashMap<Integer, Integer>() {{
            put(0, finalStar0);
            put(1, finalStar1);
            put(2, finalStar2);
            put(3, finalStar3);
            put(4, finalStar4);
            put(5, finalStar5);
        }};

        setData(dataGraph);

        moyenne.setText(service.moyenneVotes(Id));
        ecartType.setText(service.ecartTypeVotes(Id));
    }

    private void setData(Map<Integer, Integer> datas) {

        ArrayList<BarEntry> values = new ArrayList<>();

        /* Every bar entry is a bar in the graphic */
        for (Map.Entry<Integer, Integer> key : datas.entrySet()){
            values.add(new BarEntry(key.getKey() , key.getValue()));
        }

        BarDataSet set1;
        if (chart.getData() != null &&
                chart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) chart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(values, "Notes");

            set1.setDrawIcons(false);

            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);
            data.setBarWidth(.9f);
            chart.setData(data);
        }
    }
}