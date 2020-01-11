package com.oneclique.larolexia.LaroLexiaSQLite.LaroLexiaSQLiteModel;

public class QuestionModel {
    private String a_id;
    private String a_gameMode;
    private String a_level;
    private String a_letter;
    private String a_question;
    private String a_choices;
    private String a_answer;
    private String a_instruction;
    private String a_question_code;

    public String getA_question_code() {
        return a_question_code;
    }

    public void setA_question_code(String a_question_code) {
        this.a_question_code = a_question_code;
    }

    public String getA_letter() {
        return a_letter;
    }

    public void setA_letter(String a_letter) {
        this.a_letter = a_letter;
    }

    public String getA_level() {
        return a_level;
    }

    public void setA_level(String a_level) {
        this.a_level = a_level;
    }

    public String getA_gameMode() {
        return a_gameMode;
    }

    public void setA_gameMode(String a_gameMode) {
        this.a_gameMode = a_gameMode;
    }

    public String getA_id() {
        return a_id;
    }

    public void setA_id(String a_id) {
        this.a_id = a_id;
    }

    public String getA_answer() {
        return a_answer;
    }

    public void setA_answer(String a_answer) {
        this.a_answer = a_answer;
    }

    public String getA_choices() {
        return a_choices;
    }

    public void setA_choices(String a_choices) {
        this.a_choices = a_choices;
    }

    public String getA_instruction() {
        return a_instruction;
    }

    public void setA_instruction(String a_instruction) {
        this.a_instruction = a_instruction;
    }

    public String getA_question() {
        return a_question;
    }

    public void setA_question(String a_question) {
        this.a_question = a_question;
    }
}
