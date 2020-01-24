package com.oneclique.larolexia;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.oneclique.larolexia.LaroLexiaSQLite.LaroLexiaSQLite;
import com.oneclique.larolexia.LaroLexiaSQLite.LaroLexiaSQLiteModel.WordsModel;
import com.oneclique.larolexia.LaroLexiaSQLite.SQLITE_VARIABLES.*;
import com.oneclique.larolexia.adapter.TitikGridViewAdapter;
import com.oneclique.larolexia.helper.LogicHelper;
import com.oneclique.larolexia.model.PlayerStatisticModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SalitaInGameChoicesActivity extends AppCompatActivityHelper {
    private GridView mGridViewPantig;

    private LaroLexiaSQLite laroLexiaSQLite;

    private ReadPantig readPantig;

    private WordsModel wordsModel;

    private PlayerStatisticModel playerStatisticModel;
    private Button mButtonSalitaSummativeTest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fullScreen();
        setContentView(R.layout.activity_salita_in_game_choices);
        Intent intent = getIntent();
        playerStatisticModel = (PlayerStatisticModel) Objects.requireNonNull(intent.getExtras())
                .getSerializable(PLAYER_STATISTICS);
        mGridViewPantig = findViewById(R.id.mGridViewPantig);
        mButtonSalitaSummativeTest = findViewById(R.id.mButtonSalitaSummativeTest);

        List<String> pantigList = new ArrayList<>();
        wordsModel = new WordsModel();
        laroLexiaSQLite = new LaroLexiaSQLite(SalitaInGameChoicesActivity.this);
        laroLexiaSQLite.createDatabase();

        Cursor lettersCursor = laroLexiaSQLite.executeReader(
                "SELECT * FROM " + Table_Salita.DB_TABLE_NAME + " order by " + Table_Salita.DB_COL_SALITA + "; ");
        mButtonSalitaSummativeTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = null;
                switch (playerStatisticModel.getLevel()){
                    case "1":{
                        intent = new Intent(SalitaInGameChoicesActivity.this, PaloseboActivity.class);
                        break;
                    }
                    case "2":{
                        intent = new Intent(SalitaInGameChoicesActivity.this, BasagPalayokActivity.class);
                        break;
                    }
                    case "3":{
                        intent = new Intent(SalitaInGameChoicesActivity.this, LuksongTinikActivity.class);
                        break;
                    }
                }
                if (intent != null) {
                    intent.putExtra(SUMMATIVE_TEST, SUMMATIVE_TEST_PANTIG);
                    playerStatisticModel.setLetter(SUMMATIVE_TEST_PANTIG);
                    intent.putExtra(PLAYER_STATISTICS, playerStatisticModel);
                    PlayerStatisticLog(playerStatisticModel);
                    startActivity(intent);
                }

            }
        });
        if(lettersCursor.getCount() != 0){
            //Toast.makeText(TitikInGameChoicesActivity.this, String.valueOf(lettersCursor.getCount()), Toast.LENGTH_LONG).show();
            while(lettersCursor.moveToNext()){
                pantigList.add(lettersCursor.getString(lettersCursor.getColumnIndex(Table_Salita.DB_COL_SALITA)));
            }

            mGridViewPantig.setAdapter(new TitikGridViewAdapter(SalitaInGameChoicesActivity.this, pantigList));

            mGridViewPantig.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    try{
                        String selectedSyllable = ((TextView)view.findViewById(R.id.mTextViewLetter)).getText().toString();
                        Cursor lettersCursor = laroLexiaSQLite.executeReader(
                                "SELECT * FROM " + Table_Salita.DB_TABLE_NAME + " " +
                                        "WHERE " + Table_Salita.DB_COL_SALITA + " = " +
                                        "'" + selectedSyllable.toUpperCase() + "';");
                        if(lettersCursor.getCount() != 0){
                            while(lettersCursor.moveToNext()){
                                wordsModel.setA_salita(lettersCursor.getString(lettersCursor.getColumnIndex(Table_Salita.DB_COL_SALITA)));
                                wordsModel.setA_examples_images(lettersCursor.getString(lettersCursor.getColumnIndex(Table_Salita.DB_COL_EXAMPLES_IMAGES)));
                                wordsModel.setA_examples_text(lettersCursor.getString(lettersCursor.getColumnIndex(Table_Salita.DB_COL_EXAMPLES_TEXT)));
                                wordsModel.setA_instruction(lettersCursor.getString(lettersCursor.getColumnIndex(Table_Salita.DB_COL_INSTRUCTION)));
                            }
                            readPantig.show(wordsModel);
                        }
                    }catch (Exception e){
                        Toast.makeText(SalitaInGameChoicesActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
            readPantig = new ReadPantig(SalitaInGameChoicesActivity.this, playerStatisticModel, wordsModel);

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
        Log.i(TAG, "onBackPressed: TitikGameChoicesActivity");
        PlayerStatisticLog(playerStatisticModel);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    private class ReadPantig {
        private Dialog dialog;

        private TextView mTextViewInstructionToBeRead;

        private Button mButtonPakinggan;
        private Button mButtonMagpatuloy;
        private Button mButtonBumalik;
        private TextView[] mTextViewExamples;
        private ImageView[] mImageViews;

        ReadPantig(Context context, final PlayerStatisticModel playerStatisticModel, final WordsModel wordsModel){
            dialog = new Dialog(context);
            dialog.setContentView(R.layout.dialog_read_the_letter);

            final PlaySound playSound = new PlaySound(SalitaInGameChoicesActivity.this);
            FullscreenDialog(dialog);

            mTextViewInstructionToBeRead = dialog.findViewById(R.id.mTextViewInstructionToBeRead);
            mButtonPakinggan = dialog.findViewById(R.id.mButtonPakinggan);
            mTextViewExamples = new TextView[]{
                    dialog.findViewById(R.id.mTextViewExample1),
                    dialog.findViewById(R.id.mTextViewExample2),
                    dialog.findViewById(R.id.mTextViewExample3)};

            mImageViews = new ImageView[]{
                    dialog.findViewById(R.id.mImageViewExample1),
                    dialog.findViewById(R.id.mImageViewExample2),
                    dialog.findViewById(R.id.mImageViewExample3)};

            mButtonMagpatuloy = dialog.findViewById(R.id.mButtonMagpatuloy);
            mButtonBumalik = dialog.findViewById(R.id.mButtonBumalik);

            mButtonPakinggan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try{
                        playSound.stop();
                    }catch (Exception ignore){

                    }
                    Log.i(TAG, "wordsModel.getA_salita(): " + wordsModel.getA_salita());
                    playSound.play(wordsModel.getA_salita(), false);
                }
            });

            mButtonBumalik.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.cancel();
                }
            });

            mButtonMagpatuloy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = null;
                    playSound.stop();
                    switch (playerStatisticModel.getLevel()){
                        case "1":{
                            intent = new Intent(SalitaInGameChoicesActivity.this, PaloseboActivity.class);
                            break;
                        }
                        case "2":{
                            intent = new Intent(SalitaInGameChoicesActivity.this, BasagPalayokActivity.class);
                            break;
                        }
                        case "3":{
                            intent = new Intent(SalitaInGameChoicesActivity.this, LuksongTinikActivity.class);
                            break;
                        }
                    }
                    if (intent != null) {
                        intent.putExtra(CHOSEN_SYLLABLE, wordsModel);
                        playerStatisticModel.setLetter(wordsModel.getA_salita());
                        intent.putExtra(PLAYER_STATISTICS, playerStatisticModel);
                        PlayerStatisticLog(playerStatisticModel);
                        startActivity(intent);
                        dialog.cancel();
                    }

                }
            });

        }

        void show(WordsModel wordsModel){

            mTextViewInstructionToBeRead.setText(Html.fromHtml(wordsModel.getA_instruction()));

            List<String> exampleTexts = LogicHelper.choiceReBuilder(wordsModel.getA_examples_text());
            List<String> exampleImages = LogicHelper.choiceReBuilder(wordsModel.getA_examples_images());

            for(int i = 0; i < exampleTexts.size(); i++){
                mTextViewExamples[i].setText(Html.fromHtml(exampleTexts.get(i)));
                try{
                    mImageViews[i].setImageDrawable(GetDrawableResource(SalitaInGameChoicesActivity.this, exampleImages.get(i).toLowerCase()));
                }catch (Exception ignored){
//                    Toast.makeText(TitikInGameChoicesActivity.this, exampleTexts.get(i).toLowerCase(), Toast.LENGTH_SHORT).show();
//                    Toast.makeText(TitikInGameChoicesActivity.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
                final int finalI = i;
                mImageViews[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(SalitaInGameChoicesActivity.this, mTextViewExamples[finalI].getText().toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            dialog.show();
        }
    }
}
