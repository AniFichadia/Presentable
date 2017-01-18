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

import com.aniruddhfichadia.replayableinterface.ReplayTarget;
import com.aniruddhfichadia.replayableinterface.ReplayableAction;
import com.aniruddhfichadia.replayableinterface.ReplayableActionHelper;

import java.util.LinkedHashMap;
import java.util.Map.Entry;


/**
 * @author Aniruddh Fichadia
 * @date 17/1/17
 */
public class ReplayableDemoUi
        implements DemoUi, ReplayTarget<DemoUi> {
    private static final String TAG = ReplayableDemoUi.class.getSimpleName();

    private final LinkedHashMap<String, ReplayableAction<DemoUi>> actions = new LinkedHashMap<>();


    public ReplayableDemoUi() {
        super();
    }


    @Override
    public void showLoading() {
        Log.d(TAG, "showLoading()");

        String actionKey = ReplayableActionHelper.generateKeyForEnqueue();
        actions.remove(actionKey);
        actions.put(actionKey, new ReplayableAction<DemoUi>() {
            @Override
            public void replayOnTarget(DemoUi demoUi) {
                demoUi.showLoading();
            }
        });
    }

    @Override
    public void hideLoading() {
        Log.d(TAG, "hideLoading()");

        String actionKey = ReplayableActionHelper.generateKeyForEnqueue();
        actions.remove(actionKey);
        actions.put(actionKey, new ReplayableAction<DemoUi>() {
            @Override
            public void replayOnTarget(DemoUi demoUi) {
                demoUi.hideLoading();
            }
        });
    }

    @Override
    public void setMessage(String text) {
        Log.d(TAG, "setMessage(text: " + text + ")");

        String actionKey =
                ReplayableActionHelper.generateKeyForEnqueueParamUnique("setMessage(String)", text);

        actions.remove(actionKey);
        actions.put(actionKey, new ReplayableAction<DemoUi>(text) {
            @Override
            public void replayOnTarget(DemoUi demoUi) {
                demoUi.setMessage((String) params[0]);
            }
        });
    }


    @Override
    public void setLoadingAllowed(boolean allowed) {
        Log.d(TAG, "setLoadingAllowed(allowed: " + allowed + ")");

        String actionKey =
                ReplayableActionHelper.generateKeyForEnqueueParamUnique(
                        "setLoadingAllowed(boolean)", allowed);

        actions.remove(actionKey);
        actions.put(actionKey, new ReplayableAction<DemoUi>(allowed) {
            @Override
            public void replayOnTarget(DemoUi demoUi) {
                demoUi.setLoadingAllowed((boolean) params[0]);
            }
        });
    }

    @Override
    public void replay(DemoUi target) {
        for (Entry<String, ReplayableAction<DemoUi>> entry : actions.entrySet()) {
            entry.getValue().replayOnTarget(target);
        }

        actions.clear();
    }
}