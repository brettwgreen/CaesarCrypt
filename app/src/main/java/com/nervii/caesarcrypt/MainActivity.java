package com.nervii.caesarcrypt;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final String alpha = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private static String cipher = "";
    private static final String TAG = "CaesarCipher";
    private static final String CIPHER_TYPE = "CIPHER_TYPE";
    private static final String CIPHER_KEY = "CIPHER";
    private static final String CIPHER_PREFS = "CAESARCRYPT";
    private boolean spinnerInitialized = false;

    private void EncodeMessage(String s) {
        TextView encryptedMessage = (TextView)findViewById(R.id.encryptedMessage);
        encryptedMessage.setText(Cipher.EncryptMessage(s, alpha, cipher));
    }

    private void applyCipher() {
        TextView code = (TextView)findViewById(R.id.code);
        code.setText(cipher);
        TextView message = (TextView)findViewById(R.id.message);
        String s = message.getText().toString();
        if (s.length() > 0) {
            EncodeMessage(s);
        }

    }

    private void newCipher() {
        Spinner cipherType = (Spinner)findViewById(R.id.cipherType);
        if (cipherType.getSelectedItem().toString().equals("Caesar Shift")) {
            cipher = Cipher.CaesarShift(alpha);
        }
        else {
            cipher = Cipher.RandomizeCipher(alpha);
        }
        applyCipher();
    }

    private void saveCipher() {
        SharedPreferences mPrefs = getSharedPreferences(CIPHER_PREFS, MODE_APPEND);
        SharedPreferences.Editor ed = mPrefs.edit();
        ed.putString(CIPHER_KEY, cipher);
        Spinner cipherType = (Spinner)findViewById(R.id.cipherType);
        ed.putInt(CIPHER_TYPE, cipherType.getSelectedItemPosition());
        ed.apply();
    }

    private void initCipher() {
        SharedPreferences mPrefs = getSharedPreferences(CIPHER_PREFS, MODE_APPEND);
        if (mPrefs.contains(CIPHER_KEY)) {
            cipher = mPrefs.getString(CIPHER_KEY, "");
        }
        if (mPrefs.contains(CIPHER_TYPE)) {
            int id = mPrefs.getInt(CIPHER_TYPE, 0);
            Spinner cipherType = (Spinner)findViewById(R.id.cipherType);
            cipherType.setSelection(id);
        }
        if (cipher.length() > 0) {
            applyCipher();
        }
        else {
            newCipher();
        }
    }

    private void setupListeners(){
        EditText message = (EditText)findViewById(R.id.message);
        Spinner cipherType = (Spinner)findViewById(R.id.cipherType);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.cipherTypes, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        cipherType.setAdapter(adapter);
        cipherType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spinnerInitialized) {
                    newCipher();
                } else {
                    spinnerInitialized = true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        TextWatcher tw = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                EncodeMessage(s.toString());
            }
        };
        message.addTextChangedListener(tw);
        Button send = (Button)findViewById(R.id.sendBtn);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent smsIntent = new Intent(Intent.ACTION_VIEW);
                TextView encryptedMessage = (TextView) findViewById(R.id.encryptedMessage);
                smsIntent.setType("vnd.android-dir/mms-sms")
                        .putExtra("sms_body", encryptedMessage.getText().toString());
                startActivity(smsIntent);
            }
        });
        final ImageButton newCipher = (ImageButton)findViewById(R.id.newCipher);
        newCipher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newCipher();
            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupListeners();
        initCipher();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initCipher();
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveCipher();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
