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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private RadioButton rbnEncrypt;
    private EditText etInput, etKey;
    private TextView etOutput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rbnEncrypt = (RadioButton) findViewById(R.id.rbnEncrypt);
        rbnEncrypt.toggle();
        Button btnInvert = (Button) findViewById(R.id.btnInvert);
        Button btnProcess = (Button) findViewById(R.id.btnProcess);
        Button btnSend = (Button) findViewById(R.id.btnSend);
        etInput = (EditText) findViewById(R.id.etInput);
        etOutput = (TextView) findViewById(R.id.etOutput);
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
                String output = etOutput.getText().toString();
                etInput.setText(output);
                etOutput.setText("");
                break;
            case R.id.btnProcess:
                hideSoftKeyboard(this);
                if (etKey.getText().toString().isEmpty())
                {
                    Toast.makeText(this, "Fill the Key field", Toast.LENGTH_SHORT).show();
                    return;
                }
                int shift = Integer.parseInt(etKey.getText().toString());
                String input = etInput.getText().toString();
                if (rbnEncrypt.isChecked())
                    output = crypt(input, shift);
                else
                    output = crypt(input, -shift);
                etOutput.setText(output);
                break;
            case R.id.btnSend:
                Intent smsIntent = new Intent(android.content.Intent.ACTION_VIEW);
                smsIntent.setType("vnd.android-dir/mms-sms");
                smsIntent.putExtra("address","");
                smsIntent.putExtra("sms_body",etOutput.getText().toString());
                startActivity(smsIntent);
                break;
            default:
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
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private static String crypt(String iString, int ishift) {
        StringBuilder newString = new StringBuilder(iString);
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

    private static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

}
