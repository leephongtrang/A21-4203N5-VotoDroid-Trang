package com.example.votodroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Placeholder;
import androidx.lifecycle.ViewModelStoreOwner;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.votodroid.databinding.ActivityCreateQuestionBinding;
import com.example.votodroid.databinding.ActivityMainBinding;

public class CreateQuestionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCreateQuestionBinding binding = ActivityCreateQuestionBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.actionAskQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent cuicui = new Intent(CreateQuestionActivity.this, MainActivity.class);
            startActivity(cuicui);
            }
        });


    }
}