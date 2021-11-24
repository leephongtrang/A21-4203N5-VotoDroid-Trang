package com.example.votodroid;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.votodroid.bd.BD;
import com.example.votodroid.databinding.ActivityMainBinding;
import com.example.votodroid.exceptions.MauvaiseQuestion;
import com.example.votodroid.modele.VDQuestion;
import com.example.votodroid.service.ServiceImplementation;

public class MainActivity extends AppCompatActivity {

    QuestionAdapter adapter;
    private ServiceImplementation service;
    private BD maBD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        setTitle("VoteDroid");

        maBD =  Room.databaseBuilder(getApplicationContext(), BD.class, "BDQuestions")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();
        service = ServiceImplementation.getInstance(maBD);
        //creerQuestion();

        this.initRecycler();
        afficherQuestion();
        this.test();

        binding.actionAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CreateQuestionActivity.class);
                startActivity(intent);
            }
        });
    }

    private void afficherQuestion() {
        if (!service.toutesLesQuestions().isEmpty()){
            adapter.list = service.toutesLesQuestions();
        }else {
            adapter.list.clear();
        }
        adapter.notifyDataSetChanged();
    }

    private void creerQuestion (){
        try{
            VDQuestion maQuestion = new VDQuestion();
            maQuestion.texteQuestion = "As-tu hâte au nouveau film The Matrix Resurrections?";
            service.creerQuestion(maQuestion);
        }catch (MauvaiseQuestion m){
            Log.e("CREERQUESTION", "Impossible de créer la question : " + m.getMessage());
        }
    }

    private void test(){
        for (int i = 0; i < 23; i++){
            VDQuestion q = new VDQuestion();
            q.texteQuestion = "testQ" + i;
            try {
                service.creerQuestion(q);
                adapter.list.add(q);
            } catch (MauvaiseQuestion m){
                Log.e("CREERQUESTION", "Impossible de créer la question : " + m.getMessage());
            }
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.troispoints, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_deleteAllEntries:
                service.supprimerToutVote();
                return true;
            case R.id.action_deleteAllQuestions:
                service.supprimerToutQuestion();
                afficherQuestion();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initRecycler(){
        RecyclerView recyclerView = findViewById(R.id.list_question);
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        adapter = new QuestionAdapter();
        recyclerView.setAdapter(adapter);
    }
}