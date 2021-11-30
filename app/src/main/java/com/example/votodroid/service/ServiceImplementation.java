package com.example.votodroid.service;

import com.example.votodroid.bd.BD;
import com.example.votodroid.exceptions.MauvaisVote;
import com.example.votodroid.exceptions.MauvaiseQuestion;
import com.example.votodroid.modele.VDQuestion;
import com.example.votodroid.modele.VDVote;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class ServiceImplementation{

    private static ServiceImplementation single_instance = null;
    private BD maBD;

    public ServiceImplementation (BD maBD){
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

    
    public void creerVote(VDVote vdVote) throws MauvaisVote {
        if (vdVote.nomVoteur == null || vdVote.nomVoteur.trim().length() == 0) throw new MauvaisVote("Nom vide");
        if (vdVote.nomVoteur.trim().length() < 4) throw new MauvaisVote("Nom trop court");
        if (vdVote.nomVoteur.trim().length() > 255) throw new MauvaisVote("Nom trop long");
        if (vdVote.idVote != null) throw new MauvaisVote("Id non nul. La BD doit le gérer");
        if (vdVote.vote > 10) throw new MauvaisVote("Valeur du vote supérieur à 5 étoiles");
        if (vdVote.vote < 0) throw new MauvaisVote("Valeur du vote inférieur à 0");

        for (VDVote v : maBD.monDao().lesVDVote()){
            if (v.QuestionID.equals(vdVote.QuestionID) && v.nomVoteur.toUpperCase().equals(vdVote.nomVoteur.toUpperCase())){
                throw new MauvaisVote("Vous avez déjà voté la question.");
            }
        }

        vdVote.idVote = maBD.monDao().insertVote(vdVote);
    }

    public List<VDVote> toutLesVotes() {
        return maBD.monDao().lesVDVote();
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

    public void supprimerToutVote() {
        maBD.monDao().deleteVote();
    }

    public void supprimerToutQuestion() {
        maBD.monDao().deleteQuestion();
    }

    
    public String moyenneVotes(Long questionID) {
        int nbQuestion = 0;
        int totalVote = 0;

        if (!toutLesVotes().isEmpty()){
            for (VDVote v: toutLesVotes()){
                if (v.QuestionID.equals(questionID)){
                    nbQuestion++;
                    totalVote += v.vote;
                }
            }

            DecimalFormat df = new DecimalFormat();
            df.setMaximumFractionDigits(2);

            float moyenne = totalVote/nbQuestion;

            return df.format(moyenne);
        }
        return "0";
    }

    
    public float ecartTypeVotes(VDQuestion question) {
        return 0;
    }

    
    public Map<Integer, Integer> distributionVotes(VDQuestion question) {
        return null;
    }
}
