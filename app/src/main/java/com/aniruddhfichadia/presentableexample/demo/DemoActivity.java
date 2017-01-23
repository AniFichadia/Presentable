/**
 * Copyright (C) 2016 Aniruddh Fichadia
 * <p/>
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * <p/>
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * <p/>
 * You should have received a copy of the GNU General Public License along with this program. If not, see
 * <http://www.gnu.org/licenses/>.
 * <p/>
 * If you use or enhance the code, please let me know using the provided author information or via email
 * Ani.Fichadia@gmail.com.
 */
package com.aniruddhfichadia.presentableexample.demo;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.aniruddhfichadia.presentable.PresentableActivity;
import com.aniruddhfichadia.presentableexample.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;


/**
 * @author Aniruddh Fichadia
 * @date 18/1/17
 */
public class DemoActivity
        extends PresentableActivity<DemoPresenter, DemoUi>
        implements DemoUi {
    private static final String TAG = DemoActivity.class.getSimpleName();

    @BindView(R.id.demo_progress_loading)
    ProgressBar progressLoading;
    @BindView(R.id.demo_txt_message)
    TextView    txtMessage;
    @BindView(R.id.demo_btn_load)
    Button      btnLoad;
    @BindView(R.id.demo_chk_something)
    CheckBox    chkSomething;


    @Nullable
    private Unbinder unbinder;


    @Override
    public int getLayoutResource() {
        return R.layout.activity_demo;
    }

    @NonNull
    @Override
    protected DemoPresenter createPresenter() {
        return new DemoPresenterImpl();
    }


    @Override
    protected void restoreUiState(@NonNull Bundle savedInstanceState) {
        super.restoreUiState(savedInstanceState);

        progressLoading.setVisibility(
                savedInstanceState.getBoolean("progressLoadingVisible") ? VISIBLE
                                                                        : INVISIBLE);
        btnLoad.setEnabled(savedInstanceState.getBoolean("btnLoadEnabled"));
    }

    @Override
    public void saveUiState(@NonNull Bundle outState) {
        super.saveUiState(outState);

        outState.putBoolean("progressLoadingVisible", progressLoading.getVisibility() == VISIBLE);
        outState.putBoolean("btnLoadEnabled", btnLoad.isEnabled());
    }


    @Override
    public void bindView(@NonNull View view) {
        unbinder = ButterKnife.bind(this, view);
    }

    @Override
    public void unbindView() {
        if (unbinder != null) {
            unbinder.unbind();
        }
        unbinder = null;
    }


    @OnClick(R.id.demo_btn_load)
    public void onLoadClicked() {
        getPresenter().loadSomething();
    }


    @Override
    public void doMeaninglessThing() {
        Toast.makeText(this, "Meaningless thing", Toast.LENGTH_LONG).show();
    }

    @Override
    public void doSomethingElseMeaningLess(String aParam, boolean anotherParam) {
        Toast.makeText(this, "Another meaningless thing " + aParam + " " + anotherParam,
                       Toast.LENGTH_LONG)
             .show();
    }

    @Override
    public void somethingEnqueueable(String aParam) {
        Toast.makeText(this, aParam, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoading() {
        Log.d(TAG, "showLoading()");

        progressLoading.setVisibility(VISIBLE);
    }

    @Override
    public void hideLoading() {
        Log.d(TAG, "hideLoading()");

        progressLoading.setVisibility(INVISIBLE);
    }

    @Override
    public void setMessage(String text) {
        Log.d(TAG, "setMessage(text: " + text + ")" + " from " + txtMessage.getText().toString());

        txtMessage.setText(text);
    }

    @Override
    public void setLoadingAllowed(boolean allowed) {
        Log.d(TAG, "setLoadingAllowed(allowed: " + allowed + ")");

        btnLoad.setEnabled(allowed);
    }


    @Override
    public int returnsSomething() {
        return 0;
    }
}