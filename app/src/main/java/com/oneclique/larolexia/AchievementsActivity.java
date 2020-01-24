package com.oneclique.larolexia;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.oneclique.larolexia.LaroLexiaSQLite.LaroLexiaSQLite;
import com.oneclique.larolexia.LaroLexiaSQLite.LaroLexiaSQLiteModel.AchievementsModel;
import com.oneclique.larolexia.LaroLexiaSQLite.SQLITE_VARIABLES;
import com.oneclique.larolexia.LaroLexiaSQLite.SQLITE_VARIABLES.*;
import com.oneclique.larolexia.adapter.AchievementsListViewAdapter;
import com.oneclique.larolexia.model.PlayerStatisticModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AchievementsActivity extends AppCompatActivityHelper {

    private ListView mListViewAchievementsList;
    private LaroLexiaSQLite laroLexiaSQLite;


    private AchievementsListViewAdapter achievementsListViewAdapter;

    private List<AchievementsModel> achievementsModelList;

    private PlayerStatisticModel playerStatisticModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fullScreen();
        setContentView(R.layout.activity_achievements);

        mListViewAchievementsList = findViewById(R.id.mListViewAchievementsList);
        Intent intent = getIntent();
        playerStatisticModel = (PlayerStatisticModel) Objects.requireNonNull(intent.getExtras()).getSerializable(PLAYER_STATISTICS);
        achievementsModelList = new ArrayList<>();
        laroLexiaSQLite = new LaroLexiaSQLite(AchievementsActivity.this);
        laroLexiaSQLite.createDatabase();

        Cursor cursor = laroLexiaSQLite.executeReader(("SELECT * FROM " + Table_Achievements.DB_TABLE_NAME + " " +
                "WHERE " + Table_Achievements.DB_COL_USERNAME + " = '" + playerStatisticModel.getUsername() + "';"));

        if(cursor.getCount() != 0){
            while (cursor.moveToNext()){
                AchievementsModel achievementsModel = new AchievementsModel();
                achievementsModel.setA_id(cursor.getString(cursor.getColumnIndex(Table_Achievements.DB_COL_ID)));
                achievementsModel.setA_date_time_used(cursor.getString(cursor.getColumnIndex(Table_Achievements.DB_COL_DATE_TIME_USED)));
                achievementsModel.setA_gameMode(cursor.getString(cursor.getColumnIndex(Table_Achievements.DB_COL_GAMEMODE)));
                achievementsModel.setA_letter(cursor.getString(cursor.getColumnIndex(Table_Achievements.DB_COL_LETTER)));
                achievementsModel.setA_level(cursor.getString(cursor.getColumnIndex(Table_Achievements.DB_COL_LEVEL)));
                achievementsModel.setA_star(cursor.getString(cursor.getColumnIndex(Table_Achievements.DB_COL_STAR)));
                achievementsModel.setA_time(cursor.getString(cursor.getColumnIndex(Table_Achievements.DB_COL_TIME)));
                achievementsModel.setA_username(cursor.getString(cursor.getColumnIndex(Table_Achievements.DB_COL_USERNAME)));
                achievementsModel.setA_username(cursor.getString(cursor.getColumnIndex(Table_Achievements.DB_COL_USERNAME)));
                achievementsModel.setA_tries(cursor.getString(cursor.getColumnIndex(Table_Achievements.DB_COL_TRIES)));
                AchievementsModelLog(achievementsModel);
                achievementsModelList.add(achievementsModel);
            }

            achievementsListViewAdapter = new AchievementsListViewAdapter((AchievementsActivity.this), achievementsModelList);

            mListViewAchievementsList.setAdapter(achievementsListViewAdapter);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == REQUEST_PLAYER_STATISTIC){
            if(resultCode == Activity.RESULT_OK){
                playerStatisticModel = (PlayerStatisticModel) Objects.requireNonNull(
                        Objects.requireNonNull(data).getExtras()).getSerializable(PLAYER_STATISTICS);
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(PLAYER_STATISTICS, playerStatisticModel);
        Log.i(TAG, "onBackPressed: AchievementsActivity");
        PlayerStatisticLog(playerStatisticModel);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}
