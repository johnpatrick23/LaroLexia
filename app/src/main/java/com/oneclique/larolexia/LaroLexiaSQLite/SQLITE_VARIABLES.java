package com.oneclique.larolexia.LaroLexiaSQLite;

public final class SQLITE_VARIABLES {
    final static String DB_NAME = "larolexia.db";

    public final static class Table_Users{
        public final static String DB_TABLE_NAME = "tbl_users";
        public final static String DB_COL_ID = "a_id";
        public final static String DB_COL_USERNAME = "a_username";
        public final static String DB_COL_CHARACTER = "a_character";
        public final static String DB_COL_LAST_USED = "a_last_used";
        public final static String DB_COL_STARS = "a_stars";
        public final static String DB_COL_NEW_USER = "a_new_user";
    }

    public final static class Table_Questions{
        public final static String DB_TABLE_NAME = "tbl_questions";
        public final static String DB_COL_ID = "a_id";
        public final static String DB_COL_GAMEMODE = "a_gameMode";
        public final static String DB_COL_LEVEL = "a_level";
        public final static String DB_COL_LETTER = "a_letter";
        public final static String DB_COL_QUESTION = "a_question";
        public final static String DB_COL_CHOICES = "a_choices";
        public final static String DB_COL_ANSWER = "a_answer";
        public final static String DB_COL_INSTRUCTION = "a_instruction";
        public final static String DB_COL_QUESTION_CODE = "a_question_code";
    }

    public final static class Table_Letters{
        public final static String DB_TABLE_NAME = "tbl_letters";
        public final static String DB_COL_LETTER = "a_letter";
        public final static String DB_COL_INSTRUCTION = "a_instruction";
        public final static String DB_COL_EXAMPLES_IMAGES = "a_examples_images";
        public final static String DB_COL_EXAMPLES_TEXT = "a_examples_text";
    }

    public final static class Table_Salita{
        public final static String DB_TABLE_NAME = "tbl_salita";
        public final static String DB_COL_SALITA = "a_salita";
        public final static String DB_COL_INSTRUCTION = "a_instruction";
        public final static String DB_COL_EXAMPLES_IMAGES = "a_examples_images";
        public final static String DB_COL_EXAMPLES_TEXT = "a_examples_text";
    }

    public final static class Table_Achievements{
        public final static String DB_TABLE_NAME = "tbl_achievements";
        public final static String DB_COL_ID = "a_id";
        public final static String DB_COL_USERNAME = "a_username";
        public final static String DB_COL_GAMEMODE = "a_gameMode";
        public final static String DB_COL_LEVEL = "a_level";
        public final static String DB_COL_LETTER = "a_letter";
        public final static String DB_COL_STAR = "a_star";
        public final static String DB_COL_TIME = "a_time";
        public final static String DB_COL_DATE_TIME_USED = "a_date_time_used";
    }
}
