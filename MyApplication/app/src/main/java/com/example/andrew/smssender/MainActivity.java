package com.example.andrew.smssender;

/*
    EncryptedSmsSender - is an application for encrypting/decrypting text
	according to "Caesar" cipher. The application also can send the text
	via SMS.
    Copyright (C) 2016  Andrii Iarkovoi

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.

    You can also contact me via e-mail vereszp@gmail.com
 */

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    RadioButton rbnEncrypt, rbnDecrypt;
    Button btnInvert, btnProcess, btnSend;
    EditText etInput, etOutput, etKey;
    private static final String TAG = "mylog";
    String input, output;
    int shift = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate()");

        rbnDecrypt = (RadioButton) findViewById(R.id.rbnDecrypt);
        rbnEncrypt = (RadioButton) findViewById(R.id.rbnEncrypt);
        rbnEncrypt.toggle();
        btnInvert = (Button) findViewById(R.id.btnInvert);
        btnProcess = (Button) findViewById(R.id.btnProcess);
        btnSend = (Button) findViewById(R.id.btnSend);
        etInput = (EditText) findViewById(R.id.etInput);
        etOutput = (EditText) findViewById(R.id.etOutpuy);
        etKey = (EditText) findViewById(R.id.etKey);

        btnInvert.setOnClickListener(this);
        btnProcess.setOnClickListener(this);
        btnSend.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.btnInvert:
                Log.d(TAG, "btnInvert");
                output = etOutput.getText().toString();
                etInput.setText(output);
                etOutput.setText("");
                break;
            case R.id.btnProcess:
                Log.d(TAG, "btnProcess");
                hideSoftKeyboard(this);
                if (TextUtils.isEmpty(etKey.getText().toString()))
                {
                    Toast.makeText(this, "Fill the Key field", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!TextUtils.isDigitsOnly(etKey.getText().toString()))
                {
                    Toast.makeText(this, "The Key must be a digit", Toast.LENGTH_SHORT).show();
                    return;
                }
                shift = Integer.parseInt(etKey.getText().toString());
                input = etInput.getText().toString();
                if (rbnEncrypt.isChecked())
                    output = crypt(input, shift);
                else
                    output = crypt(input, -shift);
                etOutput.setText(output);
                break;
            case R.id.btnSend:
                Log.d(TAG, "btnSend");
                Intent smsIntent = new Intent(android.content.Intent.ACTION_VIEW);
                smsIntent.setType("vnd.android-dir/mms-sms");
                smsIntent.putExtra("address","");
                smsIntent.putExtra("sms_body",etOutput.getText().toString());
                startActivity(smsIntent);
                break;
            default:
                Log.d(TAG, "CaseDefault");
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // TODO Auto-generated method stub
        menu.add(0, 1, 0, "About...");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        switch (String.valueOf(item.getItemId()))
        {
            case "1":
                Intent aboutIntent = new Intent(this, AboutActivity.class);
                startActivity(aboutIntent);
                break;
            default:
                //etOutput.setText("" + String.valueOf(item.getItemId()));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public static String crypt(String iString, int ishift) {
        StringBuffer newString = new StringBuffer (iString);
        int shift = ishift % 95;
        char c;
        for (int i = 0; i < newString.length(); ++i)
        {
            c = newString.charAt(i);
            if (c >= 32 && c <= 126) {
                c = (char) (c + shift);
                if (c < 32) {
                    c += 95;
                } else if (c > 126) {
                    c -= 95;
                }
                newString.setCharAt(i, c);
            }
        }
        return newString.toString();
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

}
