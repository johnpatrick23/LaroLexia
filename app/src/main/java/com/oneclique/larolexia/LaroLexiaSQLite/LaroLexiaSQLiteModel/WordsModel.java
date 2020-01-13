package com.oneclique.larolexia.LaroLexiaSQLite.LaroLexiaSQLiteModel;

import java.io.Serializable;

public class WordsModel implements Serializable {
    private String a_salita;
    private String a_instruction;
    private String a_examples_images;
    private String a_examples_text;

    public void setA_instruction(String a_instruction) {
        this.a_instruction = a_instruction;
    }

    public String getA_instruction() {
        return a_instruction;
    }

    public void setA_examples_text(String a_examples_text) {
        this.a_examples_text = a_examples_text;
    }

    public String getA_examples_text() {
        return a_examples_text;
    }

    public void setA_examples_images(String a_examples_images) {
        this.a_examples_images = a_examples_images;
    }

    public String getA_examples_images() {
        return a_examples_images;
    }

    public void setA_salita(String a_salita) {
        this.a_salita = a_salita;
    }

    public String getA_salita() {
        return a_salita;
    }
}
