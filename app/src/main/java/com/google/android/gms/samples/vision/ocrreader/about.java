package com.google.android.gms.samples.vision.ocrreader;

import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.TextView;

public class about extends AppCompatActivity {

    TextView a1,b1,b2,c1,c2,a2,d1,d2,d3,d4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        a1=(TextView)findViewById(R.id.textView);
        a1.setPaintFlags(a1.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
        b1=(TextView)findViewById(R.id.textView1);
        b2=(TextView)findViewById(R.id.textView2);
        c1=(TextView)findViewById(R.id.textView3);
        c2=(TextView)findViewById(R.id.textView4);
        a2=(TextView)findViewById(R.id.textView5);
        a2.setPaintFlags(a2.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
        d1=(TextView)findViewById(R.id.textView6);
        d2=(TextView)findViewById(R.id.textView7);
        d3=(TextView)findViewById(R.id.textView8);
        d4=(TextView)findViewById(R.id.textView9);
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
