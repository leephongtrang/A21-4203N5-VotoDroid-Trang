package com.example.votodroid.bd;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.votodroid.modele.VDQuestion;
import com.example.votodroid.modele.VDVote;

import java.util.List;

@Dao
public interface MonDao {
    @Insert
    Long insertQuestion(VDQuestion v);

    //TODO Compléter les autres actions

    @Query("SELECT * FROM VDQuestion")
    List<VDQuestion> lesVDQuestion();

    @Query("SELECT * FROM VDVote")
    List<VDVote> lesVDVote();

    @Insert
    Long insertVote(VDVote v);

    @Query("DELETE FROM VDQuestion")
    public void deleteQuestion();

    @Query("DELETE FROM VDVote")
    public void deleteVote();

}
