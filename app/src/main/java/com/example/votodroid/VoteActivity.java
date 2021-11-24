package com.example.votodroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.votodroid.bd.BD;
import com.example.votodroid.databinding.ActivityVoteBinding;
import com.example.votodroid.exceptions.MauvaisVote;
import com.example.votodroid.exceptions.MauvaiseQuestion;
import com.example.votodroid.modele.VDQuestion;
import com.example.votodroid.modele.VDVote;
import com.example.votodroid.service.ServiceImplementation;

import java.security.Provider;

public class VoteActivity extends AppCompatActivity {

    private ServiceImplementation service;
    private BD maBD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityVoteBinding binding = ActivityVoteBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        maBD =  Room.databaseBuilder(getApplicationContext(), BD.class, "BDQuestions")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();
        service = ServiceImplementation.getInstance(maBD);

        String question = getIntent().getStringExtra("questionPose");
        TextView textView = findViewById(R.id.text_questionPose);
        textView.setText(question);

        binding.actionVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    creerVote();
                    Intent yo = new Intent(VoteActivity.this, MainActivity.class);
                    startActivity(yo);
                    Toast toast = Toast.makeText( getApplicationContext(),"Vote effectué", Toast.LENGTH_LONG);
                    toast.show();
                } catch (MauvaisVote mauvaisVote) {
                    TextView textView1 = findViewById(R.id.text_errorVote);
                    textView1.setText(mauvaisVote.getMessage());
                    mauvaisVote.printStackTrace();
                }
            }
        });
    }

    private void creerVote () throws MauvaisVote {
        TextView textView = findViewById(R.id.text_nomVoteur);
        RatingBar ratingBar = findViewById(R.id.vote_vote);
        /*try{
            VDQuestion maQuestion = new VDQuestion();
            maQuestion.texteQuestion = "As-tu hâte au nouveau film The Matrix Resurrections?";
            service.creerQuestion(maQuestion);
        }catch (MauvaiseQuestion m){
            Log.e("CREERQUESTION", "Impossible de créer la question : " + m.getMessage());
        }*/

        VDVote vote = new VDVote();
        vote.nomVoteur = textView.getText().toString();
        vote.vote = (int) (ratingBar.getRating()*2);
        vote.QuestionID = getIntent().getLongExtra("questionID", 0);
        service.creerVote(vote);
    }
}