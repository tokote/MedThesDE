package com.de_thes;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * Created by tom on 27.09.16.
 */
public class ThesaurusCursorAdapter extends CursorAdapter {

    public ThesaurusCursorAdapter(Context context, Cursor cursor) {
        super(context,cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.thesautus_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView textView = (TextView)view.findViewById(R.id.theText);

        String str = cursor.getString(cursor.getColumnIndexOrThrow(MyDB_OpenHelper.TEXT_COLUMN_NAME));
        textView.setText(str);
    }
}
