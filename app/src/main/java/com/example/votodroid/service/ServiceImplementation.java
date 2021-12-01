package com.example.votodroid.service;

import com.example.votodroid.bd.BD;
import com.example.votodroid.exceptions.MauvaisVote;
import com.example.votodroid.exceptions.MauvaiseQuestion;
import com.example.votodroid.modele.VDQuestion;
import com.example.votodroid.modele.VDVote;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
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

        /*List<VDQuestion> listTrier = new ArrayList<>();

        int freq = 0;
        for (int i = 0; i < lq.size(); i++){
            for (VDQuestion q: lq){
                if (maBD.monDao().nbVote(q.idQuestion) == freq){
                    listTrier.add(q);
                }
            }
            freq++;
        }
        Collections.reverse(listTrier);*/

        Collections.sort(lq, new Comparator<VDQuestion>() {
            @Override
            public int compare(VDQuestion o1, VDQuestion o2) {
                int nbVotes1 = nombreDeVotePour(o1);
                int nbVotes2 = nombreDeVotePour(o2);
                if (nbVotes1 > nbVotes2) return -1;

                if (nbVotes1 < nbVotes2) return +1;

                return 0;
            }
        });

        return lq;
    }

    private int nombreDeVotePour(VDQuestion o1) {
        int nbVote = 0;
        for (VDVote v: toutLesVotes()) if (v.QuestionID.equals(o1.idQuestion)) nbVote++;
        return nbVote;
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

    
    public String ecartTypeVotes(Long questionID) {
        int resultat = 0;
        int nbQuestion = 0;
        int totalVote = 0;

        if (!toutLesVotes().isEmpty()){
            for (VDVote v: toutLesVotes()){
                if (v.QuestionID.equals(questionID)){
                    nbQuestion++;
                    totalVote += v.vote;
                }
            }

            resultat = totalVote*(3);

            DecimalFormat df = new DecimalFormat();
            df.setMaximumFractionDigits(2);

            return df.format(Math.sqrt(resultat));
        }
        return "0";
    }

    
    public Map<Integer, Integer> distributionVotes(VDQuestion question) {
        return null;
    }
}
