package com.de_thes;

import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

/**
 * Created by tom on 26.09.16.
 */
public class DatabaseReader extends AsyncTask<Void, Void, Void> {

    MainActivity myMainActivity;

    public final String DATABASE_PATH;

    private BufferedReader bf;
    Context context;

    private InputStream iS_DB;

    public DatabaseReader(MainActivity myMainActivity, InputStream iS_DB, Context context) {
        this.iS_DB = iS_DB;
        this.context = context;
        this.myMainActivity = myMainActivity;

        DATABASE_PATH = "/data/data/" + context.getPackageName() + "/databases/" + MyDB_OpenHelper.DATABASE_NAME;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        myMainActivity.showProgressDialog();
    }

    @Override
    protected Void doInBackground(Void... params) {

        //myDB_openHelper = new MyDB_OpenHelper(context);

        //loadEnglish();
        //loadGerman();

        copyDB();

        return null;
    }


    public void copyDB() {
        try {
            copyInputStreamtoFile(iS_DB, DATABASE_PATH);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void copyInputStreamtoFile(InputStream iS, String newFileName) throws FileNotFoundException, UnsupportedEncodingException {

        FileOutputStream oS = new FileOutputStream(newFileName);

        try {
            bf = new BufferedReader(new InputStreamReader(iS, StandardCharsets.ISO_8859_1));

            byte[] buf = new byte[1024];
            int bytesRead;
            while ((bytesRead = iS.read(buf)) > 0) {
                oS.write(buf, 0, bytesRead);
            }
            oS.close();
            bf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
    public void loadFromTextFile(InputStream iS, boolean isEn) {

        String line;
        try {
            bf = new BufferedReader(new InputStreamReader(iS, StandardCharsets.ISO_8859_1));

            while ((line = bf.readLine()) != null) {
                String[] strArr = line.split("[|]");
                if (strArr.length < 4) continue;

                String text = strArr[1];
                int D_num = Integer.parseInt(strArr[3].substring(1));

                if (isEn) {
                    myDB_openHelper.insertEnglishRow(text, D_num);
                }
                else {
                    myDB_openHelper.insertGermanRow(text, D_num);
                }
            }

            bf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }*/

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);

        myMainActivity.setMyDB_openHelper(new MyDB_OpenHelper(context));

        myMainActivity.populateListView();
        myMainActivity.setBothListViewsClickable();
        myMainActivity.dismissProgressDialog();

    }
}
