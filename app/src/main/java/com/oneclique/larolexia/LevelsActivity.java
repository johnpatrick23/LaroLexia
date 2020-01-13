package com.oneclique.larolexia;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.oneclique.larolexia.model.PlayerStatisticModel;

import java.util.Objects;

public class LevelsActivity extends AppCompatActivityHelper {

    private PlayerStatisticModel playerStatisticModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fullScreen();
        setContentView(R.layout.activity_levels);

        Intent intent = getIntent();
        playerStatisticModel = (PlayerStatisticModel) Objects.requireNonNull(intent.getExtras())
                .getSerializable(PLAYER_STATISTICS);

    }
    public void SelectedLevel(View v){
        Intent intent;
        if(playerStatisticModel.getGameMode().equals("TITIK")){
            intent = new Intent(LevelsActivity.this, TitikInGameChoicesActivity.class);
        }else {
            intent = new Intent(LevelsActivity.this, SalitaInGameChoicesActivity.class);
        }
        switch (v.getId()){
            case R.id.mButtonLevel1:{
                playerStatisticModel.setLevel("1");
                Log.i(TAG, "SelectedLevel: 1");
                Toast.makeText(LevelsActivity.this, "Level 1", Toast.LENGTH_SHORT).show();
                intent.putExtra(PLAYER_STATISTICS, playerStatisticModel);
                break;
            }
            case R.id.mButtonLevel2:{
                playerStatisticModel.setLevel("2");
                Log.i(TAG, "SelectedLevel: 3");
                Toast.makeText(LevelsActivity.this, "Level 2", Toast.LENGTH_SHORT).show();
                intent.putExtra(PLAYER_STATISTICS, playerStatisticModel);
                break;
            }
            case R.id.mButtonLevel3:{
                playerStatisticModel.setLevel("3");
                Log.i(TAG, "SelectedLevel: 3");
                Toast.makeText(LevelsActivity.this, "Level 3", Toast.LENGTH_SHORT).show();
                intent.putExtra(PLAYER_STATISTICS, playerStatisticModel);
                break;
            }
        }
        PlayerStatisticLog(playerStatisticModel);
        startActivityForResult(intent, REQUEST_PLAYER_STATISTIC);

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
        Log.i(TAG, "onBackPressed: LevelsActivity");
        PlayerStatisticLog(playerStatisticModel);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}
