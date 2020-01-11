package com.oneclique.larolexia;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import com.oneclique.larolexia.LaroLexiaSQLite.LaroLexiaSQLite;
import com.oneclique.larolexia.model.PlayerStatisticModel;

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

        laroLexiaSQLite = new LaroLexiaSQLite(MainActivity.this);

        laroLexiaSQLite.createDatabase();

        


    }
}
