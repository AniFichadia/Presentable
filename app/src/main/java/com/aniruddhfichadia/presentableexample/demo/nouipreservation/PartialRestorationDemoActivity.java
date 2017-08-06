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
package com.aniruddhfichadia.presentableexample.demo.nouipreservation;


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
import com.aniruddhfichadia.presentable.Registry;
import com.aniruddhfichadia.presentableexample.DemoApplication;
import com.aniruddhfichadia.presentableexample.R;
import com.aniruddhfichadia.presentableexample.demo.nouipreservation.PartialRestorationDemoContract.PartialRestorationDemoPresenter;
import com.aniruddhfichadia.presentableexample.demo.nouipreservation.PartialRestorationDemoContract.PartialRestorationDemoUi;
import com.tierable.stasis.StasisPreserve;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;


/**
 * @author Aniruddh Fichadia | Email: Ani.Fichadia@gmail.com | GitHub: AniFichadia (http://github.com/AniFichadia)
 * @date 18/1/17
 */
public class PartialRestorationDemoActivity
        extends PresentableActivity<PartialRestorationDemoPresenter, PartialRestorationDemoUi>
        implements PartialRestorationDemoUi {
    private static final String TAG = PartialRestorationDemoActivity.class.getSimpleName();

    @BindView(R.id.demo_progress_loading)
    @StasisPreserve
    ProgressBar progressLoading;
    @BindView(R.id.demo_txt_message)
    @StasisPreserve
    TextView    txtMessage;
    @BindView(R.id.demo_btn_load)
    @StasisPreserve
    Button      btnLoad;
    @BindView(R.id.demo_chk_something)
    @StasisPreserve
    CheckBox    chkSomething;

    @Nullable
    private Unbinder unbinder;

    private StasisPreservationStrategyPartialRestorationDemoActivity preservationStrategy;


    @Override
    public int getLayoutResource() {
        return R.layout.activity_demo;
    }

    @NonNull
    @Override
    public Registry getRegistry() {
        return ((DemoApplication) getApplication()).getRegistry();
    }

    @NonNull
    @Override
    public PartialRestorationDemoPresenter createPresenter() {
        PartialRestorationDemoInterActorImpl interActor = new PartialRestorationDemoInterActorImpl(
                ((DemoApplication) getApplication()).getSharedExecutor()
        );
        return new PartialRestorationDemoPresenterImpl(interActor);
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


    //region PartialRestorationDemoUi
    @Override
    public void doMeaninglessThing() {
        Log.d(TAG, "doMeaninglessThing() called");

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(PartialRestorationDemoActivity.this,
                               "Meaningless thing", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void doSomethingElseMeaningLess(final String aParam, final boolean anotherParam) {
        Log.d(TAG,
              "doSomethingElseMeaningLess() called with: aParam = [" + aParam + "], anotherParam = [" + anotherParam + "]");

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(PartialRestorationDemoActivity.this,
                               "Another meaningless thing " + aParam + " " + anotherParam,
                               Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void somethingEnqueueable(final String aParam) {
        Log.d(TAG, "somethingEnqueueable() called with: aParam = [" + aParam + "]");

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(PartialRestorationDemoActivity.this, aParam, Toast.LENGTH_SHORT)
                     .show();
            }
        });
    }

    @Override
    public void showLoading() {
        Log.d(TAG, "showLoading()");

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressLoading.setVisibility(VISIBLE);
            }
        });
    }

    @Override
    public void hideLoading() {
        Log.d(TAG, "hideLoading()");

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressLoading.setVisibility(INVISIBLE);
            }
        });
    }

    @Override
    public void setMessage(final String text) {
        Log.d(TAG, "setMessage(text: " + text + ")" + " from " + txtMessage.getText().toString());

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                txtMessage.setText(text);
            }
        });
    }

    @Override
    public void setLoadingAllowed(final boolean allowed) {
        Log.d(TAG, "setLoadingAllowed(allowed: " + allowed + ")");

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                btnLoad.setEnabled(allowed);
            }
        });
    }


    @Override
    public int returnsSomething() {
        return 0;
    }
    //endregion
}