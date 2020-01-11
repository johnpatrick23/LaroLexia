package com.oneclique.larolexia.model;

import com.oneclique.larolexia.LaroLexiaSQLite.LaroLexiaSQLiteModel.QuestionModel;

import java.io.Serializable;
import java.util.List;

public class PlayerStatisticModel implements Serializable {

    private String username;
    private String gameMode;
    private String letter;
    private String character;
    private String level;
    private String sessionId;
    private String larongLahi;

   /* private String numbersOfRetake;

    public String getNumbersOfRetake() {
        return numbersOfRetake;
    }

    public void setNumbersOfRetake(String numbersOfRetake) {
        this.numbersOfRetake = numbersOfRetake;
    }*/

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    private List<QuestionModel> questionTakenList;

    public void setQuestionTakenList(List<QuestionModel> questionTakenList) {
        this.questionTakenList = questionTakenList;
    }

    public List<QuestionModel> getQuestionTakenList() {
        return questionTakenList;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }

    public String getLetter() {
        return letter;
    }

    public void setGameMode(String gameMode) {
        this.gameMode = gameMode;
    }

    public String getGameMode() {
        return gameMode;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
