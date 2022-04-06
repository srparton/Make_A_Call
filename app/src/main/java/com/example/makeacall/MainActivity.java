package com.example.makeacall;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final String AUTHORITY = "com.example.phonebook.MyContentProvider";
    private static final String TABLE_CONTACTS = "contacts";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_FNAME = "fname";
    public static final String COLUMN_LNAME = "lname";
    public static final String COLUMN_PHONENUM = "phonenum";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/"+ TABLE_CONTACTS);
        String[] projection = {COLUMN_ID, COLUMN_FNAME,
                COLUMN_LNAME, COLUMN_PHONENUM};
        //To get a list of the items from the MyContentProvider, we use ContentResolver.query()
        ContentResolver crInstance = getContentResolver();
        // the arguments are (1) uri to the table,
        // (2) selection criteria, (3) selection criteria, (4) sort of the order of return
        Cursor cursor = crInstance.query(CONTENT_URI,projection,null,null,null);
        TextView phoneNum = findViewById(R.id.phone_number);
        String s = "The list of item(s) are:\n\n";
        if(!cursor.moveToFirst())
            s = "no result to display";
        else
            do{
                s += String.format("%-10s\t%-10s\t%-10s\n",cursor.getString(0),
                        cursor.getString(1), cursor.getString(2));
            }while (cursor.moveToNext());
        phoneNum.setText(s);
    }
/*
    public void makeCall(View view) {
        if(){
            String phone = et1.getText().toString() + et2.getText().toString() + et3.getText().toString();

            try {
                Uri uri = Uri.parse("tel:" + phone);
                Intent intent = new Intent(Intent.ACTION_DIAL, uri);
                Log.i("Make A Call app", phone);
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(getApplicationContext(), "Application failed", Toast.LENGTH_LONG).show();
            }
        }
    }*/
}
