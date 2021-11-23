package com.example.votodroid.service;

import com.example.votodroid.bd.BD;
import com.example.votodroid.exceptions.MauvaiseQuestion;
import com.example.votodroid.modele.VDQuestion;
import com.example.votodroid.modele.VDVote;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class ServiceImplementation{

    private static ServiceImplementation single_instance = null;
    private BD maBD;

    private ServiceImplementation (BD maBD){
        this.maBD = maBD;
    }

    public static ServiceImplementation getInstance(BD maBD)
    {
        if (single_instance == null)
            single_instance = new ServiceImplementation(maBD);

        return single_instance;
    }


    public void creerQuestion(VDQuestion vdQuestion) throws MauvaiseQuestion {
        // Validation
        if (vdQuestion.texteQuestion == null || vdQuestion.texteQuestion.trim().length() == 0) throw new MauvaiseQuestion("Question vide");
        if (vdQuestion.texteQuestion.trim().length() < 5) throw new MauvaiseQuestion("Question trop courte");
        if (vdQuestion.texteQuestion.trim().length() > 255) throw new MauvaiseQuestion("Question trop longue");
        if (vdQuestion.idQuestion != null) throw new MauvaiseQuestion("Id non nul. La BD doit le gérer");

        // Doublon du texte de la question
        for (VDQuestion q : toutesLesQuestions()){
            if (q.texteQuestion.toUpperCase().equals(vdQuestion.texteQuestion.toUpperCase())){
                    throw new MauvaiseQuestion("Question existante");
            }
        }

        // Ajout
        vdQuestion.idQuestion = maBD.monDao().insertQuestion(vdQuestion);
    }

    
    public void creerVote(VDVote vdVote) {

        vdVote.idVote = maBD.monDao().insertVote(vdVote);
    }

    
    public List<VDQuestion> toutesLesQuestions() {
        //TODO Trier la liste reçue en BD par nombre de votes et la retourner
        List<VDQuestion> lq = maBD.monDao().lesVDQuestion();
        List<VDVote> lv = maBD.monDao().lesVDVote();

        for (VDQuestion q : lq){

            for (VDVote v : lv) {

            }
        }

        return lq;
    }

    
    public float moyenneVotes(VDQuestion question) {
        return 0;
    }

    
    public float ecartTypeVotes(VDQuestion question) {
        return 0;
    }

    
    public Map<Integer, Integer> distributionVotes(VDQuestion question) {
        return null;
    }
}
