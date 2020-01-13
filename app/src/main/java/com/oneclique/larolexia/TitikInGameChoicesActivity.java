package com.oneclique.larolexia;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
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
import com.oneclique.larolexia.LaroLexiaSQLite.LaroLexiaSQLiteModel.LettersModel;
import com.oneclique.larolexia.LaroLexiaSQLite.SQLITE_VARIABLES;
import com.oneclique.larolexia.adapter.TitikGridViewAdapter;
import com.oneclique.larolexia.helper.LogicHelper;
import com.oneclique.larolexia.model.PlayerStatisticModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TitikInGameChoicesActivity extends AppCompatActivityHelper {

    private GridView mGridViewLetters;

    private LaroLexiaSQLite laroLexiaSQLite;

    private ReadLetter readLetter;

    private LettersModel lettersModel;

    private PlayerStatisticModel playerStatisticModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fullScreen();
        setContentView(R.layout.activity_titik_in_game_choices);

        Intent intent = getIntent();
        playerStatisticModel = (PlayerStatisticModel) Objects.requireNonNull(intent.getExtras())
                .getSerializable(PLAYER_STATISTICS);

        mGridViewLetters = findViewById(R.id.mGridViewLetters);

        List<String> letterList = new ArrayList<>();
        lettersModel = new LettersModel();
        laroLexiaSQLite = new LaroLexiaSQLite(TitikInGameChoicesActivity.this);
        laroLexiaSQLite.createDatabase();

        Cursor lettersCursor = laroLexiaSQLite.executeReader(
                "SELECT * FROM " + SQLITE_VARIABLES.Table_Letters.DB_TABLE_NAME + ";");

        if(lettersCursor.getCount() != 0){
            //Toast.makeText(TitikInGameChoicesActivity.this, String.valueOf(lettersCursor.getCount()), Toast.LENGTH_LONG).show();
            while(lettersCursor.moveToNext()){
                letterList.add(lettersCursor.getString(lettersCursor.getColumnIndex(
                        SQLITE_VARIABLES.Table_Letters.DB_COL_LETTER)));
            }

            mGridViewLetters.setAdapter(new TitikGridViewAdapter(TitikInGameChoicesActivity.this, letterList));

            mGridViewLetters.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    try{
                        String selectedLetter = ((TextView)view.findViewById(R.id.mTextViewLetter)).getText().toString();
                        Cursor lettersCursor = laroLexiaSQLite.executeReader(
                                "SELECT * FROM " + SQLITE_VARIABLES.Table_Letters.DB_TABLE_NAME + " " +
                                        "WHERE " + SQLITE_VARIABLES.Table_Letters.DB_COL_LETTER + " = " +
                                        "'" + selectedLetter.toUpperCase() + "';");
                        if(lettersCursor.getCount() != 0){
                            while(lettersCursor.moveToNext()){
                                lettersModel.setA_letter(lettersCursor.getString(lettersCursor.getColumnIndex(
                                        SQLITE_VARIABLES.Table_Letters.DB_COL_LETTER)));
                                lettersModel.setA_examples_images(lettersCursor.getString(lettersCursor.getColumnIndex(
                                        SQLITE_VARIABLES.Table_Letters.DB_COL_EXAMPLES_IMAGES)));
                                lettersModel.setA_examples_text(lettersCursor.getString(lettersCursor.getColumnIndex(
                                        SQLITE_VARIABLES.Table_Letters.DB_COL_EXAMPLES_TEXT)));
                                lettersModel.setA_instruction(lettersCursor.getString(lettersCursor.getColumnIndex(
                                        SQLITE_VARIABLES.Table_Letters.DB_COL_INSTRUCTION)));
                            }
                            readLetter.show(lettersModel);
                        }
                    }catch (Exception e){
                        Toast.makeText(TitikInGameChoicesActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
            readLetter = new ReadLetter(TitikInGameChoicesActivity.this, playerStatisticModel, lettersModel);

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

    private class ReadLetter{
        private Dialog dialog;

        private TextView mTextViewInstructionToBeRead;

        private Button mButtonMagpatuloy;
        private Button mButtonBumalik;
        private Button mButtonPakinggan;
        private TextView[] mTextViewExamples;
        private ImageView[] mImageViews;

        ReadLetter(Context context, final PlayerStatisticModel playerStatisticModel, final LettersModel lettersModel){
            dialog = new Dialog(context);
            dialog.setContentView(R.layout.dialog_read_the_letter);
            final PlaySound playSound = new PlaySound(TitikInGameChoicesActivity.this);
            FullscreenDialog(dialog);

            mTextViewInstructionToBeRead = dialog.findViewById(R.id.mTextViewInstructionToBeRead);
            mButtonPakinggan = dialog.findViewById(R.id.mButtonPakinggan);
            mTextViewExamples = new TextView[]{
                    dialog.findViewById(R.id.mTextViewExample1),
                    dialog.findViewById(R.id.mTextViewExample2),
                    dialog.findViewById(R.id.mTextViewExample3)};

            mButtonPakinggan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try{
                        playSound.stop();
                    }catch (Exception ignore){

                    }
                    Log.i(TAG, "lettersModel.getA_letter(): " + lettersModel.getA_letter());
                    playSound.play(lettersModel.getA_letter(), false);
                }
            });

            mImageViews = new ImageView[]{
                    dialog.findViewById(R.id.mImageViewExample1),
                    dialog.findViewById(R.id.mImageViewExample2),
                    dialog.findViewById(R.id.mImageViewExample3)};

            mButtonMagpatuloy = dialog.findViewById(R.id.mButtonMagpatuloy);
            mButtonBumalik = dialog.findViewById(R.id.mButtonBumalik);

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
                            intent = new Intent(TitikInGameChoicesActivity.this, PaloseboActivity.class);
                            break;
                        }
                        case "2":{
                            intent = new Intent(TitikInGameChoicesActivity.this, BasagPalayokActivity.class);
                            break;
                        }
                        case "3":{
                            intent = new Intent(TitikInGameChoicesActivity.this, LuksongTinikActivity.class);
                            break;
                        }
                    }
                    if (intent != null) {
                        intent.putExtra(CHOSEN_LETTER, lettersModel);
                        playerStatisticModel.setLetter(lettersModel.getA_letter());
                        intent.putExtra(PLAYER_STATISTICS, playerStatisticModel);
                        PlayerStatisticLog(playerStatisticModel);
                        startActivity(intent);
                        dialog.cancel();
                    }

                }
            });

        }

        void show(LettersModel lettersModel){

            mTextViewInstructionToBeRead.setText(Html.fromHtml(lettersModel.getA_instruction()));

            List<String> exampleTexts = LogicHelper.choiceReBuilder(lettersModel.getA_examples_text());
            List<String> exampleImages = LogicHelper.choiceReBuilder(lettersModel.getA_examples_images());

            for(int i = 0; i < exampleTexts.size(); i++){
                mTextViewExamples[i].setText(Html.fromHtml(exampleTexts.get(i)));
                try{
                    mImageViews[i].setImageDrawable(GetDrawableResource(TitikInGameChoicesActivity.this, exampleImages.get(i).toLowerCase()));
                }catch (Exception ignored){
//                    Toast.makeText(TitikInGameChoicesActivity.this, exampleTexts.get(i).toLowerCase(), Toast.LENGTH_SHORT).show();
//                    Toast.makeText(TitikInGameChoicesActivity.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
                final int finalI = i;
                mImageViews[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(TitikInGameChoicesActivity.this, mTextViewExamples[finalI].getText().toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            dialog.show();
        }
    }

}
