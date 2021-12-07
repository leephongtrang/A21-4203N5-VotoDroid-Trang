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
import com.example.votodroid.exceptions.MauvaisVote;
import com.example.votodroid.exceptions.MauvaiseQuestion;
import com.example.votodroid.modele.VDQuestion;
import com.example.votodroid.modele.VDVote;
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

    private void test(){
        Long id = 20L;
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
        try {
            VDVote vote1 = new VDVote();
            vote1.vote = 1;
            vote1.nomVoteur = "sdjffiodjv";
            vote1.QuestionID = id;
            service.creerVote(vote1);
            for (int i = 0; i < 3; i++){
                VDVote vote = new VDVote();
                vote.nomVoteur = "TestV" + i;
                vote.QuestionID = id;
                vote.vote = 5;
                service.creerVote(vote);
            }
        } catch (MauvaisVote m) {
        Log.e("CREERVOTE", "Impossible de créer le vote : " + m.getMessage());
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