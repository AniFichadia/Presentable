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
package com.aniruddhfichadia.presentable.binder;


import android.app.Activity;
import android.app.Application.ActivityLifecycleCallbacks;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.aniruddhfichadia.presentable.Contract.Presenter;
import com.aniruddhfichadia.presentable.Contract.Ui;
import com.aniruddhfichadia.presentable.PresentableUiAndroid;

import org.jetbrains.annotations.Nullable;

import java.lang.ref.WeakReference;


/**
 * @author Aniruddh Fichadia
 * @date 2017-07-25
 */
public class LifecycleBinderActivity<
        ActivityT extends AppCompatActivity & PresentableUiAndroid<PresenterT, UiT>,
        PresenterT extends Presenter<UiT>,
        UiT extends Ui
        >
        extends LifecycleBinder<ActivityT> {
    @Nullable
    private BindingLifecycleCallbacks activityLifecycleCallbacks;


    @Override
    public void registerBinding(@NonNull ActivityT bound) {
        activityLifecycleCallbacks = new BindingLifecycleCallbacks<>(bound);
        bound.getApplication().registerActivityLifecycleCallbacks(activityLifecycleCallbacks);
    }

    @Override
    public void unregisterBinding(@NonNull ActivityT bound) {
        if (activityLifecycleCallbacks != null) {
            bound.getApplication()
                 .unregisterActivityLifecycleCallbacks(activityLifecycleCallbacks);
            activityLifecycleCallbacks = null;
        }
    }

    public static class BindingLifecycleCallbacks<
            ActivityT extends AppCompatActivity & PresentableUiAndroid<PresenterT, UiT>,
            PresenterT extends Presenter<UiT>,
            UiT extends Ui
            >
            implements ActivityLifecycleCallbacks {
        // TODO: to weak reference or not to weak reference?
        @NonNull
        private final WeakReference<ActivityT> boundReference;


        public BindingLifecycleCallbacks(@NonNull ActivityT boundActivity) {
            this.boundReference = new WeakReference<>(boundActivity);
        }


        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            if (guaranteeBinding(activity)) {
                ActivityT boundActivity = boundReference.get();

                boundActivity.inject();

                boundActivity.beforeOnCreate(savedInstanceState);

                int layoutResource = boundActivity.getLayoutResource();
                if (layoutResource > 0) {
                    boundActivity.setContentView(layoutResource);
                }

                View contentView = boundActivity.getWindow().getDecorView();
                boundActivity.bindView(contentView);
                boundActivity.afterBindView(contentView);


                PresenterT presenter = null;
                String bundleKey = generateBundleKeyForUi(boundActivity);
                if (savedInstanceState != null && savedInstanceState.containsKey(bundleKey)) {
                    presenter = boundActivity.getRegistry()
                                             .get(savedInstanceState.getString(bundleKey));

                    boundActivity.setPresenter(presenter);
                }

                if (presenter == null) {
                    // Create a new presenter instance
                    boundActivity.setPresenter(boundActivity.createPresenter());

                    boundActivity.onNewInstance();
                } else {
                    // The presenter has been retained, restore the UI state
                    boundActivity.restoreUiState(savedInstanceState);
                }

                boundActivity.afterOnCreate(savedInstanceState);
            }
        }


        @Override
        public void onActivityStarted(Activity activity) {
            if (guaranteeBinding(activity)) {
                ActivityT boundActivity = boundReference.get();

                boundActivity.getPresenter().bindUi(boundActivity.getUi());
            }
        }

        @Override
        public void onActivityResumed(Activity activity) {
            if (guaranteeBinding(activity)) {
                ActivityT boundActivity = boundReference.get();

                boundActivity.getPresenter().onUiReady(boundActivity.getUi());
            }
        }

        @Override
        public void onActivityPaused(Activity activity) {
        }

        @Override
        public void onActivityStopped(Activity activity) {
            if (guaranteeBinding(activity)) {
                ActivityT boundActivity = boundReference.get();

                boundActivity.getPresenter().unBindUi();
            }
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            if (guaranteeBinding(activity)) {
                ActivityT boundActivity = boundReference.get();

                PresenterT presenter = boundActivity.getPresenter();
                if (presenter.shouldRetainPresenter()) {
                    String presenterKey = boundActivity.getRegistry().put(presenter);
                    if (presenterKey != null) {
                        // Registry has persisted a value, save its key
                        outState.putString(generateBundleKeyForUi(boundActivity), presenterKey);
                    }
                }

                boundActivity.saveUiState(outState);
            }
        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            if (guaranteeBinding(activity)) {
                ActivityT boundActivity = boundReference.get();

                boundActivity.getPresenter().unBindUi();

                // Unbinding is unnecessary in Activities, just use a no-op in your unbindView method unless
                // this is explicitly necessary
                boundActivity.unbindView();
            }
        }

        protected boolean guaranteeBinding(Activity activity) {
            ActivityT boundActivity = boundReference.get();

            if (boundActivity != null && boundActivity == activity) {
                return true;
            } else if (boundActivity == null) {
                activity.getApplication().unregisterActivityLifecycleCallbacks(this);
            }

            return false;
        }
    }
}
