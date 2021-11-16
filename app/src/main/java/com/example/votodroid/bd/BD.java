package com.example.votodroid.bd;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.votodroid.modele.VDQuestion;

@Database(entities = {VDQuestion.class}, version = 2)
public abstract class BD extends RoomDatabase {
    public abstract MonDao monDao();
}
