package com.lance.common.activity.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void showDemo(View v) {
        switch (v.getId()) {
            case R.id.btn_crop_demo:
                startActivity(new Intent(this, CropImageDemoActivity.class));
                break;
        }
    }
}
