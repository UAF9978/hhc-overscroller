package com.hhc.overscroller;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.hhc.overscroller.views.JellyTextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button mButton1;
    private Button mButton2;
    private Button mButton3;

    private JellyTextView mJellyTextView;
    private int distance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mButton1 = (Button) findViewById(R.id.button1);
        mButton2 = (Button) findViewById(R.id.button2);
        mButton3 = (Button) findViewById(R.id.button3);
        mJellyTextView = (JellyTextView) findViewById(R.id.mJellyTextView);
        mButton1.setOnClickListener(this);
        mButton2.setOnClickListener(this);
        mButton3.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button1:
                mJellyTextView.scrollTo(distance, 0);
                distance += 10;
                break;
            case R.id.button2:
                mJellyTextView.scrollBy(30, 0);
                mJellyTextView.setX(500);
                mJellyTextView.setY(500);
                break;
            case R.id.button3:
                //不知道为什么第一次调用会贴墙，即到达x=0的位置
                mJellyTextView.spingBack();
                break;
        }
    }
}
