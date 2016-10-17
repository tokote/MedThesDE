
package com.de_thes;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    MyDB_OpenHelper myDB_openHelper;

    private ProgressDialog progressDialog = null;

    private static final String RAW_QUERY_ENGLISH =
            "SELECT " + MyDB_OpenHelper.ID_COLUMN_NAME + "," + MyDB_OpenHelper.TEXT_COLUMN_NAME +
                    " FROM " + MyDB_OpenHelper.TABLE_ENGLISH_NAME;
    private static final String RAW_QUERY_GERMAN =
            "SELECT " + MyDB_OpenHelper.ID_COLUMN_NAME + "," + MyDB_OpenHelper.TEXT_COLUMN_NAME +
                    " FROM " + MyDB_OpenHelper.TABLE_GERMAN_NAME;

    ListView listViewEn;
    ListView listViewDe;
    boolean enIsVisible;

    EditText searchEditText;
    ImageButton languageSwitchButton;

    boolean listViewEnPopulated = false;
    boolean listViewDePopulated = false;

    public SQLiteDatabase getDbWritable() {
        if (dbWritable == null) {
            dbWritable = myDB_openHelper.getWritableDatabase();
        }
        return dbWritable;
    }

    public void setDbWritable(SQLiteDatabase dbWritable) {
        this.dbWritable = dbWritable;
    }

    SQLiteDatabase dbWritable = null;

    DatabaseReader dbReader;



    public MainActivity() {

    }

    public void showProgressDialog() {
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.show();
    }

    public void dismissProgressDialog() {
        progressDialog.dismiss();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.include);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        getSupportActionBar().setDisplayShowTitleEnabled(true);

        progressDialog = new ProgressDialog(MainActivity.this);

        listViewEn = (ListView) findViewById(R.id.listViewEn);
        listViewDe = (ListView) findViewById(R.id.listViewDe);
        searchEditText = (EditText) findViewById(R.id.searchEditText);
        languageSwitchButton = (ImageButton) findViewById(R.id.languageSwitchButton);

        listViewEn.setVisibility(View.GONE);
        listViewDe.setVisibility(View.GONE);

        Resources resources;
        resources = getResources();

        int id_DB = resources.getIdentifier(getBaseContext().getPackageName() + ":raw/dimdi", null, null);

        InputStream iS_DB = resources.openRawResource(id_DB);

        dbReader = new DatabaseReader(this, iS_DB, getBaseContext());

        dbReader.execute();

        languageSwitchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listViewEnPopulated && listViewDePopulated) {
                    MainActivity.this.switchLanguages();
                }
            }
        });

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                boolean b = listViewEnPopulated && listViewDePopulated;
                if (listViewEnPopulated && listViewDePopulated) {
                    MainActivity.this.updateListViewPosition(s);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void setMyDB_openHelper(MyDB_OpenHelper myDB_openHelper) {
        this.myDB_openHelper = myDB_openHelper;
    }

    public void populateListView() {

        Cursor enCursor = myDB_openHelper.EnglishCursor("");
        Cursor deCursor = myDB_openHelper.GermanCursor("");

        listViewEn.setAdapter(new ThesaurusCursorAdapter(this, enCursor));
        listViewDe.setAdapter(new ThesaurusCursorAdapter(this, deCursor));

        if (enIsVisible) {
            listViewEn.setVisibility(View.VISIBLE);
        }
        else {
            listViewDe.setVisibility(View.VISIBLE);
        }

        listViewEnPopulated = true;
        listViewDePopulated = true;
    }

    private void updateListViewPosition(CharSequence s) {
        String newEditTextValue = s.toString();

        ListView lstToSearch = enIsVisible ? listViewEn : listViewDe;
        Cursor newCursor = myDB_openHelper.getListViewCursor(newEditTextValue ,enIsVisible);

        ThesaurusCursorAdapter tCA = (ThesaurusCursorAdapter) lstToSearch.getAdapter();
        tCA.changeCursor(newCursor);

    }
    public void switchLanguages() {

        enIsVisible = !enIsVisible;

        int flagResId = enIsVisible ? R.mipmap.en : R.mipmap.de;
        languageSwitchButton.setImageResource(flagResId);

        listViewDe.setVisibility(enIsVisible ? View.GONE : View.VISIBLE);
        listViewEn.setVisibility(enIsVisible ? View.VISIBLE : View.GONE);
    }


    public void setBothListViewsClickable() {
        setListViewClickable(listViewDe);
        setListViewClickable(listViewEn);
    }

    private void setListViewClickable(final ListView listView) {

        listView.setClickable(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                Intent showTermActivityIntent = new Intent(MainActivity.this, ShowTermActivity.class);

                Cursor cursor = (Cursor)listView.getAdapter().getItem(position);

                int dnumColumn = cursor.getColumnIndex(MyDB_OpenHelper.DNUM_COLUMN_NAME);
                String D_str = cursor.getString(dnumColumn);


                showTermActivityIntent.putExtra(MyDB_OpenHelper.DNUM_COLUMN_NAME, D_str);

                MainActivity.this.startActivity(showTermActivityIntent);
            }
        });

    }

    // Menu icons are inflated just as they were with actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }




}
