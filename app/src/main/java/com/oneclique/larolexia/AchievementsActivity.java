package com.oneclique.larolexia;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.oneclique.larolexia.LaroLexiaSQLite.LaroLexiaSQLite;

public class AchievementsActivity extends AppCompatActivityHelper {

    private ListView mListViewAchievementsList;
    private LaroLexiaSQLite laroLexiaSQLite;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fullScreen();
        setContentView(R.layout.activity_achievements);

        mListViewAchievementsList = findViewById(R.id.mListViewAchievementsList);
        laroLexiaSQLite = new LaroLexiaSQLite(AchievementsActivity.this);

    }
}
