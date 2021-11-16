package com.example.votodroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Placeholder;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.votodroid.bd.BD;
import com.example.votodroid.databinding.ActivityCreateQuestionBinding;
import com.example.votodroid.databinding.ActivityMainBinding;
import com.example.votodroid.exceptions.MauvaiseQuestion;
import com.example.votodroid.modele.VDQuestion;
import com.example.votodroid.service.ServiceImplementation;

public class CreateQuestionActivity extends AppCompatActivity {

    private ServiceImplementation service;
    private BD maBD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCreateQuestionBinding binding = ActivityCreateQuestionBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        maBD =  Room.databaseBuilder(getApplicationContext(), BD.class, "BDQuestions")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();
        service = ServiceImplementation.getInstance(maBD);

        binding.actionAskQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                creerQuestion();
                Log.e("yup", "yup");

            Intent intent = new Intent(CreateQuestionActivity.this, MainActivity.class);
            startActivity(intent);
            }
        });

    }

    private void creerQuestion (){
        try{
            VDQuestion maQuestion = new VDQuestion();
            TextView textView = findViewById(R.id.text_question);
            maQuestion.texteQuestion = textView.getText().toString();
            service.creerQuestion(maQuestion);
        }catch (MauvaiseQuestion m){
            Log.e("CREERQUESTION", "Impossible de créer la question : " + m.getMessage());
            TextView textView
        }
    }
}