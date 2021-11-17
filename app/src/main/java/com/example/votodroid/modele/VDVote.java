package com.example.votodroid.modele;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class VDVote {
    //TODO Champs à définir
    @PrimaryKey(autoGenerate = true)
    public long idVote;


    public int vote;

    public String nomVoteur;

    public long QuestionID;
}
