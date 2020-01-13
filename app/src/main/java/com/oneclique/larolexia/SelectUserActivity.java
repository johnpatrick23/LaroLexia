package com.oneclique.larolexia;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.oneclique.larolexia.LaroLexiaSQLite.LaroLexiaSQLite;
import com.oneclique.larolexia.LaroLexiaSQLite.LaroLexiaSQLiteModel.UsersModel;
import com.oneclique.larolexia.LaroLexiaSQLite.SQLITE_VARIABLES;
import com.oneclique.larolexia.LaroLexiaSQLite.SQLITE_VARIABLES.*;
import com.oneclique.larolexia.adapter.UsernameListViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class SelectUserActivity extends AppCompatActivityHelper {

    private ListView mListViewUsername;

    private ImageButton mImageButtonSelectedUsername;
    private ImageButton mImageButtonAddUsername;
    private ImageButton mImageButtonDeleteUsername;

    private LaroLexiaSQLite laroLexiaSQLite;


    private List<UsersModel> usernameModelList;

    private UsernameListViewAdapter usernameListViewAdapter;

    private int selectedPlayerPosition = -1;
    private String selectedPlayer = "";


    private String selectedCharacter = "JUAN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fullScreen();
        setContentView(R.layout.activity_select_user);

        laroLexiaSQLite = new LaroLexiaSQLite(SelectUserActivity.this);

        mListViewUsername = findViewById(R.id.mListViewUsername);
        mImageButtonSelectedUsername = findViewById(R.id.mImageButtonSelectedUsername);
        mImageButtonAddUsername = findViewById(R.id.mImageButtonAddUsername);
        mImageButtonDeleteUsername = findViewById(R.id.mImageButtonDeleteUsername);

        laroLexiaSQLite.createDatabase();

        fillUsernames(laroLexiaSQLite, mListViewUsername,
                mImageButtonDeleteUsername, mImageButtonSelectedUsername, SelectUserActivity.this);

        mListViewUsername.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TextView mTextViewSelected = view.findViewById(R.id.mTextViewUsernameItem);
                for (int j = 0; j < parent.getChildCount(); j++){
                    TextView mTextView = parent.getChildAt(j).findViewById(R.id.mTextViewUsernameItem);
                    mTextView.setBackground(mTextViewSelected.getBackground());
                    mTextViewSelected.setBackground(null);
                }
                mTextViewSelected.setBackgroundColor(Color.rgb(92, 196, 205));
                selectedPlayerPosition = position;
                selectedPlayer = mTextViewSelected.getText().toString();
            }
        });

        mImageButtonSelectedUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                UsersModel usersModel = new UsersModel();
                Cursor cursor = laroLexiaSQLite.executeReader(
                        "SELECT * FROM " + Table_Users.DB_TABLE_NAME +
                                " where " + Table_Users.DB_COL_USERNAME + " = '" + selectedPlayer + "';");

                if(cursor.getCount() != 0){
                    while(cursor.moveToNext()){
                        usersModel.setA_id(cursor.getString(cursor.getColumnIndex(Table_Users.DB_COL_ID)));
                        usersModel.setA_username(cursor.getString(cursor.getColumnIndex(Table_Users.DB_COL_USERNAME)));
                        usersModel.setA_character(cursor.getString(cursor.getColumnIndex(Table_Users.DB_COL_CHARACTER)));
                        usersModel.setA_last_used(cursor.getString(cursor.getColumnIndex(Table_Users.DB_COL_LAST_USED)));
                        usersModel.setA_stars(cursor.getString(cursor.getColumnIndex(Table_Users.DB_COL_STARS)));
                        usersModel.setA_new_user(cursor.getString(cursor.getColumnIndex(Table_Users.DB_COL_NEW_USER)));
                    }
                }
                intent.putExtra(SELECTED_USERNAME, usersModel);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });

        mImageButtonDeleteUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    laroLexiaSQLite.executeWriter(
                            "DELETE FROM " + Table_Users.DB_TABLE_NAME +
                                    " WHERE " + Table_Users.DB_COL_USERNAME +
                                    " = '" + selectedPlayer + "';");
                    laroLexiaSQLite.executeWriter(
                            "DELETE FROM " + Table_Achievements.DB_TABLE_NAME +
                                    " WHERE " + Table_Achievements.DB_COL_USERNAME +
                                    " = '" + selectedPlayer + "';");
                    Toast.makeText(SelectUserActivity.this, "Deleted!", Toast.LENGTH_LONG).show();
                    fillUsernames( laroLexiaSQLite, mListViewUsername, mImageButtonDeleteUsername,
                            mImageButtonSelectedUsername, SelectUserActivity.this);
                }catch (SQLiteException ex){
                    Toast.makeText(SelectUserActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

        mImageButtonAddUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog addUser = new Dialog(SelectUserActivity.this);
                addUser.setContentView(R.layout.dialog_add_username);
                FullscreenDialog(addUser);

                final EditText mEditTextDialogUsername = addUser.findViewById(R.id.mEditTextDialogUsername);

                final ImageButton mButtonJuan = addUser.findViewById(R.id.mImageButtonJuan);
                final ImageButton mButtonJuana = addUser.findViewById(R.id.mImageButtonJuana);

                final Button mButtonDialogAddUsername = addUser.findViewById(R.id.mButtonDialogAddUsername);

                mEditTextDialogUsername.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if(mEditTextDialogUsername.getText().length() == 0){
                            mButtonDialogAddUsername.setEnabled(false);
                        }else {
                            mButtonDialogAddUsername.setEnabled(true);
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

                mButtonJuan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectedCharacter = "JUAN";
                    }
                });

                mButtonJuana.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectedCharacter = "JUANA";
                    }
                });

                mButtonDialogAddUsername.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!mEditTextDialogUsername.getText().toString().trim().equals("")){
                            try{
                                laroLexiaSQLite.executeWriter(
                                        "INSERT INTO tbl_users (a_id, a_username, a_character) " +
                                                "VALUES (" +
                                                "'" + System.currentTimeMillis() + "', " +
                                                "'" + mEditTextDialogUsername.getText().toString().trim() + "', " +
                                                "'" + selectedCharacter + "');");
                                Toast.makeText(SelectUserActivity.this, "Naidagdag na ang iyong pangalan!", Toast.LENGTH_LONG).show();
                                addUser.cancel();
                            }catch (SQLiteException ex){
                                Log.i(TAG, "ex.getMessage(): " + ex.getMessage());
                                Toast.makeText(SelectUserActivity.this, "Mayroon nang ganitong pangalan!", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });

                addUser.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        selectedCharacter = "JUAN";
                        mEditTextDialogUsername.setText("");
                        mButtonDialogAddUsername.setEnabled(false);
                        fillUsernames( laroLexiaSQLite, mListViewUsername,
                                mImageButtonDeleteUsername, mImageButtonSelectedUsername, SelectUserActivity.this);
                    }
                });
                addUser.show();
            }
        });
    }

    private void fillUsernames(LaroLexiaSQLite laroLexiaSQLite,
                               ListView mListViewUsername,
                               ImageButton mButtonDeleteSelectedUsername,
                               ImageButton mButtonSelectedUsername,
                               Activity activity){
        Cursor usernamesCursor = laroLexiaSQLite.executeReader(
                "select " + SQLITE_VARIABLES.Table_Users.DB_COL_USERNAME + " from " +
                        SQLITE_VARIABLES.Table_Users.DB_TABLE_NAME);

        List<String> usernameList = new ArrayList<>();


        if(usernamesCursor.getCount() != 0){
            while(usernamesCursor.moveToNext()){
                usernameList.add(usernamesCursor.getString(usernamesCursor.getColumnIndex(
                        SQLITE_VARIABLES.Table_Users.DB_COL_USERNAME)));
            }
            mListViewUsername.setAdapter(new UsernameListViewAdapter(activity, usernameList));

            mButtonDeleteSelectedUsername.setEnabled(true);
            mButtonSelectedUsername.setEnabled(true);
        }
        else {
            mListViewUsername.setAdapter(new UsernameListViewAdapter(activity, usernameList));
            mButtonDeleteSelectedUsername.setEnabled(false);
            mButtonSelectedUsername.setEnabled(false);
        }
    }

    @Override
    public void onBackPressed() {
       /* if(usernameModelList.size() == 0){

        }

        if(selectedPlayerPosition != -1){*/
            Intent intent = new Intent();
            UsersModel usersModel = new UsersModel();
            Cursor cursor = laroLexiaSQLite.executeReader(
                    "SELECT * FROM " + Table_Users.DB_TABLE_NAME +
                            " where " + Table_Users.DB_COL_USERNAME + " = '" + selectedPlayer + "';");

            if(cursor.getCount() != 0){
                while(cursor.moveToNext()){
                    usersModel.setA_id(cursor.getString(cursor.getColumnIndex(Table_Users.DB_COL_ID)));
                    usersModel.setA_username(cursor.getString(cursor.getColumnIndex(Table_Users.DB_COL_USERNAME)));
                    usersModel.setA_character(cursor.getString(cursor.getColumnIndex(Table_Users.DB_COL_CHARACTER)));
                    usersModel.setA_last_used(cursor.getString(cursor.getColumnIndex(Table_Users.DB_COL_LAST_USED)));
                    usersModel.setA_stars(cursor.getString(cursor.getColumnIndex(Table_Users.DB_COL_STARS)));
                    usersModel.setA_new_user(cursor.getString(cursor.getColumnIndex(Table_Users.DB_COL_NEW_USER)));
                }
            }
            intent.putExtra(SELECTED_USERNAME, usersModel);
            setResult(Activity.RESULT_OK, intent);
            finish();
        //}
    }

}
