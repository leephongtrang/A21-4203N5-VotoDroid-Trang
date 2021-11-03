package com.example.votodroid;

import android.content.Intent;
import android.renderscript.ScriptGroup;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import static androidx.core.content.ContextCompat.startActivity;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.MyViewHolder> {
    public List<Question> list;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView tvQuestion;
        public ImageButton btnResult;
        public LinearLayout ll;
        public MyViewHolder(LinearLayout v) {
            super(v);
            this.ll = v;
            tvQuestion = v.findViewById(R.id.tvQuestion);
            btnResult = v.findViewById(R.id.btn_result);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public QuestionAdapter() {
        list = new ArrayList<>();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.question_item, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Question questionCourante = list.get(position);
        holder.tvQuestion.setText(questionCourante.question);
        holder.ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("GNA", "clic sur " + questionCourante.question);
                Intent intent = new Intent (v.getContext(), VoteActivity.class);
                intent.putExtra("questionPose", questionCourante.question);
                v.getContext().startActivity(intent);

            }
        });

        holder.btnResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), VoteResultat.class);
                intent.putExtra("questionPose", questionCourante.question);
                v.getContext().startActivity(intent);
            }
        });
    }



    // renvoie la taille de la liste
    @Override
    public int getItemCount() {
        return list.size();
    }

    //https://medium.com/android-news/click-listener-for-recyclerview-adapter-2d17a6f6f6c9
    //Pour bouton item recycleview
}