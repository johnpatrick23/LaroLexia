package com.oneclique.larolexia;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.oneclique.larolexia.model.PlayerStatisticModel;

import java.util.Objects;

public class GameModeActivity extends AppCompatActivityHelper {

    private PlayerStatisticModel playerStatisticModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fullScreen();
        setContentView(R.layout.activity_game_mode);

        Intent intent = getIntent();
        playerStatisticModel = (PlayerStatisticModel) Objects.requireNonNull(intent.getExtras())
                .getSerializable(PLAYER_STATISTICS);

    }

    public void GameModeButton(View v){
        Intent intent = new Intent(GameModeActivity.this, LevelsActivity.class);

        switch (v.getId()){
            case R.id.mImageButtonTitik:{
                playerStatisticModel.setGameMode("TITIK");
                Log.i(TAG, "GameModeButton: TITIK");
                break;
            }
            case R.id.mImageButtonSalita:{
                playerStatisticModel.setGameMode("SALITA");
                Log.i(TAG, "GameModeButton: SALITA");
                break;
            }
            default: break;
        }
        intent.putExtra(PLAYER_STATISTICS, playerStatisticModel);
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
        Log.i(TAG, "onBackPressed: GameModeActivity");
        PlayerStatisticLog(playerStatisticModel);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

}
