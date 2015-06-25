package com.nervii.caesarcrypt;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final String alpha = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private String randomCipher = "";
    private static final String TAG = "CaesarCipher";

    private void EncodeMessage(Editable s) {
        TextView encryptedMessage = (TextView)findViewById(R.id.encryptedMessage);
        encryptedMessage.setText(Cipher.EncryptMessage(s.toString(), alpha, randomCipher));
    }
    private void NewCipher() {
        TextView code = (TextView)findViewById(R.id.code);
        randomCipher = Cipher.RandomizeCipher(alpha);
        code.setText(randomCipher);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EditText message = (EditText)findViewById(R.id.message);
        TextWatcher tw = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                EncodeMessage(s);
            }
        };
        message.addTextChangedListener(tw);
        Button send = (Button)findViewById(R.id.sendBtn);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent smsIntent = new Intent(Intent.ACTION_VIEW);
                TextView encryptedMessage = (TextView) findViewById(R.id.encryptedMessage);
                smsIntent.setType("vnd.android-dir/mms-sms");
                smsIntent.putExtra("sms_body", encryptedMessage.getText().toString());
                startActivity(smsIntent);
            }
        });
        final ImageButton newCipher = (ImageButton)findViewById(R.id.newCipher);
        newCipher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewCipher();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (randomCipher.length() == 0) {
            NewCipher();
        }
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
