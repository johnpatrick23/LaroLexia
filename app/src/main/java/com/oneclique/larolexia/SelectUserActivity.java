package com.oneclique.larolexia;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ListView;

import com.oneclique.larolexia.LaroLexiaSQLite.LaroLexiaSQLite;

public class SelectUserActivity extends AppCompatActivityHelper {

    private ListView mListViewUsername;

    private ImageButton mImageButtonSelectedUsername;
    private ImageButton mImageButtonAddUsername;
    private ImageButton mImageButtonDeleteUsername;

    private LaroLexiaSQLite laroLexiaSQLite;

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


    }
}
