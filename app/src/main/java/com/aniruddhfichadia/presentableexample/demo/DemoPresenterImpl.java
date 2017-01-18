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


import android.os.Handler;
import android.util.Log;

import com.aniruddhfichadia.presentable.BaseReplayablePresenter;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;


/**
 * @author Aniruddh Fichadia
 * @date 17/1/17
 */
public class DemoPresenterImpl
        extends BaseReplayablePresenter<DemoUi>
        implements DemoPresenter {
    private static final String TAG = DemoPresenterImpl.class.getSimpleName();

    private final Handler handler = new Handler();


    @NotNull
    @Override
    protected DemoUi getUnboundUi() {
        // Alternatively, inject or retain an instance of this
        return new ReplayableDemoUi();
    }


    @Override
    public void loadSomething() {
        Log.d(TAG, "loadSomething");

        getUi().showLoading();
        getUi().setMessage("Loading ...");
        getUi().setLoadingAllowed(false);

        // Emulate a long running task
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "Loading done");
                getUi().setLoadingAllowed(true);
                getUi().hideLoading();
                getUi().setMessage("Loading done");
            }
        }, TimeUnit.SECONDS.toMillis(5));
    }
}