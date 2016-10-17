package com.de_thes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ShowTermActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_term);

        Intent myIntent = getIntent();
        String D_str = myIntent.getStringExtra(MyDB_OpenHelper.DNUM_COLUMN_NAME);
    }
}
