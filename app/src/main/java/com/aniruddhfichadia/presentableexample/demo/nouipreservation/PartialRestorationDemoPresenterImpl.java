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


import android.util.Log;

import com.aniruddhfichadia.presentable.BaseReplayablePresenter;
import com.aniruddhfichadia.presentableexample.demo.nouipreservation.PartialRestorationDemoContract.PartialRestorationDemoInterActor;
import com.aniruddhfichadia.presentableexample.demo.nouipreservation.PartialRestorationDemoContract.PartialRestorationDemoInterActorListener;
import com.aniruddhfichadia.presentableexample.demo.nouipreservation.PartialRestorationDemoContract.PartialRestorationDemoPresenter;
import com.aniruddhfichadia.presentableexample.demo.nouipreservation.PartialRestorationDemoContract.PartialRestorationDemoUi;

import org.jetbrains.annotations.NotNull;


/**
 * @author Aniruddh Fichadia | Email: Ani.Fichadia@gmail.com | GitHub: AniFichadia (http://github.com/AniFichadia)
 * @date 17/1/17
 */
public class PartialRestorationDemoPresenterImpl
        extends
        BaseReplayablePresenter<PartialRestorationDemoUi, ReplayablePartialRestorationDemoContract$PartialRestorationDemoUi>
        implements PartialRestorationDemoPresenter, PartialRestorationDemoInterActorListener {
    private static final String TAG = PartialRestorationDemoPresenterImpl.class.getSimpleName();

    private final PartialRestorationDemoInterActor interActor;


    public PartialRestorationDemoPresenterImpl(PartialRestorationDemoInterActor interActor) {
        this.interActor = interActor;
        interActor.setListener(this);
    }


    @NotNull
    @Override
    protected ReplayablePartialRestorationDemoContract$PartialRestorationDemoUi createUiProxy() {
        return new ReplayablePartialRestorationDemoContract$PartialRestorationDemoUi();
    }


    @Override
    public void loadSomething() {
        Log.d(TAG, "loadSomething");

        if (!interActor.isDoingAsyncStuff()) {
            getUi().showLoading();
            getUi().setMessage("Loading ...");
            getUi().setLoadingAllowed(false);

            interActor.doAsyncStuff();
        }
    }

    @Override
    public void onAsyncStuffComplete() {
        Log.d(TAG, "Loading done");
        getUi().setLoadingAllowed(true);
        getUi().hideLoading();
        getUi().setMessage("Loading done");
        getUi().doMeaninglessThing();
        getUi().doSomethingElseMeaningLess("blah", false);
    }
}