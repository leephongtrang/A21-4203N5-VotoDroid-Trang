package com.example.votodroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.votodroid.databinding.ActivityVoteBinding;

public class VoteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityVoteBinding binding = ActivityVoteBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        String question = getIntent().getStringExtra("questionPose");
        TextView textView = findViewById(R.id.text_questionPose);
        textView.setText(question);

        binding.actionVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent yo = new Intent(VoteActivity.this, MainActivity.class);
                startActivity(yo);

            }
        });
}
}