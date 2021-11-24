package com.example.votodroid;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.example.votodroid.bd.BD;
import com.example.votodroid.exceptions.MauvaisVote;
import com.example.votodroid.exceptions.MauvaiseQuestion;
import com.example.votodroid.modele.VDQuestion;
import com.example.votodroid.modele.VDVote;
import com.example.votodroid.service.ServiceImplementation;

@RunWith(AndroidJUnit4.class)
public class TestsApplication {

    private BD bd;
    private ServiceImplementation service;

    // S'exécute avant chacun des tests. Crée une BD en mémoire
    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        bd = Room.inMemoryDatabaseBuilder(context, BD.class).build();
        service = ServiceImplementation.getInstance(bd);
    }


    @Test(expected = MauvaiseQuestion.class)
    public void ajoutQuestionKOVide() throws MauvaiseQuestion {
        VDQuestion question = new VDQuestion();
        question.texteQuestion = "";
        service.creerQuestion(question);

        Assert.fail("Exception MauvaiseQuestion non lancée");
    }


    @Test(expected = MauvaiseQuestion.class)
    public void ajoutQuestionKOCourte() throws MauvaiseQuestion {
        VDQuestion question = new VDQuestion();
        question.texteQuestion = "aa";
        service.creerQuestion(question);

        Assert.fail("Exception MauvaiseQuestion non lancée");
    }


    @Test(expected = MauvaiseQuestion.class)
    public void ajoutQuestionKOLongue() throws MauvaiseQuestion {
        VDQuestion question = new VDQuestion();
        for (int i = 0 ; i < 256 ; i ++) question.texteQuestion += "aa";
        service.creerQuestion(question);

        Assert.fail("Exception MauvaiseQuestion non lancée");
    }


    @Test(expected = MauvaiseQuestion.class)
    public void ajoutQuestionKOIDFixe() throws MauvaiseQuestion {
        VDQuestion question = new VDQuestion();
        question.texteQuestion = "aaaaaaaaaaaaaaaa";
        question.idQuestion = 5L;
        service.creerQuestion(question);

        Assert.fail("Exception MauvaiseQuestion non lancée");
    }


    @Test
    public void ajoutQuestionOK() throws MauvaiseQuestion {
        VDQuestion question = new VDQuestion();
        question.texteQuestion = "Aimes-tu les brownies au chocolat?";
        service.creerQuestion(question);

        Assert.assertNotNull(question.idQuestion);
    }


    @Test(expected = MauvaiseQuestion.class)
    public void ajoutQuestionKOExiste() throws MauvaiseQuestion {
        VDQuestion question = new VDQuestion();
        VDQuestion question2 = new VDQuestion();

        question.texteQuestion = "Aimes-tu les brownies au chocolat?";
        question2.texteQuestion = "Aimes-tu les BROWNIES au chocolAT?";

        service.creerQuestion(question);
        service.creerQuestion(question2);

        //TODO Ce test va fail tant que vous n'implémenterez pas toutesLesQuestions() dans ServiceImplementation
        Assert.fail("Exception MauvaiseQuestion non lancée");
    }

    //region testVote
    @Test
    public void testAjoutVoteOK() throws MauvaisVote {
        VDVote vote = new VDVote();
        vote.vote = 10;
        vote.nomVoteur = "test";
        vote.QuestionID = 1L;
        service.creerVote(vote);

        Assert.assertNotNull(vote.idVote);
    }

    @Test(expected = MauvaisVote.class)
    public void testAjoutVoteKOExiste() throws MauvaisVote {
        VDVote testVote01 = new VDVote();
        testVote01.vote = 10;
        testVote01.nomVoteur = "test";
        testVote01.QuestionID = 1L;

        VDVote testVote02 = new VDVote();
        testVote02.vote = 10;
        testVote02.nomVoteur = "TEST";
        testVote02.QuestionID = 1L;

        service.creerVote(testVote01);
        service.creerVote(testVote02);

        Assert.fail("Exception MauvaisVote non lancée");
    }

    @Test(expected = MauvaisVote.class)
    public void testAjoutVoteKOPlus10() throws MauvaisVote {
        VDVote vdVote = new VDVote();
        vdVote.vote = 99;
        vdVote.nomVoteur = "test";
        vdVote.QuestionID = 1L;
        service.creerVote(vdVote);

        Assert.fail("Exception MauvaisVote non lancée");
    }

    @Test(expected = MauvaisVote.class)
    public void testAjoutVoteKOMoins0() throws MauvaisVote {
        VDVote vdVote = new VDVote();
        vdVote.vote = -99;
        vdVote.nomVoteur = "test";
        vdVote.QuestionID = 1L;
        service.creerVote(vdVote);

        Assert.fail("Exception MauvaisVote non lancée");
    }

    @Test(expected = MauvaisVote.class)
    public void testAjoutVoteKONomVide() throws MauvaisVote {
        VDVote vdVote = new VDVote();
        vdVote.vote = 9;
        vdVote.nomVoteur = "";
        vdVote.QuestionID = 1L;
        service.creerVote(vdVote);

        Assert.fail("Exception MauvaisVote non lancée");
    }

    //endregion

    //region testDeleteBD
    public void RemplirBDQuestion(){
        for (int i = 0; i < 5; i++){
            VDQuestion vdQuestion = new VDQuestion();
            vdQuestion.texteQuestion = "testRemplirBD0" + i;
        }
    }

    public void RemplirBDVote(){
        
    }

    @Test
    public void testSupprimerToutQuestion(){

    }

    @Test
    public void testSupprimerToutVote(){

    }
    //endregion

    /*
    @After
    public void closeDb() {
        bd.close();
    }
    */
}