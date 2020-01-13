package com.oneclique.larolexia;

public interface Variables {



    enum GameMode
    {
        TITIK("TITIK"),
        SALITA("SALITA");

        private String value;

        public String getValue()
        {
            return this.value;
        }

        GameMode(String value)
        {
            this.value = value;
        }
    }
}
