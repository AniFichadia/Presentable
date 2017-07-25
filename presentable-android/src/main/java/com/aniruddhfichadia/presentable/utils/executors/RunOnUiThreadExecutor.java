/**
 * Copyright (C) 2017 Aniruddh Fichadia
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
package com.aniruddhfichadia.presentable.utils.executors;


import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import java.util.concurrent.Executor;


/**
 * Imitates {@link android.app.Activity#runOnUiThread(Runnable)} behaviour when calling {@link #execute(Runnable)}
 *
 * @author Aniruddh Fichadia
 * @date 2017-07-25
 */
public class RunOnUiThreadExecutor
        implements Executor {
    @NonNull
    private final Handler mainHandler;


    public RunOnUiThreadExecutor() {
        mainHandler = new Handler(Looper.getMainLooper());
    }


    @Override
    public void execute(@NonNull Runnable command) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            // This is the main looper/thread, just execute the runnable
            command.run();
        } else {
            // Not the main looper, post event on it
            mainHandler.post(command);
        }
    }
}
