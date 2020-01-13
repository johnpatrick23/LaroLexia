package com.oneclique.larolexia;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.oneclique.larolexia.LaroLexiaSQLite.LaroLexiaSQLite;
import com.oneclique.larolexia.LaroLexiaSQLite.LaroLexiaSQLiteModel.AchievementsModel;
import com.oneclique.larolexia.LaroLexiaSQLite.LaroLexiaSQLiteModel.LettersModel;
import com.oneclique.larolexia.LaroLexiaSQLite.LaroLexiaSQLiteModel.QuestionModel;
import com.oneclique.larolexia.LaroLexiaSQLite.LaroLexiaSQLiteModel.WordsModel;
import com.oneclique.larolexia.LaroLexiaSQLite.SQLITE_VARIABLES;
import com.oneclique.larolexia.LaroLexiaSQLite.SQLITE_VARIABLES.*;
import com.oneclique.larolexia.helper.CountUpTimer;
import com.oneclique.larolexia.helper.LogicHelper;
import com.oneclique.larolexia.model.PlayerStatisticModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class BasagPalayokActivity extends AppCompatActivityHelper {

    private TextView mTextViewBasagPalayokInstruction;
    private TextView mTextViewBasagpalayokScore;
    private TextView mTextViewBasagPalayokQuestion;

    private ImageButton mImageButtonBasagPalayokPause;

    private Button[] mButtonBasagPalayokChoices;

    private LaroLexiaSQLite laroLexiaSQLite;
    private PlayerStatisticModel playerStatisticModel;

    private List<QuestionModel> questionModelList;

    private List<Integer> sequenceOfQuestions;

    private int count = 0;

    private String blank = "";
    private int score = 0;

    private LettersModel lettersModel;
    private WordsModel wordsModel;

    private int currentQuestion = 0;

    private CountUpTimer countUpTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fullScreen();
        setContentView(R.layout.activity_basag_palayok);

        mTextViewBasagPalayokInstruction = findViewById(R.id.mTextViewBasagPalayokInstruction);
        mTextViewBasagpalayokScore = findViewById(R.id.mTextViewBasagpalayokScore);
        mTextViewBasagPalayokQuestion = findViewById(R.id.mTextViewBasagPalayokQuestion);
        mImageButtonBasagPalayokPause = findViewById(R.id.mImageButtonBasagPalayokPause);



        Intent intent = getIntent();
        lettersModel = new LettersModel();
        wordsModel = new WordsModel();
        lettersModel = (LettersModel) Objects.requireNonNull(intent.getExtras()).getSerializable(CHOSEN_LETTER);
        wordsModel = (WordsModel) Objects.requireNonNull(intent.getExtras()).getSerializable(CHOSEN_SYLLABLE);
        playerStatisticModel = (PlayerStatisticModel) Objects.requireNonNull(intent.getExtras()).getSerializable(PLAYER_STATISTICS);
        questionModelList = new ArrayList<>();
        sequenceOfQuestions = new ArrayList<>();

        laroLexiaSQLite = new LaroLexiaSQLite(BasagPalayokActivity.this);
        laroLexiaSQLite.createDatabase();

        countUpTimer = new CountUpTimer();
        mImageButtonBasagPalayokPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mTextViewBasagpalayokScore.setText("0");

        String selectedObject = "";

        try{
            selectedObject = wordsModel.getA_salita();
            mButtonBasagPalayokChoices = new Button[]{
                    findViewById(R.id.mButtonBasagPalayokChoice1),
                    findViewById(R.id.mButtonBasagPalayokChoice2),
                    findViewById(R.id.mButtonBasagPalayokChoice3),
                    findViewById(R.id.mButtonBasagPalayokChoice4),
                    findViewById(R.id.mButtonBasagPalayokChoice5)
            };
            blank = "__";
        }catch (Exception e){
            Log.i(TAG, "word model is null:<------------");
        }

        try{
            selectedObject = lettersModel.getA_letter();
            mButtonBasagPalayokChoices = new Button[]{
                    findViewById(R.id.mButtonBasagPalayokChoice1),
                    findViewById(R.id.mButtonBasagPalayokChoice2),
                    findViewById(R.id.mButtonBasagPalayokChoice3),
                    findViewById(R.id.mButtonBasagPalayokChoice4)
            };

            Button buttonChoice = findViewById(R.id.mButtonBasagPalayokChoice5);
            buttonChoice.setVisibility(View.GONE);
            blank = "_";
        }catch (Exception e){
            Log.i(TAG, "letter model is null:<------------");
        }
        playerStatisticModel.setLetter(selectedObject);
        Log.i(TAG, "selectedObject: " + selectedObject);
        if(!selectedObject.equals("")){
            try{

                Cursor cursor = laroLexiaSQLite.executeReader(
                        "Select * from " + Table_Questions.DB_TABLE_NAME + " " +
                                "Where " + Table_Questions.DB_COL_LETTER + " = " +
                                " '" + selectedObject + "' AND " +
                                "" + Table_Questions.DB_COL_LEVEL + " = '" + playerStatisticModel.getLevel() + "' ;"
                );

                Log.i(TAG, "cursor.getCount(): " + cursor.getCount());
                if(cursor.getCount() != 0){
                    while (cursor.moveToNext()){
                        QuestionModel questionModel = new QuestionModel();
                        questionModel.setA_id(cursor.getString(cursor.getColumnIndex(Table_Questions.DB_COL_ID)));
                        questionModel.setA_answer(cursor.getString(cursor.getColumnIndex(Table_Questions.DB_COL_ANSWER)));
                        questionModel.setA_choices(cursor.getString(cursor.getColumnIndex(Table_Questions.DB_COL_CHOICES)));
                        questionModel.setA_gameMode(cursor.getString(cursor.getColumnIndex(Table_Questions.DB_COL_GAMEMODE)));
                        questionModel.setA_instruction(cursor.getString(cursor.getColumnIndex(Table_Questions.DB_COL_INSTRUCTION)));
                        questionModel.setA_letter(cursor.getString(cursor.getColumnIndex(Table_Questions.DB_COL_LETTER)));
                        questionModel.setA_level(cursor.getString(cursor.getColumnIndex(Table_Questions.DB_COL_LEVEL)));
                        questionModel.setA_question(cursor.getString(cursor.getColumnIndex(Table_Questions.DB_COL_QUESTION)));
                        questionModel.setA_question_code(cursor.getString(cursor.getColumnIndex(Table_Questions.DB_COL_QUESTION_CODE)));
                        QuestionModelLog(questionModel);
                        questionModelList.add(questionModel);
                    }
                }
                Log.i(TAG, "questionModelList.size(): " + questionModelList.size());
                sequenceOfQuestions = LogicHelper.randomNumbers(1, questionModelList.size());
                List<QuestionModel> tmpQuestionModelList = new ArrayList<>();
                for (int i = 0; i < questionModelList.size(); i++) {
                    tmpQuestionModelList.add(questionModelList.get(sequenceOfQuestions.get(i)-1));
                }
                questionModelList = tmpQuestionModelList;
                countUpTimer.start();

                setUpBasagPalayokQuestion(mTextViewBasagPalayokQuestion, mTextViewBasagpalayokScore,
                        mButtonBasagPalayokChoices, questionModelList, currentQuestion, score,
                        playerStatisticModel, countUpTimer);
            }catch (SQLiteException e){
                Log.i(TAG, "onCreate: " + e.getMessage());
            }

        }

    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(PLAYER_STATISTICS, playerStatisticModel);
        Log.i(TAG, "onBackPressed: BasagPalayokActivity");
        PlayerStatisticLog(playerStatisticModel);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }


    public void setUpBasagPalayokQuestion(
            //TextView mTextViewBasagPalayokInstruction,
            final TextView mTextViewBasagPalayokQuestion,
            final TextView mTextViewBasagpalayokScore,
            final Button[] mButtonBasagPalayokChoices,
            final List<QuestionModel> questionModelList,
            final int currentQuestion, final int score,
            final PlayerStatisticModel playerStatisticModel, final CountUpTimer countUpTimer)
    {
        final BasagPalayokCorrect basagPalayokCorrect = new BasagPalayokCorrect(BasagPalayokActivity.this);

        if(currentQuestion != questionModelList.size()){
            final QuestionModel questionModel = questionModelList.get(currentQuestion);
            //Toast.makeText(BasagPalayokActivity.this, questionModel.getA_question(), Toast.LENGTH_SHORT).show();

            /*mTextViewBasagPalayokInstruction.setText(
                    Html.fromHtml(questionModel.getA_instruction().replace("<char/>", playerStatisticModel.getCharacter())));*/

            mTextViewBasagPalayokQuestion.setText(Html.fromHtml(questionModel.getA_question()));
            List<String> choices = LogicHelper.choiceReBuilder(questionModel.getA_choices());
            final List<Integer> numbers = LogicHelper.randomNumbers(1, choices.size());
            List<String> tmpChoices = new ArrayList<>();
            mTextViewBasagpalayokScore.setText(String.valueOf(score));
            Log.i(TAG, "setUpPaloseboQuestion: Choices");
            for (int i = 0; i < choices.size(); i++) {
                tmpChoices.add(choices.get(numbers.get(i)-1).trim());
                Log.i(TAG, "setUpPaloseboQuestion: " + choices.get(numbers.get(i)-1));
            }

            final List<String> finalChoices  = tmpChoices;

            for (int i = 0; i < mButtonBasagPalayokChoices.length; i++){
                final int finalI = i;
                mButtonBasagPalayokChoices[i].setTextColor(Color.WHITE);
                mButtonBasagPalayokChoices[i].setText(finalChoices.get(i));
                mButtonBasagPalayokChoices[finalI].setBackgroundResource(R.drawable.ic_palayok);
                mButtonBasagPalayokChoices[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mButtonBasagPalayokChoices[finalI].setTextColor(Color.BLACK);
                        mButtonBasagPalayokChoices[finalI].setBackgroundResource(R.drawable.ic_broken_palayok);
                        String correctWord = "";
                        if(questionModel.getA_answer().toLowerCase().trim().equals(finalChoices.get(finalI).toLowerCase())){
                            // TODO Correct
                            Log.i(TAG, "onClick: Tama");
                            String getA_question = questionModel.getA_question();
                            correctWord =
                                    getA_question
                                            .replace("Piliin ang nawawalang letra ", "")
                                            .replace("<b>", "")
                                            .replace("</b>","")
                                            .replace(blank, questionModel.getA_answer());
                            basagPalayokCorrect.mTextViewCorrectWord.setText(correctWord);
                            basagPalayokCorrect.dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialog) {
                                    mTextViewBasagpalayokScore.setText(String.valueOf(score + 1));
                                    checkIfGameIsCompleted((currentQuestion + 1), questionModelList.size(),
                                            String.valueOf((score + 1)), countUpTimer);
                                    setUpBasagPalayokQuestion(mTextViewBasagPalayokQuestion, mTextViewBasagpalayokScore,
                                            mButtonBasagPalayokChoices, questionModelList, (currentQuestion + 1), (score + 1),
                                            playerStatisticModel, countUpTimer);
                                }
                            });
                            basagPalayokCorrect.dialog.show();
                        }else {
                            // TODO Wrong
                            Log.i(TAG, "onClick: Mali");
                            basagPalayokCorrect.mTextViewCorrectionStatus.setText("Subukan muli.");
                            //climbUp(imageViews, score, questionModelList.size(), String.valueOf((score)), playerStatisticModel.getCharacter());
                            Log.i(TAG, "(currentQuestion + 1): " + (currentQuestion + 1));
                            Log.i(TAG, "questionModelList.size(): " + questionModelList.size());
                            String getA_question = questionModel.getA_question();
                            correctWord =
                                    getA_question
                                            .replace("Piliin ang nawawalang letra ", "")
                                            .replace("<b>", "")
                                            .replace("</b>","")
                                            .replace(blank, questionModel.getA_answer());
                            basagPalayokCorrect.mTextViewCorrectWord.setText(correctWord);
                            if((currentQuestion + 1) != questionModelList.size()){
                                basagPalayokCorrect.dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                    @Override
                                    public void onCancel(DialogInterface dialog) {
                                        mTextViewBasagpalayokScore.setText(String.valueOf(score + 1));
                                        setUpBasagPalayokQuestion(mTextViewBasagPalayokQuestion,
                                                mTextViewBasagpalayokScore, mButtonBasagPalayokChoices, questionModelList,
                                                (currentQuestion + 1), score, playerStatisticModel, countUpTimer);
                                    }
                                });
                            }else{
                                checkIfGameIsCompleted((currentQuestion + 1), questionModelList.size(),
                                        String.valueOf((score)), countUpTimer);
                            }
                        }
                        try{
                            Log.i(TAG, "correctWord: " + correctWord);
                            basagPalayokCorrect.dialog.show();
                        }catch (Exception e){
                            Log.i(TAG, "onClick: " + e.getMessage());
                        }
                    }
                });

            }

        }

    }

    public void checkIfGameIsCompleted(final int currentQuestion, final int maxQuestion,
                                       final String score, final CountUpTimer countUpTimer){
        final FinishedGame finishedGame = new FinishedGame(BasagPalayokActivity.this);
        finishedGame.mButtonFinishedPlayAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishedGame.dialog.cancel();
                finish();
            }
        });

        Log.i(TAG, "onAnimationEnd: " + currentQuestion);
        if(currentQuestion == maxQuestion){
            final int[] timeConsumed = {0};
            countUpTimer.setGameTimerListener(new CountUpTimer.GameTimerListener() {
                @Override
                public void onCancelTime(int time) {
                    timeConsumed[0] = time;
                }
            });
            countUpTimer.cancel();

            String points = finishedGame.mTextViewPointsGameMode.getText().toString();
            points = points.replace("points", score);
            finishedGame.mTextViewFinishedGameMode.setText("Natapos mo ang laro!");
            finishedGame.mTextViewPointsGameMode.setText(points);
            int stars = 0;
            switch (score){
                case "5":{
                    stars = 3;
                    break;
                }
                case "3": case "4": {
                    stars = 2;
                    break;
                }
                case "2": case "1":{
                    stars = 1;
                    break;
                }
            }
            finishedGame.mImageViewFinishedGameStars[0].setImageDrawable(getDrawable(R.drawable.ic_starlocked));
            finishedGame.mImageViewFinishedGameStars[1].setImageDrawable(getDrawable(R.drawable.ic_starlocked));
            finishedGame.mImageViewFinishedGameStars[2].setImageDrawable(getDrawable(R.drawable.ic_starlocked));

            for (int i = 0; i < stars; i++){
                finishedGame.mImageViewFinishedGameStars[i].setImageDrawable(getDrawable(R.drawable.ic_star));
            }
            AchievementsModel achievementsModel = new AchievementsModel();
            DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
            Date dateobj = new Date();
            int totalStars = 0;
            String dateNow = df.format(dateobj);


            Cursor cursor = laroLexiaSQLite.executeReader(("SELECT * FROM " + Table_Achievements.DB_TABLE_NAME + " " +
                    "where " +
                    "" + Table_Achievements.DB_COL_USERNAME + " = '" + playerStatisticModel.getUsername() + "' and " +
                    "" + Table_Achievements.DB_COL_GAMEMODE + " = '" + playerStatisticModel.getGameMode() + "' and " +
                    "" + Table_Achievements.DB_COL_LETTER + " = '" + playerStatisticModel.getLetter() + "' and " +
                    "" + Table_Achievements.DB_COL_LEVEL + " = '" + playerStatisticModel.getLevel() + "';"));

            Log.i(TAG, ("cursor: " + cursor));
            if(cursor.getCount() == 0){
                try {
                    laroLexiaSQLite.executeWriter("INSERT INTO " + Table_Achievements.DB_TABLE_NAME + " " +
                            "(" + Table_Achievements.DB_COL_ID + ", " +
                            "" + Table_Achievements.DB_COL_USERNAME + ", " +
                            "" + Table_Achievements.DB_COL_GAMEMODE + ", " +
                            "" + Table_Achievements.DB_COL_LEVEL + ", " +
                            "" + Table_Achievements.DB_COL_LETTER + ", " +
                            "" + Table_Achievements.DB_COL_STAR + ", " +
                            "" + Table_Achievements.DB_COL_TIME + ", " +
                            "" + Table_Achievements.DB_COL_DATE_TIME_USED + ") " +
                            "VALUES ( '" + System.currentTimeMillis() + "', " +
                            "'" + playerStatisticModel.getUsername() + "', " +
                            "'" + playerStatisticModel.getGameMode() + "', " +
                            "'" + playerStatisticModel.getLevel() + "', " +
                            "'" + playerStatisticModel.getLetter() + "', " +
                            "'" + stars + "', " +
                            "'" + score + "', " +
                            "'" + dateNow + "');");

                    Log.i(TAG, ("laroLexiaSQLite.executeWriter(INSERT INTO: data is inserted!"));
                }catch (SQLiteException e){
                    Toast.makeText((BasagPalayokActivity.this), e.getMessage(), Toast.LENGTH_LONG).show();
                    Log.i(TAG, "laroLexiaSQLite.executeWriter(INSERT INTO: " + e.getMessage());
                }
            }
            else{

                while (cursor.moveToNext()){
                    achievementsModel.setA_id(cursor.getString(cursor.getColumnIndex(Table_Achievements.DB_COL_ID)));
                    achievementsModel.setA_date_time_used(cursor.getString(cursor.getColumnIndex(Table_Achievements.DB_COL_DATE_TIME_USED)));
                    achievementsModel.setA_gameMode(cursor.getString(cursor.getColumnIndex(Table_Achievements.DB_COL_GAMEMODE)));
                    achievementsModel.setA_letter(cursor.getString(cursor.getColumnIndex(Table_Achievements.DB_COL_LETTER)));
                    achievementsModel.setA_level(cursor.getString(cursor.getColumnIndex(Table_Achievements.DB_COL_LEVEL)));
                    achievementsModel.setA_star(cursor.getString(cursor.getColumnIndex(Table_Achievements.DB_COL_STAR)));
                    achievementsModel.setA_time(cursor.getString(cursor.getColumnIndex(Table_Achievements.DB_COL_TIME)));
                    achievementsModel.setA_username(cursor.getString(cursor.getColumnIndex(Table_Achievements.DB_COL_USERNAME)));
                    AchievementsModelLog(achievementsModel);
                }
                if(stars >= Integer.parseInt(achievementsModel.getA_star())){
                    try{
                        laroLexiaSQLite.executeWriter(("UPDATE " + Table_Achievements.DB_TABLE_NAME + " " +
                                "set " + Table_Achievements.DB_COL_STAR + " = '" + stars + "', " +
                                "" + Table_Achievements.DB_COL_TIME + " = '" + score + "' " +
                                "WHERE " +
                                "" + Table_Achievements.DB_COL_ID + " = '" + achievementsModel.getA_id() + "' AND " +
                                "" + Table_Achievements.DB_COL_USERNAME + " = '" + achievementsModel.getA_username() + "' AND " +
                                "" + Table_Achievements.DB_COL_GAMEMODE + " = '" + achievementsModel.getA_gameMode() + "' AND " +
                                "" + Table_Achievements.DB_COL_LEVEL + " = '" + achievementsModel.getA_level() + "' AND  " +
                                "" + Table_Achievements.DB_COL_LETTER + " = '" + achievementsModel.getA_letter() + "';"));
                        Log.i(TAG, ("laroLexiaSQLite.executeWriter(UPDATE: data is updated!"));
                    }catch (SQLiteException e){
                        Toast.makeText((BasagPalayokActivity.this), e.getMessage(), Toast.LENGTH_LONG).show();
                        Log.i(TAG, "laroLexiaSQLite.executeWriter((UPDATE: " + e.getMessage());
                    }
                }
            }

            cursor = laroLexiaSQLite.executeReader((
                    "SELECT " + Table_Achievements.DB_COL_STAR+ " " +
                            "FROM " + Table_Achievements.DB_TABLE_NAME + " " +
                            "where " + Table_Achievements.DB_COL_USERNAME + " = '" + playerStatisticModel.getUsername() + "';"));

            Log.i(TAG, ("cursor: " + cursor));
            if(cursor.getCount() != 0){
                while (cursor.moveToNext()){
                    totalStars += Integer.parseInt(cursor.getString(cursor.getColumnIndex(Table_Achievements.DB_COL_STAR)));
                }
            }
            Log.i(TAG, ("totalStars: " + totalStars));
            try {
                //region update user
                laroLexiaSQLite.executeWriter(("UPDATE " + Table_Users.DB_TABLE_NAME + " " +
                        "SET " + Table_Users.DB_COL_STARS + " = '" + totalStars + "' " +
                        "WHERE " + Table_Users.DB_COL_USERNAME + " = '" + playerStatisticModel.getUsername() + "';"));
                Log.i(TAG, ("update user: data is updated!"));
                //endregion
            }catch (SQLiteException e){
                Toast.makeText((BasagPalayokActivity.this), e.getMessage(), Toast.LENGTH_LONG).show();
                Log.i(TAG, ("laroLexiaSQLite.executeWriter((UPDATE: " + e.getMessage()));
            }

            Log.i(TAG, ("onAnimationEnd: achievement added!"));

            finishedGame.dialog.show();
        }
    }

}
