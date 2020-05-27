package com.oneclique.larolexia;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
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

import pl.droidsonroids.gif.GifAnimationMetaData;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageButton;
import pl.droidsonroids.gif.GifImageView;

public class LuksongTinikActivity extends AppCompatActivityHelper {

    private ImageButton mImageButtonPause;
    private TextView mTextViewQuestion;
    private TextView mTextViewScore;
    private ImageView mImageViewObject;
    private EditText mEditTextAnswer;
    private ImageButton mImageButtonSubmitAnswer;
    private TextView mTextViewClue;

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
    private String summativeTest = "";
    private String additionalQuery = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fullScreen();
        setContentView(R.layout.activity_luksong_tinik);

        mTextViewClue = findViewById(R.id.mTextViewClue);
        mImageButtonPause = findViewById(R.id.mImageButtonPause);
        mTextViewQuestion = findViewById(R.id.mTextViewQuestion);
        mTextViewScore = findViewById(R.id.mTextViewScore);
        mImageViewObject = findViewById(R.id.mImageViewObject);
        mEditTextAnswer = findViewById(R.id.mEditTextAnswer);
        mImageButtonSubmitAnswer = findViewById(R.id.mImageButtonSubmitAnswer);

        Intent intent = getIntent();
        lettersModel = new LettersModel();
        wordsModel = new WordsModel();
        try{
            summativeTest = (String) Objects.requireNonNull(intent.getExtras().getSerializable(SUMMATIVE_TEST));
            additionalQuery = randomGet20;
        }catch (Exception ee){
            summativeTest = "";
            additionalQuery = "";
        }
        lettersModel = (LettersModel) Objects.requireNonNull(intent.getExtras()).getSerializable(CHOSEN_LETTER);
        wordsModel = (WordsModel) Objects.requireNonNull(intent.getExtras()).getSerializable(CHOSEN_SYLLABLE);
        playerStatisticModel = (PlayerStatisticModel) Objects.requireNonNull(intent.getExtras()).getSerializable(PLAYER_STATISTICS);
        questionModelList = new ArrayList<>();
        sequenceOfQuestions = new ArrayList<>();

        laroLexiaSQLite = new LaroLexiaSQLite(LuksongTinikActivity.this);
        laroLexiaSQLite.createDatabase();

        countUpTimer = new CountUpTimer();


        mImageButtonPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mTextViewScore.setText("0");

        String selectedObject = "";

        try{
            selectedObject = wordsModel.getA_salita();
            blank = "__";
            additionalQuery = "";
        }catch (Exception e){
            Log.i(TAG, "word model is null:<------------");
        }

        try{
            selectedObject = lettersModel.getA_letter();
            blank = "_";
            additionalQuery = "";
        }catch (Exception e){
            Log.i(TAG, "letter model is null:<------------");
        }

        try{
            if(!summativeTest.equals("")){
                selectedObject = "%%";
                Log.i(TAG, "onCreate: Summative");
                Log.i(TAG, "onCreate: " + summativeTest);
                switch (summativeTest){
                    case SUMMATIVE_TEST_PANTIG:{
                        blank = "__";
                        break;
                    }
                    case SUMMATIVE_TEST_TITIK:{
                        blank = "_";
                        break;
                    }
                }
            }
        }catch (Exception e){
            Log.i(TAG, "Summative test titik is null:<------------");
        }
        playerStatisticModel.setLetter(selectedObject);
        Log.i(TAG, "selectedObject: " + selectedObject);
        if(!selectedObject.equals("")){
            Cursor questionsCursor = laroLexiaSQLite.executeReader(
                    "Select * from " + Table_Questions.DB_TABLE_NAME + " " +
                            "Where " + Table_Questions.DB_COL_LETTER + " LIKE " +
                            " '" + selectedObject + "' AND " +
                            "" + Table_Questions.DB_COL_LEVEL + " = '" + playerStatisticModel.getLevel() + "' AND " +
                            "" + Table_Questions.DB_COL_GAMEMODE + " = '" + playerStatisticModel.getGameMode() + "'" +
                            "" + additionalQuery + ";"
            );
            if(questionsCursor.getCount() != 0){

                while (questionsCursor.moveToNext()){
                    QuestionModel questionModel = new QuestionModel();
                    questionModel.setA_id(questionsCursor.getString(questionsCursor.getColumnIndex(Table_Questions.DB_COL_ID)));
                    questionModel.setA_answer(questionsCursor.getString(questionsCursor.getColumnIndex(Table_Questions.DB_COL_ANSWER)));
                    questionModel.setA_choices(questionsCursor.getString(questionsCursor.getColumnIndex(Table_Questions.DB_COL_CHOICES)));
                    questionModel.setA_gameMode(questionsCursor.getString(questionsCursor.getColumnIndex(Table_Questions.DB_COL_GAMEMODE)));
                    questionModel.setA_instruction(questionsCursor.getString(questionsCursor.getColumnIndex(Table_Questions.DB_COL_INSTRUCTION)));
                    questionModel.setA_letter(questionsCursor.getString(questionsCursor.getColumnIndex(Table_Questions.DB_COL_LETTER)));
                    questionModel.setA_level(questionsCursor.getString(questionsCursor.getColumnIndex(Table_Questions.DB_COL_LEVEL)));
                    questionModel.setA_question(questionsCursor.getString(questionsCursor.getColumnIndex(Table_Questions.DB_COL_QUESTION)));
                    questionModel.setA_question_code(questionsCursor.getString(questionsCursor.getColumnIndex(Table_Questions.DB_COL_QUESTION_CODE)));
                    questionModelList.add(questionModel);
                }
                Log.i(TAG, "questionModelList.size(): " + questionModelList.size());
                sequenceOfQuestions = LogicHelper.randomNumbers(1, questionModelList.size());
                List<QuestionModel> tmpQuestionModelList = new ArrayList<>();
                for (int i = 0; i < questionModelList.size(); i++) {
                    tmpQuestionModelList.add(questionModelList.get(sequenceOfQuestions.get(i)-1));
                }
                questionModelList = tmpQuestionModelList;
                countUpTimer.start();
//                setUpPaloseboQuestion(mTextViewPaloseboInstruction, mTextViewPaloseboQuestion, mTextViewPaloseboScore,
//                        imageButtonChoices, questionModelList, currentQuestion, score, imageViews, playerStatisticModel, countUpTimer);
                setUpLuksongTinik(mImageViewObject, mTextViewScore, mEditTextAnswer,
                        mImageButtonSubmitAnswer, questionModelList, currentQuestion,
                        score, playerStatisticModel, mTextViewClue, countUpTimer);
            }
        }

    }

    private void setUpLuksongTinik(final ImageView mImageViewObject,
                                   final TextView mTextViewScore,
                                   final EditText mEditTextAnswer,
                                   final ImageButton mImageButtonSubmitAnswer,
                                   final List<QuestionModel> questionModelList,
                                   final int currentQuestion, final int score,
                                   final PlayerStatisticModel playerStatisticModel,
                                   final TextView mTextViewClue,
                                   final CountUpTimer countUpTimer){
        final LuksongTinikCorrect luksongTinikCorrect = new LuksongTinikCorrect(LuksongTinikActivity.this);
        if(currentQuestion != questionModelList.size()){
            final QuestionModel questionModel = questionModelList.get(currentQuestion);
//            Toast.makeText(LuksongTinikActivity.this, questionModel.getA_question(), Toast.LENGTH_SHORT).show();
            mEditTextAnswer.setText("");
            mTextViewClue.setText("");
            mImageViewObject.setImageDrawable(null);
            mImageViewObject.setContentDescription(questionModel.getA_answer().toLowerCase().trim());
            try{
                mImageViewObject.setImageDrawable(GetDrawableResource(LuksongTinikActivity.this, questionModel.getA_answer().toLowerCase().trim()));
                Log.i(TAG, "mImageViewObject.setImageDrawable: " + questionModel.getA_answer().toLowerCase().trim());
            }
            catch (Exception e){
                mImageViewObject.setImageDrawable(null);
                Log.i(TAG, "mImageViewObject.setImageDrawable: " + questionModel.getA_answer().toLowerCase().trim());
            }

            mTextViewClue.setText(questionModel.getA_choices().replace(blank, ""));

            mImageButtonSubmitAnswer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String answer = mEditTextAnswer.getText().toString().toLowerCase().trim() + mTextViewClue.getText().toString().trim().toLowerCase();
                    if(!answer.equals("")){
                        if(mImageViewObject.getContentDescription().toString().equals(answer)){
                            //TODO CORRECT
                            Log.i(TAG, "onClick: Tama");

                            luksongTinikCorrect.mTextViewCorrectionStatus.setText("Napakahusay!");
                            luksongTinikCorrect.mImageViewTumbangPreso.setVisibility(View.VISIBLE);
                            luksongTinikCorrect.dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialog) {
                                    mTextViewScore.setText(String.valueOf(score + 1));
                                    checkIfGameIsCompleted((currentQuestion + 1), questionModelList.size(),
                                            String.valueOf((score + 1)), countUpTimer);
                                    setUpLuksongTinik(mImageViewObject, mTextViewScore, mEditTextAnswer,
                                            mImageButtonSubmitAnswer, questionModelList, (currentQuestion + 1),
                                            (score + 1), playerStatisticModel, mTextViewClue, countUpTimer);
                                }
                            });
                        }else {
                            //TODO WRONG
                            Log.i(TAG, "onClick: Mali!");
                            luksongTinikCorrect.mTextViewCorrectionStatus.setText("Subukan muli.");
                            luksongTinikCorrect.mImageViewTumbangPreso.setVisibility(View.INVISIBLE);
                            if((currentQuestion + 1) != questionModelList.size()){
                                luksongTinikCorrect.dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                    @Override
                                    public void onCancel(DialogInterface dialog) {
                                        mTextViewScore.setText(String.valueOf(score));
                                        setUpLuksongTinik(mImageViewObject, mTextViewScore, mEditTextAnswer,
                                                mImageButtonSubmitAnswer, questionModelList, (currentQuestion + 1),
                                                (score), playerStatisticModel, mTextViewClue, countUpTimer);
                                    }
                                });
                            }else{
                                checkIfGameIsCompleted((currentQuestion + 1), questionModelList.size(),
                                        String.valueOf((score)), countUpTimer);
                            }
                        }
                        try{
                            luksongTinikCorrect.mImageViewCorrectImage.setContentDescription("");
                            luksongTinikCorrect.mImageViewCorrectImage.setImageDrawable(null);
                            luksongTinikCorrect.mImageViewCorrectImage.setContentDescription(questionModel.getA_answer().toLowerCase().trim());
                            try{
                                luksongTinikCorrect.mImageViewCorrectImage.setImageDrawable(GetDrawableResource(LuksongTinikActivity.this, questionModel.getA_answer().toLowerCase().trim()));
                                Log.i(TAG, "mImageViewObject.setImageDrawable: " + questionModel.getA_answer().toLowerCase().trim());
                            }
                            catch (Exception e){
                                luksongTinikCorrect.mImageViewCorrectImage.setImageDrawable(null);
                                Log.i(TAG, "mImageViewObject.setImageDrawable: " + questionModel.getA_answer().toLowerCase().trim());
                            }
                            luksongTinikCorrect.mTextViewCorrectWord.setText(questionModel.getA_answer());
                            luksongTinikCorrect.dialog.show();
                        }catch (Exception e){
                            Log.i(TAG, "onClick: " + e.getMessage());
                        }
                    }
                }
            });
        }
    }

    public void checkIfGameIsCompleted(final int currentQuestion, final int maxQuestion,
                                       final String score, final CountUpTimer countUpTimer){
        final FinishedGame finishedGame = new FinishedGame(LuksongTinikActivity.this);
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
                case "0":{
                    stars = 0;
                    break;
                }
                default:{
                    double score_ = (Double.parseDouble(score)/questionModelList.size());
                    Log.i(TAG, "score_: " + score_);
                    if(score_ >= 90 || score_ <= 100){
                        stars = 3;
                    }else if(score_ >= 75 || score_ <= 89){
                        stars = 2;
                    }else if(score_ >= 60 || score_ <= 74){
                        stars = 1;
                    }
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
                    "" + Table_Achievements.DB_COL_LETTER + " like '" + playerStatisticModel.getLetter() + "' and " +
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
                            "" + Table_Achievements.DB_COL_DATE_TIME_USED + ", " +
                            "" + Table_Achievements.DB_COL_TRIES + ") " +
                            "VALUES ( '" + System.currentTimeMillis() + "', " +
                            "'" + playerStatisticModel.getUsername() + "', " +
                            "'" + playerStatisticModel.getGameMode() + "', " +
                            "'" + playerStatisticModel.getLevel() + "', " +
                            "'" + (playerStatisticModel.getLetter().equals("%%") ? "Huling Pagsusulit" : playerStatisticModel.getLetter()) + "', " +
                            "'" + stars + "', " +
                            "'" + score + "', " +
                            "'" + dateNow + "', " +
                            "'" + 1 + "');");

                    Log.i(TAG, ("laroLexiaSQLite.executeWriter(INSERT INTO: data is inserted!"));
                }catch (SQLiteException e){
                    Toast.makeText((LuksongTinikActivity.this), e.getMessage(), Toast.LENGTH_LONG).show();
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
                        Toast.makeText((LuksongTinikActivity.this), e.getMessage(), Toast.LENGTH_LONG).show();
                        Log.i(TAG, "laroLexiaSQLite.executeWriter((UPDATE: " + e.getMessage());
                    }
                }
                int nt;
                try {
                    nt = Integer.parseInt(achievementsModel.getA_tries());
                }catch (Exception e){
                    nt = 0;
                }
                int numberOfTries = nt + 1;
                try{
                    laroLexiaSQLite.executeWriter(("UPDATE " + Table_Achievements.DB_TABLE_NAME + " " +
                            "set " + Table_Achievements.DB_COL_TRIES + " = '" + numberOfTries + "' " +
                            "WHERE " +
                            "" + Table_Achievements.DB_COL_ID + " = '" + achievementsModel.getA_id() + "' AND " +
                            "" + Table_Achievements.DB_COL_USERNAME + " = '" + achievementsModel.getA_username() + "' AND " +
                            "" + Table_Achievements.DB_COL_GAMEMODE + " = '" + achievementsModel.getA_gameMode() + "' AND " +
                            "" + Table_Achievements.DB_COL_LEVEL + " = '" + achievementsModel.getA_level() + "' AND  " +
                            "" + Table_Achievements.DB_COL_LETTER + " = '" + achievementsModel.getA_letter() + "';"));
                    Log.i(TAG, ("laroLexiaSQLite.executeWriter(UPDATE: data is updated!"));
                }catch (SQLiteException e){
                    Log.i(TAG, "numberOfTries((UPDATE: " + e.getMessage());
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
                Toast.makeText((LuksongTinikActivity.this), e.getMessage(), Toast.LENGTH_LONG).show();
                Log.i(TAG, ("laroLexiaSQLite.executeWriter((UPDATE: " + e.getMessage()));
            }

            Log.i(TAG, ("onAnimationEnd: achievement added!"));
            int layoutId = 0;
            String soundName = "";
            if(playerStatisticModel.getGameMode().equals("TITIK")){
                layoutId = R.layout.dialog_storyline_letra_level3_ending1;
                soundName = "juan_storyline_level3_ending2";
            }else if(playerStatisticModel.getGameMode().equals("SALITA")){
                layoutId = R.layout.dialog_storyline_letra_level3_ending2;
                soundName = "juan_storyline_level3_ending1";
            }
            final Storyline storyline = new Storyline(LuksongTinikActivity.this, layoutId);
            final PlaySound playSound = new PlaySound(LuksongTinikActivity.this);
            storyline.dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    playSound.stop();
                    finishedGame.dialog.show();
                }
            });
            storyline.mImageButtonStorylinePlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    storyline.dialog.cancel();
                }
            });
            playSound.play(soundName, false);
            storyline.dialog.show();
        }
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(PLAYER_STATISTICS, playerStatisticModel);
        Log.i(TAG, "onBackPressed: LuksongTinikActivity");
        PlayerStatisticLog(playerStatisticModel);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

}
