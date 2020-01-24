package com.oneclique.larolexia;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
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
    private Intent intent_;

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
        int resourceId = 0;
        int mode = 0;
        String soundName = "";
        if(playerStatisticModel.getGameMode().equals("TITIK")){
            intent_ = new Intent(LevelsActivity.this, TitikInGameChoicesActivity.class);
            mode = 1;
        }
        else {
            intent_ = new Intent(LevelsActivity.this, SalitaInGameChoicesActivity.class);
            mode = 2;
        }
        switch (v.getId()){
            case R.id.mButtonLevel1:{
                playerStatisticModel.setLevel("1");
                Log.i(TAG, "SelectedLevel: 1");
                resourceId = mode == 1 ? R.layout.dialog_storyline_letra_level1_scenario_1 : R.layout.dialog_storyline_letra_level1_scenario_2;
                soundName = mode == 1 ? "juan_storyline_level1_scenario1" : "juan_storyline_level1_scenario2";
                //Toast.makeText(LevelsActivity.this, "Level 1", Toast.LENGTH_SHORT).show();
                intent_.putExtra(PLAYER_STATISTICS, playerStatisticModel);
                break;
            }
            case R.id.mButtonLevel2:{
                playerStatisticModel.setLevel("2");
                Log.i(TAG, "SelectedLevel: 3");
                resourceId = mode == 1 ? R.layout.dialog_storyline_letra_level2_scenario_1 : R.layout.dialog_storyline_letra_level2_scenario_2;
                soundName = mode == 1 ? "juan_storyline_level2_scenario1" : "juan_storyline_level2_scenario2";
                //Toast.makeText(LevelsActivity.this, "Level 2", Toast.LENGTH_SHORT).show();
                intent_.putExtra(PLAYER_STATISTICS, playerStatisticModel);
                break;
            }
            case R.id.mButtonLevel3:{
                playerStatisticModel.setLevel("3");
                Log.i(TAG, "SelectedLevel: 3");
                resourceId = 0;
                //Toast.makeText(LevelsActivity.this, "Level 3", Toast.LENGTH_SHORT).show();
                intent_.putExtra(PLAYER_STATISTICS, playerStatisticModel);
                break;
            }
        }
        if(resourceId!=0){
            final Storyline storyline = new Storyline(LevelsActivity.this, resourceId);
            final PlaySound playSound = new PlaySound(LevelsActivity.this);
            storyline.dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    playSound.stop();
                    PlayerStatisticLog(playerStatisticModel);
                    startActivityForResult(intent_, REQUEST_PLAYER_STATISTIC);
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
        }else {
            PlayerStatisticLog(playerStatisticModel);
            startActivityForResult(intent_, REQUEST_PLAYER_STATISTIC);
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
        Log.i(TAG, "onBackPressed: LevelsActivity");
        PlayerStatisticLog(playerStatisticModel);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}
