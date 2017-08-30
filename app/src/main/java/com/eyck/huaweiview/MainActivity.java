package com.eyck.huaweiview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private EditText et_main;
    private SpeedingBallView sbv_main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et_main = (EditText)findViewById(R.id.et_main);
        sbv_main = (SpeedingBallView)findViewById(R.id.sbv_main);
        sbv_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cin = et_main.getText().toString();
                int percent = Integer.parseInt(cin);
                sbv_main.refresh(percent);
            }
        });
    }
}
