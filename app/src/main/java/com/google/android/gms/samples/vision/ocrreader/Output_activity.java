package com.google.android.gms.samples.vision.ocrreader;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

public class Output_activity extends AppCompatActivity {

    EditText output;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_output_activity);
        output=(EditText) findViewById(R.id.img_output);
        Bundle bundle = getIntent().getExtras();
        String message = bundle.getString("key");
        output.setText(message);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}

