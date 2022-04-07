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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final String AUTHORITY = "com.example.phonebook.mycontentprovider";
    private static final String TABLE_CONTACTS = "contacts";
    public static final String COLUMN_FNAME = "fName";
    public static final String COLUMN_LNAME = "lName";
    public static final String COLUMN_PHONENUM = "phoneNumber";
    EditText fnameBox, lnameBox;
    TextView phnNumBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fnameBox = findViewById(R.id.fname);
        lnameBox = findViewById(R.id.lname);
        phnNumBox = findViewById(R.id.phone_number);
    }

    public void find(View view) {
        Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_CONTACTS);
        String[] projection = {COLUMN_FNAME, COLUMN_LNAME, COLUMN_PHONENUM};
        // To get the list of items from MyContentProvider, use ContentResolver.query()
        ContentResolver crInstance = getContentResolver();
        Cursor cursor = crInstance.query(CONTENT_URI, projection,
                null, null, null);

        // This logic is kind of awful...
        boolean b = true;
//        Log.d("cursor", "find: cursor.getCount() = "+cursor.getCount());
        if (!cursor.moveToFirst()) {
            phnNumBox.setText("There are no contacts in the phone book.");
            b = false;
        }
        while (b) {
            if ((cursor.getString(0).equals(fnameBox.getText().toString())) &&
                    (cursor.getString(1).equals(lnameBox.getText().toString()))) {
                phnNumBox.setText(cursor.getString(2));
                break;
            }
            else if (!cursor.moveToNext()) {
                phnNumBox.setText("Contact not found.");
                break;
            }
        }
        cursor.close();
    }

    public void makeCall(View view) {
        String PN = "";
        Log.d("PN", "PN.length"+phnNumBox.getText().toString().length());
        if (phnNumBox.getText().toString().length() == 10) {
            Log.d("PN", "areaCode: "+phnNumBox.getText().toString().substring(0, 3));
            PN = phnNumBox.getText().toString().substring(0, 3) +
                    phnNumBox.getText().toString().substring(4, 7) +
                    phnNumBox.getText().toString().substring(7);
        }
        else if (phnNumBox.getText().toString().length() == 8) {
            PN = phnNumBox.getText().toString().substring(0, 3) +
                    phnNumBox.getText().toString().substring(4, 8);
        }
        Log.d("MakeCall", "makeCall: PhoneNumber= "+PN);

        if (!PN.equals("")) {
            try {
                Uri uri = Uri.parse("tel:" + PN);
                Intent intent = new Intent(Intent.ACTION_DIAL, uri);
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(this, "Application failed", Toast.LENGTH_LONG).show();
            }
        }
        else
            Toast.makeText(this, "Find an existing contact", Toast.LENGTH_LONG).show();
    }
}
