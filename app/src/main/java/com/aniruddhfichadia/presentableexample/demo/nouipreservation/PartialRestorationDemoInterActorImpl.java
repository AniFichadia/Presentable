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
package com.aniruddhfichadia.presentableexample.demo.nouipreservation;


import com.aniruddhfichadia.presentable.BaseInterActor;
import com.aniruddhfichadia.presentableexample.demo.nouipreservation.PartialRestorationDemoContract.PartialRestorationDemoInterActor;
import com.aniruddhfichadia.presentableexample.demo.nouipreservation.PartialRestorationDemoContract.PartialRestorationDemoInterActorListener;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;


/**
 * @author Aniruddh Fichadia
 * @date 2017-03-02
 */
public class PartialRestorationDemoInterActorImpl
        extends BaseInterActor
        implements PartialRestorationDemoInterActor {
    private PartialRestorationDemoInterActorListener listener;
    private AtomicBoolean doingAsyncStuff = new AtomicBoolean(false);


    public PartialRestorationDemoInterActorImpl(@NotNull Executor executor) {
        super(executor);
    }


    @Override
    public void setListener(PartialRestorationDemoInterActorListener listener) {
        this.listener = listener;
    }

    @Override
    public boolean isDoingAsyncStuff() {
        return doingAsyncStuff.get();
    }

    @Override
    public void doAsyncStuff() {
        doingAsyncStuff.set(true);

        // Emulate a long running task
        execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                } catch (Exception e) {
                }

                doingAsyncStuff.set(false);

                if (listener != null) {
                    listener.onAsyncStuffComplete();
                }
            }
        });
    }
}