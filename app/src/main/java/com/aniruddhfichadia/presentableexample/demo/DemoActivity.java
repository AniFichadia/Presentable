package com.aniruddhfichadia.presentableexample.demo;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.aniruddhfichadia.presentableexample.R;


/**
 * @author Aniruddh Fichadia
 * @date 18/1/17
 */
public class DemoActivity
        extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
    }
}