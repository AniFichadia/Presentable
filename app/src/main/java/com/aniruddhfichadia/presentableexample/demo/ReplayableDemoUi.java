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


import android.util.Log;

import com.aniruddhfichadia.replayableinterface.Delegatable;
import com.aniruddhfichadia.replayableinterface.ReplaySource;
import com.aniruddhfichadia.replayableinterface.ReplayableAction;

import java.util.LinkedHashMap;
import java.util.Map.Entry;

import static com.aniruddhfichadia.replayableinterface.ReplayableActionHelper.generateKeyForEnqueueLastOnly;


/**
 * @author Aniruddh Fichadia
 * @date 17/1/17
 */
public class ReplayableDemoUi
        implements Delegatable<DemoUi>, DemoUi, ReplaySource<DemoUi> {
    private static final String TAG = ReplayableDemoUi.class.getSimpleName();

    private DemoUi delegate;
    private final LinkedHashMap<String, ReplayableAction<DemoUi>> actions = new LinkedHashMap<>();


    public ReplayableDemoUi() {
        super();
    }


    @Override
    public void bindDelegate(DemoUi delegate) {
        Log.d(TAG, "bindDelegate(DemoUi: " + delegate.getClass().getSimpleName() + ")");

        this.delegate = delegate;
    }

    @Override
    public void unBindDelegate() {
        Log.d(TAG, "unBindDelegate()");

        this.delegate = null;
    }

    @Override
    public boolean isDelegateBound() {
        return delegate != null;
    }


    @Override
    public void doMeaninglessThing() {
        Log.d(TAG, "doMeaninglessThing()");

        if (delegate != null) {
            delegate.doMeaninglessThing();
        }
    }

    @Override
    public void showLoading() {
        Log.d(TAG, "showLoading()");

        if (delegate != null) {
            delegate.showLoading();
        }

        String actionKey = generateKeyForEnqueueLastOnly("showLoading()");
        actions.remove(actionKey);
        actions.put(actionKey, new ReplayableAction<DemoUi>() {
            @Override
            public void replayOnTarget(DemoUi demoUi) {
                Log.d(TAG, "Replaying: showLoading()");

                demoUi.showLoading();
            }
        });
    }

    @Override
    public void hideLoading() {
        Log.d(TAG, "hideLoading()");

        if (delegate != null) {
            delegate.hideLoading();
        }

        String actionKey = generateKeyForEnqueueLastOnly("hideLoading()");
        actions.remove(actionKey);
        actions.put(actionKey, new ReplayableAction<DemoUi>() {
            @Override
            public void replayOnTarget(DemoUi demoUi) {
                Log.d(TAG, "Replaying: hideLoading()");

                demoUi.hideLoading();
            }
        });
    }

    @Override
    public void setMessage(String text) {
        Log.d(TAG, "setMessage(text: " + text + ")");

        if (delegate != null) {
            delegate.setMessage(text);
        }

        String actionKey = generateKeyForEnqueueLastOnly("setMessage(String)");

        actions.remove(actionKey);
        actions.put(actionKey, new ReplayableAction<DemoUi>(text) {
            @Override
            public void replayOnTarget(DemoUi demoUi) {
                Log.d(TAG, "Replaying: setMessage(text: " + params[0] + ")");

                demoUi.setMessage((String) params[0]);
            }
        });
    }


    @Override
    public void setLoadingAllowed(boolean allowed) {
        Log.d(TAG, "setLoadingAllowed(allowed: " + allowed + ")");

        if (delegate != null) {
            delegate.setLoadingAllowed(allowed);
        }

        String actionKey = generateKeyForEnqueueLastOnly("setLoadingAllowed(boolean)");

        actions.remove(actionKey);
        actions.put(actionKey, new ReplayableAction<DemoUi>(allowed) {
            @Override
            public void replayOnTarget(DemoUi demoUi) {
                Log.d(TAG, "Replaying: setLoadingAllowed(allowed: " + params[0] + ")");

                demoUi.setLoadingAllowed((boolean) params[0]);
            }
        });
    }

    @Override
    public void replay(DemoUi target) {
        for (Entry<String, ReplayableAction<DemoUi>> entry : actions.entrySet()) {
            entry.getValue().replayOnTarget(target);
        }

//        actions.clear();
    }
}