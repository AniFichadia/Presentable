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
package com.aniruddhfichadia.presentableexample.demo.fullrestoration;


import com.aniruddhfichadia.presentable.Contract.InterActor;
import com.aniruddhfichadia.presentable.Contract.Presenter;
import com.aniruddhfichadia.presentable.Contract.Ui;
import com.aniruddhfichadia.replayableinterface.ReplayStrategy;
import com.aniruddhfichadia.replayableinterface.ReplayableInterface;
import com.aniruddhfichadia.replayableinterface.ReplayableMethod;


/**
 * @author Aniruddh Fichadia | Email: Ani.Fichadia@gmail.com | GitHub: AniFichadia (http://github.com/AniFichadia)
 * @date 2017-02-25
 */
public interface FullRestorationDemoContract {
    @ReplayableInterface(clearAfterReplaying = false, alwaysCaptureInvocations = true)
    interface FullRestorationDemoUi
            extends Ui {
        @ReplayableMethod(ReplayStrategy.NONE)
        void doMeaninglessThing();

        @ReplayableMethod(ReplayStrategy.ENQUEUE_PARAM_UNIQUE)
        void doSomethingElseMeaningLess(String aParam, boolean anotherParam);

        @ReplayableMethod(ReplayStrategy.ENQUEUE)
        void somethingEnqueueable(String aParam);

        @ReplayableMethod(value = ReplayStrategy.ENQUEUE_LAST_IN_GROUP, group = "loadingState")
        void showLoading();

        @ReplayableMethod(value = ReplayStrategy.ENQUEUE_LAST_IN_GROUP, group = "loadingState")
        void hideLoading();

        void setMessage(String text);

        void setLoadingAllowed(boolean allowed);


        int returnsSomething();
    }

    interface FullRestorationDemoPresenter
            extends Presenter<FullRestorationDemoUi> {
        void loadSomething();
    }

    interface FullRestorationDemoInterActor
            extends InterActor {
        void setListener(FullRestorationDemoInterActorListener listener);

        boolean isDoingAsyncStuff();

        void doAsyncStuff();
    }

    interface FullRestorationDemoInterActorListener {
        void onAsyncStuffComplete();
    }
}