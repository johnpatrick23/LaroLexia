package com.oneclique.larolexia;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.oneclique.larolexia.LaroLexiaSQLite.LaroLexiaSQLiteModel.AchievementsModel;
import com.oneclique.larolexia.LaroLexiaSQLite.LaroLexiaSQLiteModel.QuestionModel;
import com.oneclique.larolexia.LaroLexiaSQLite.LaroLexiaSQLiteModel.UsersModel;
import com.oneclique.larolexia.model.PlayerStatisticModel;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class AppCompatActivityHelper extends AppCompatActivity implements Variables, RequestVariables  {
    protected static final int SPLASH_DISPLAY_LENGTH = 2000;

    protected String SelectedGameMode;
    protected String SelectedLevel;
    protected String SelectedLetter;

    private String mStringBasePath;
    private String mStringVideoPath;
    private String mStringImagePath;

    protected Typeface verdana_bold (){
        return Typeface.createFromAsset(getAssets(), "verdanab.ttf");
    }

    protected Typeface verdana (){
        return Typeface.createFromAsset(getAssets(), "verdana.ttf");
    }

    protected void fullScreen(){
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    protected void SnackBarMessage(String Message){
        View parentLayout = findViewById(android.R.id.content);
        Snackbar.make(parentLayout, Message, Snackbar.LENGTH_LONG).show();
    }

    protected void FullscreenDialog(Dialog dialog){
        Objects.requireNonNull(dialog.getWindow())
                .setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                        WindowManager.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    protected void requestPermission(Context context){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this,
                        Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this,
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_SECURE_SETTINGS) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.RECORD_AUDIO) &&
                    ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE) &&
                    ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.READ_EXTERNAL_STORAGE) &&
                    ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.CAMERA) &&
                    ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.WRITE_SECURE_SETTINGS) &&
                    ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.ACCESS_FINE_LOCATION) &&
                    ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.ACCESS_COARSE_LOCATION)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.RECORD_AUDIO,
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.CAMERA,
                                Manifest.permission.WRITE_SECURE_SETTINGS,
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION},
                        100);
                try {
                    createPath(context);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    String TAG = "LaroLexia";

    protected void createPath(Context context) throws IOException {
        final File dir = context.getExternalFilesDir(null);
        mStringBasePath = dir != null ? dir.getAbsolutePath() : null;
        mStringVideoPath = this.mStringBasePath + "/vid/";
        mStringImagePath = this.mStringBasePath + "/img/";
        Log.i(TAG, "mStringVideoPath: " + mStringVideoPath + " " + new File(mStringVideoPath).mkdir());
        Log.i(TAG, "mStringImagePath: " + mStringImagePath + " " + new File(mStringImagePath).mkdir());
    }

    public String getVidPathName(Context context) {
        final File dir = context.getExternalFilesDir(null);
        mStringBasePath = dir != null ? dir.getAbsolutePath() : null;
        mStringVideoPath = this.mStringBasePath + "/vid/";
        return mStringVideoPath;
    }

    public String getImgPathName(Context context){
        final File dir = context.getExternalFilesDir(null);
        mStringBasePath = dir != null ? dir.getAbsolutePath() : null;
        mStringImagePath = this.mStringBasePath + "/img/";
        return mStringImagePath;
    }

    public int GetResourceID(Context context, String resourceName, String resourceType){
        Resources resources = context.getResources();
        return resources.getIdentifier(resourceName, resourceType,
                context.getPackageName());
    }

    public Drawable GetDrawableResource(Context context, String resourceName){
        Resources resources = context.getResources();
        final int resourceId = resources.getIdentifier(resourceName, "drawable",
                context.getPackageName());
        return resources.getDrawable(resourceId);
    }

    public Uri GetRawResource(String resourceName){
        int rawResource = getResources()
                .getIdentifier(resourceName, "raw", getPackageName());
        return Uri.parse("android.resource://" + getPackageName() + "/" + rawResource);
    }

    public Uri GetDrawableResource(String resourceName){
        int drawableResource = getResources()
                .getIdentifier(resourceName, "drawable", getPackageName());
        return Uri.parse("android.resource://" + getPackageName() + "/" + drawableResource);
    }

    public class PlaySound{
        private MediaPlayer mediaPlayer;
        private Context context;
        public PlaySound(Context context){
            mediaPlayer = new MediaPlayer();
            this.context = context;
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        }

        public void play(String resourceName, boolean isLopping){
            Uri myUri = null;
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            try{
                myUri = GetRawResource(resourceName.toLowerCase().trim());
            }catch (Exception e){
                myUri = GetRawResource(resourceName.toLowerCase().trim() + "_");
            }
            try {
                try {
                    mediaPlayer.setDataSource(context, myUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    mediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mediaPlayer.setLooping(isLopping);
                mediaPlayer.start();
            }catch (Exception ex){
                Log.i(TAG, "play: NONE!");
            }
        }
        public void stop(){
            mediaPlayer.stop();
        }
    }

    public void playSound(Context context, int rawRes, int loop){
        SoundPool sounds;
        int sExplosion;
        sounds = new SoundPool(10, AudioManager.STREAM_MUSIC,0);
        sExplosion = sounds.load(context, rawRes, 1);
        sounds.play(sExplosion, 1.0f, 1.0f, 0, loop, 1.5f);
    }

    public void PlayerStatisticLog(PlayerStatisticModel playerStatisticModel){
        String sessionId = playerStatisticModel.getSessionId() == null ? "null" : playerStatisticModel.getSessionId();
        String username = playerStatisticModel.getUsername() == null ? "null" : playerStatisticModel.getUsername();
        String character = playerStatisticModel.getCharacter() == null  ? "null" : playerStatisticModel.getCharacter();
        String gameMode = playerStatisticModel.getGameMode() == null  ? "null" : playerStatisticModel.getGameMode();
        String level = playerStatisticModel.getLevel() == null  ? "null" : playerStatisticModel.getLevel();
        String letter = playerStatisticModel.getLetter() == null  ? "null" : playerStatisticModel.getLetter();
//        String numbersOfRetake = playerStatisticModel.getNumbersOfRetake() == null  ? "null" : playerStatisticModel.getNumbersOfRetake();
        Log.i(TAG, "sessionId: " + sessionId);
        Log.i(TAG, "username: " + username);
        Log.i(TAG, "character: " + character);
        Log.i(TAG, "gameMode: " + gameMode);
        Log.i(TAG, "level: " + level);
        Log.i(TAG, "letter: " + letter);
//        Log.i(TAG, "numbersOfRetake: " + numbersOfRetake);
    }

    public void UserModelLog(UsersModel usersModel){
        String getA_id = usersModel.getA_id() == null ? "null" : usersModel.getA_id();
        String getA_character = usersModel.getA_character() == null ? "null" : usersModel.getA_character();
        String getA_last_used = usersModel.getA_last_used() == null  ? "null" : usersModel.getA_last_used();
        String getA_stars = usersModel.getA_stars() == null  ? "null" : usersModel.getA_stars();
        String getA_username = usersModel.getA_username() == null  ? "null" : usersModel.getA_username();
        String getA_new_user = usersModel.getA_new_user() == null  ? "null" : usersModel.getA_new_user();

        Log.i(TAG, "getA_id: " + getA_id);
        Log.i(TAG, "getA_character: " + getA_character);
        Log.i(TAG, "getA_last_used: " + getA_last_used);
        Log.i(TAG, "getA_stars: " + getA_stars);
        Log.i(TAG, "getA_username: " + getA_username);
        Log.i(TAG, "getA_new_user: " + getA_new_user);
    }

    public void AchievementsModelLog(AchievementsModel achievementsModel){
        String getA_id = achievementsModel.getA_id() == null ? "null" : achievementsModel.getA_id();
        String getA_date_time_used = achievementsModel.getA_date_time_used() == null ? "null" : achievementsModel.getA_date_time_used();
        String getA_gameMode = achievementsModel.getA_gameMode() == null  ? "null" : achievementsModel.getA_gameMode();
        String getA_letter = achievementsModel.getA_letter() == null  ? "null" : achievementsModel.getA_letter();
        String getA_star = achievementsModel.getA_star() == null  ? "null" : achievementsModel.getA_star();
        String getA_time = achievementsModel.getA_time() == null  ? "null" : achievementsModel.getA_time();
        String getA_level = achievementsModel.getA_level() == null  ? "null" : achievementsModel.getA_level();
        String getA_username = achievementsModel.getA_username() == null  ? "null" : achievementsModel.getA_username();

        Log.i(TAG, "getA_id: " + getA_id);
        Log.i(TAG, "getA_date_time_used: " + getA_date_time_used);
        Log.i(TAG, "getA_gameMode: " + getA_gameMode);
        Log.i(TAG, "getA_letter: " + getA_letter);
        Log.i(TAG, "getA_star: " + getA_star);
        Log.i(TAG, "getA_time: " + getA_time);
        Log.i(TAG, "getA_level: " + getA_level);
        Log.i(TAG, "getA_username: " + getA_username);
    }

    public void QuestionModelLog(QuestionModel questionModel){
        String getA_id = questionModel.getA_id() == null ? "null" : questionModel.getA_id();
        String getA_question = questionModel.getA_question() == null ? "null" : questionModel.getA_question();
        String getA_gameMode = questionModel.getA_gameMode() == null  ? "null" : questionModel.getA_gameMode();
        String getA_letter = questionModel.getA_letter() == null  ? "null" : questionModel.getA_letter();
        String getA_instruction = questionModel.getA_instruction() == null  ? "null" : questionModel.getA_instruction();
        String getA_answer = questionModel.getA_answer() == null  ? "null" : questionModel.getA_answer();
        String getA_level = questionModel.getA_level() == null  ? "null" : questionModel.getA_level();
        String getA_question_code = questionModel.getA_question_code() == null  ? "null" : questionModel.getA_question_code();
        String getA_choices = questionModel.getA_choices() == null  ? "null" : questionModel.getA_choices();

        Log.i(TAG, "getA_id: " + getA_id);
        Log.i(TAG, "getA_question: " + getA_question);
        Log.i(TAG, "getA_gameMode: " + getA_gameMode);
        Log.i(TAG, "getA_letter: " + getA_letter);
        Log.i(TAG, "getA_instruction: " + getA_instruction);
        Log.i(TAG, "getA_answer: " + getA_answer);
        Log.i(TAG, "getA_level: " + getA_level);
        Log.i(TAG, "getA_question_code: " + getA_question_code);
        Log.i(TAG, "getA_choices: " + getA_choices);
    }

    public class BasagPalayokCorrect{

        public TextView mTextViewCorrectionStatus;
        public TextView mTextViewCorrectWord;
        public Button mButtonCorrectNext;
        public Dialog dialog;

        public BasagPalayokCorrect(Context context){
            dialog = new Dialog(context);
            dialog.setContentView(R.layout.dialog_basagpalayok_correct);
            FullscreenDialog(dialog);
            mTextViewCorrectWord = dialog.findViewById(R.id.mTextViewCorrectWord);
            mButtonCorrectNext = dialog.findViewById(R.id.mButtonCorrectNext);
            mTextViewCorrectionStatus = dialog.findViewById(R.id.mTextViewCorrectionStatus);

            mButtonCorrectNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.cancel();
                }
            });
        }
    }

    public class LuksongTinikCorrect{

        public ImageView mImageViewCorrectImage;
        public TextView mTextViewCorrectWord;
        public Button mButtonCorrectNext;
        public TextView mTextViewCorrectionStatus;
        public Dialog dialog;

        public LuksongTinikCorrect(Context context){
            dialog = new Dialog(context);
            dialog.setContentView(R.layout.dialog_luksongtinik_correct);
            FullscreenDialog(dialog);
            mImageViewCorrectImage = dialog.findViewById(R.id.mImageViewCorrectImage);
            mTextViewCorrectWord = dialog.findViewById(R.id.mTextViewCorrectWord);
            mButtonCorrectNext = dialog.findViewById(R.id.mButtonCorrectNext);
            mTextViewCorrectionStatus = dialog.findViewById(R.id.mTextViewCorrectionStatus);

            mButtonCorrectNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.cancel();
                }
            });
        }
    }

    class CorrectAnswer{
        Dialog dialog;
        ImageView mImageViewCorrectImage;
        TextView mTextViewCorrectLetter;
        Button mButtonCorrectNext;
        TextView mTextViewCorrectionStatus;

        CorrectAnswer(Context context){
            dialog = new Dialog(context);
            dialog.setContentView(R.layout.dialog_correct);
            FullscreenDialog(dialog);
            mTextViewCorrectionStatus = dialog.findViewById(R.id.mTextViewCorrectionStatus);
            mButtonCorrectNext = dialog.findViewById(R.id.mButtonCorrectNext);
            mImageViewCorrectImage = dialog.findViewById(R.id.mImageViewCorrectImage);
            mTextViewCorrectLetter = dialog.findViewById(R.id.mTextViewCorrectLetter);

            mButtonCorrectNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.cancel();
                }
            });
        }

        void show(Drawable drawable, String correctLetter){
            this.mImageViewCorrectImage.setImageDrawable(drawable);
            mTextViewCorrectLetter.setText(String.valueOf(correctLetter.charAt(0)));
            dialog.show();
        }
    }

    class FinishedGame{
        Dialog dialog;
        TextView mTextViewFinishedGameMode;
        TextView mTextViewPointsGameMode;
        ImageView mImageViewFinishedGameStars[];
        Button mButtonFinishedPlayAgain;

        FinishedGame(Context context){
            dialog = new Dialog(context);
            FullscreenDialog(dialog);
            dialog.setContentView(R.layout.dialog_finished_palosebo);
            dialog.setCancelable(false);

            mImageViewFinishedGameStars = new ImageView[]{
                    dialog.findViewById(R.id.mImageViewFinishedGameStar1),
                    dialog.findViewById(R.id.mImageViewFinishedGameStar2),
                    dialog.findViewById(R.id.mImageViewFinishedGameStar3)
            };

            mTextViewFinishedGameMode = dialog.findViewById(R.id.mTextViewFinishedGameMode);
            mTextViewPointsGameMode = dialog.findViewById(R.id.mTextViewPointsGameMode);
            mButtonFinishedPlayAgain = dialog.findViewById(R.id.mButtonFinishedPlayAgain);
        }

    }
}
