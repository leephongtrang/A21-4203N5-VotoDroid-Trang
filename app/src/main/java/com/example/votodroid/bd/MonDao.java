package com.example.votodroid.bd;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.votodroid.modele.VDQuestion;

import java.util.List;

@Dao
public interface MonDao {
    @Insert
    Long insertQuestion(VDQuestion v);

    //TODO Compléter les autres actions

    @Query("SELECT * FROM VDQuestion")
    List<VDQuestion> lesVDQuestion();
}
