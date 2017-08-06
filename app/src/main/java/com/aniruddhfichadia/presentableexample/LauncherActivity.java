package com.aniruddhfichadia.presentableexample;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.aniruddhfichadia.presentableexample.demo.fullrestoration.FullRestorationDemoActivity;
import com.aniruddhfichadia.presentableexample.demo.partialrestoration.PartialRestorationDemoActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * @author Aniruddh Fichadia
 * @date 2017-08-01
 */
public class LauncherActivity
        extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        ButterKnife.bind(this);
    }


    @OnClick(R.id.launcher_btn_demo_partial)
    public void onPartialDemoClicked() {
        startActivity(new Intent(this, PartialRestorationDemoActivity.class));
    }

    @OnClick(R.id.launcher_btn_demo_full)
    public void onFullDemoClicked() {
        startActivity(new Intent(this, FullRestorationDemoActivity.class));
    }


}
