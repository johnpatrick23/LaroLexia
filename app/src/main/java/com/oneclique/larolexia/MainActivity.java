package com.oneclique.larolexia;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.oneclique.larolexia.LaroLexiaSQLite.LaroLexiaSQLite;
import com.oneclique.larolexia.LaroLexiaSQLite.LaroLexiaSQLiteModel.UsersModel;
import com.oneclique.larolexia.LaroLexiaSQLite.SQLITE_VARIABLES;
import com.oneclique.larolexia.LaroLexiaSQLite.SQLITE_VARIABLES.*;
import com.oneclique.larolexia.model.PlayerStatisticModel;

import java.io.IOException;
import java.util.Objects;

public class MainActivity extends AppCompatActivityHelper {

    private ImageButton mImageButtonMainAboutGame;
    private ImageButton mImageButtonMainClose;

    private ImageButton mImageButtonMainPlay;
    private ImageButton mImageButtonMainAchievements;
    private ImageButton mImageButtonMainSettings;

    private TextView mTextViewMainUserStars;
    private TextView mTextViewMainUsername;

    private LaroLexiaSQLite laroLexiaSQLite;

    private PlayerStatisticModel playerStatisticModel;

    private UsersModel usersModel;

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fullScreen();
        setContentView(R.layout.activity_main);
        requestPermission(MainActivity.this);

        mImageButtonMainAboutGame = findViewById(R.id.mImageButtonMainAboutGame);
        mImageButtonMainClose = findViewById(R.id.mImageButtonMainClose);

        mImageButtonMainPlay = findViewById(R.id.mImageButtonMainPlay);
        mImageButtonMainAchievements = findViewById(R.id.mImageButtonMainAchievements);
        mImageButtonMainSettings = findViewById(R.id.mImageButtonMainSettings);

        mTextViewMainUserStars = findViewById(R.id.mTextViewMainUserStars);
        mTextViewMainUsername = findViewById(R.id.mTextViewMainUsername);

        playerStatisticModel = new PlayerStatisticModel();
        usersModel = new UsersModel();

        laroLexiaSQLite = new LaroLexiaSQLite(MainActivity.this);

        laroLexiaSQLite.createDatabase();

        mImageButtonMainClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mImageButtonMainSettings.setVisibility(View.INVISIBLE);
        mImageButtonMainAboutGame.setVisibility(View.INVISIBLE);

        try{
            Cursor cursor = laroLexiaSQLite.executeReader(
                    "SELECT * FROM " + Table_Users.DB_TABLE_NAME +
                            " where " + Table_Users.DB_COL_LAST_USED + " = '1';");

            if(cursor.getCount() != 0){
                mImageButtonMainPlay.setEnabled(true);
                while (cursor.moveToNext()){
                    usersModel.setA_id(cursor.getString(cursor.getColumnIndex(Table_Users.DB_COL_ID)));
                    usersModel.setA_username(cursor.getString(cursor.getColumnIndex(Table_Users.DB_COL_USERNAME)));
                    usersModel.setA_stars(cursor.getString(cursor.getColumnIndex(Table_Users.DB_COL_STARS)));
                    usersModel.setA_character(cursor.getString(cursor.getColumnIndex(Table_Users.DB_COL_CHARACTER)));
                    usersModel.setA_last_used(cursor.getString(cursor.getColumnIndex(Table_Users.DB_COL_LAST_USED)));
                    usersModel.setA_new_user(cursor.getString(cursor.getColumnIndex(Table_Users.DB_COL_NEW_USER)));
                }
                UserModelLog(usersModel);
                mTextViewMainUsername.setText(usersModel.getA_username());
                playerStatisticModel.setUsername(usersModel.getA_username());
                playerStatisticModel.setCharacter(usersModel.getA_character());
                mTextViewMainUserStars.setText(usersModel.getA_stars());
            }
            else {
                mTextViewMainUserStars.setText("0");
                Log.i(TAG, "laroLexiaSQLite.executeReader: no user");
            }
        }catch (SQLiteException e){
            Log.i(TAG, "laroLexiaSQLite.executeReader: " + e.getMessage());
        }

        mImageButtonMainPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mTextViewMainUsername.getText().toString().equals("")){
                    intent = new Intent(MainActivity.this, GameModeActivity.class);
                    playerStatisticModel.setSessionId(String.valueOf(System.currentTimeMillis()));
                    intent.putExtra(PLAYER_STATISTICS, playerStatisticModel);
                    PlayerStatisticLog(playerStatisticModel);
                    startActivityForResult(intent, REQUEST_PLAYER_STATISTIC);
                }
            }
        });

        mImageButtonMainSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO SETTINGS

            }
        });

        mImageButtonMainAchievements.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mTextViewMainUsername.getText().toString().equals("")){
                    intent = new Intent(MainActivity.this, AchievementsActivity.class);
                    intent.putExtra(PLAYER_STATISTICS, playerStatisticModel);
                    PlayerStatisticLog(playerStatisticModel);
                    startActivityForResult(intent, REQUEST_PLAYER_STATISTIC);
                }
            }
        });

        mTextViewMainUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MainActivity.this, SelectUserActivity.class);
                startActivityForResult(intent, REQUEST_SELECTED_USERNAME);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.i(TAG, "onActivityResult: requestCode = " + requestCode);
        Log.i(TAG, "onActivityResult: resultCode = " + resultCode);
        if(requestCode == REQUEST_SELECTED_USERNAME){
            if(resultCode == Activity.RESULT_OK){
                usersModel = (UsersModel) Objects.requireNonNull(
                        Objects.requireNonNull(data).getExtras()).getSerializable(SELECTED_USERNAME);
                laroLexiaSQLite.executeWriter(
                        "UPDATE " + SQLITE_VARIABLES.Table_Users.DB_TABLE_NAME + " " +
                                "SET " + SQLITE_VARIABLES.Table_Users.DB_COL_LAST_USED + " = '0' " +
                                "WHERE " + SQLITE_VARIABLES.Table_Users.DB_COL_LAST_USED + " = '1';");
                try{
                    laroLexiaSQLite.executeWriter(
                            "UPDATE " + SQLITE_VARIABLES.Table_Users.DB_TABLE_NAME + " " +
                                    "SET " + SQLITE_VARIABLES.Table_Users.DB_COL_LAST_USED + " = '1' " +
                                    "WHERE " + SQLITE_VARIABLES.Table_Users.DB_COL_USERNAME + " = '" +
                                    usersModel.getA_username() + "';");
                    mTextViewMainUsername.setText(usersModel.getA_username());
                    mTextViewMainUserStars.setText(usersModel.getA_stars());
                    playerStatisticModel.setUsername(usersModel.getA_username());
                    playerStatisticModel.setCharacter(usersModel.getA_character());
                }catch (SQLiteException ex){
                    Toast.makeText(MainActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }else if(requestCode == REQUEST_PLAYER_STATISTIC){
            if(resultCode == Activity.RESULT_OK){
                PlayerStatisticModel playerStatisticModel = (PlayerStatisticModel) Objects.requireNonNull(
                        Objects.requireNonNull(data).getExtras()).getSerializable(PLAYER_STATISTICS);

                try{
                    PlayerStatisticLog(playerStatisticModel);
                    Cursor usernamesCursor = laroLexiaSQLite.executeReader(
                            "SELECT * FROM " + SQLITE_VARIABLES.Table_Users.DB_TABLE_NAME +
                                    " where " + SQLITE_VARIABLES.Table_Users.DB_COL_USERNAME + " = '" + playerStatisticModel.getUsername() + "';");

                    if(usernamesCursor.getCount() != 0){
                        mImageButtonMainPlay.setEnabled(true);
                        while(usernamesCursor.moveToNext()){
                            usersModel.setA_id(usernamesCursor.getString(usernamesCursor.getColumnIndex(
                                    SQLITE_VARIABLES.Table_Users.DB_COL_ID)));
                            usersModel.setA_username(usernamesCursor.getString(usernamesCursor.getColumnIndex(
                                    SQLITE_VARIABLES.Table_Users.DB_COL_USERNAME)));
                            usersModel.setA_character(usernamesCursor.getString(usernamesCursor.getColumnIndex(
                                    SQLITE_VARIABLES.Table_Users.DB_COL_CHARACTER)));
                            usersModel.setA_last_used(usernamesCursor.getString(usernamesCursor.getColumnIndex(
                                    SQLITE_VARIABLES.Table_Users.DB_COL_LAST_USED)));
                            usersModel.setA_stars(usernamesCursor.getString(usernamesCursor.getColumnIndex(
                                    SQLITE_VARIABLES.Table_Users.DB_COL_STARS)));
                        }
                        mTextViewMainUsername.setText(usersModel.getA_username());
                        playerStatisticModel.setUsername(usersModel.getA_username());
                        playerStatisticModel.setCharacter(usersModel.getA_character());
                        mTextViewMainUserStars.setText(usersModel.getA_stars());
                        PlayerStatisticLog(playerStatisticModel);
                        UserModelLog(usersModel);
                    }

                }catch (Exception e){
                    Log.i(TAG, "onCreate: " + e.getMessage());
                }
            }
        }
    }
}
