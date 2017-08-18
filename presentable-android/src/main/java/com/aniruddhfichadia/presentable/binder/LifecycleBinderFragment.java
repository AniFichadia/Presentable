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


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentManager.FragmentLifecycleCallbacks;
import android.view.View;

import com.aniruddhfichadia.presentable.Contract.Presenter;
import com.aniruddhfichadia.presentable.Contract.Ui;
import com.aniruddhfichadia.presentable.PresentableUiAndroid;

import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;


/**
 * @author Aniruddh Fichadia
 * @date 2017-07-25
 */
public class LifecycleBinderFragment<
        FragmentT extends Fragment & PresentableUiAndroid<PresenterT, UiT>,
        PresenterT extends Presenter<UiT>,
        UiT extends Ui
        >
        extends LifecycleBinder<FragmentT> {
    @Nullable
    private BindingLifecycleCallbacks binder;


    @Override
    public void registerBinding(@NotNull FragmentT bound) {
        binder = new BindingLifecycleCallbacks<>(bound);
        bound.getFragmentManager()
             .registerFragmentLifecycleCallbacks(binder, false);
    }

    @Override
    public void unregisterBinding(@NotNull FragmentT bound) {
        if (binder != null) {
            bound.getFragmentManager()
                 .unregisterFragmentLifecycleCallbacks(binder);
            binder = null;
        }
    }


    public static class BindingLifecycleCallbacks<
            FragmentT extends Fragment & PresentableUiAndroid<PresenterT, UiT>,
            PresenterT extends Presenter<UiT>,
            UiT extends Ui
            >
            extends FragmentLifecycleCallbacks {
        // TODO: to weak reference or not to weak reference?
        @NonNull
        private final WeakReference<FragmentT> boundReference;


        public BindingLifecycleCallbacks(@NonNull FragmentT boundFragment) {
            this.boundReference = new WeakReference<>(boundFragment);
        }


        @SuppressWarnings("unchecked")
        @Override
        public void onFragmentCreated(FragmentManager fm, Fragment f, Bundle savedInstanceState) {
            super.onFragmentCreated(fm, f, savedInstanceState);

            if (guaranteeBinding(fm, f)) {
                FragmentT boundFragment = boundReference.get();

                boundFragment.inject();

                boundFragment.beforeOnCreate(savedInstanceState);

                PresenterT presenter = null;
                String bundleKey = generateBundleKeyForUi(boundFragment);
                if (savedInstanceState != null && savedInstanceState.containsKey(bundleKey)) {
                    presenter = boundFragment.getRegistry()
                                             .get(savedInstanceState.getString(bundleKey));
                    boundFragment.setPresenter(presenter);
                }

                if (presenter == null) {
                    // Create a new presenter instance
                    presenter = boundFragment.createPresenter();
                    boundFragment.setPresenter(presenter);

                    boundFragment.onNewInstance();
                }

                boundFragment.afterOnCreate(savedInstanceState);
            }
        }

        @SuppressWarnings("unchecked")
        @Override
        public void onFragmentStarted(FragmentManager fm, Fragment f) {
            super.onFragmentStarted(fm, f);

            if (guaranteeBinding(fm, f)) {
                FragmentT boundFragment = boundReference.get();

                boundFragment.getPresenter().bindUi(boundFragment.getUi());
            }
        }

        @Override
        public void onFragmentViewCreated(FragmentManager fm, Fragment f, View v, Bundle savedInstanceState) {
            super.onFragmentViewCreated(fm, f, v, savedInstanceState);

            // Note: View inflation and binding cannot be handled here. Do this in your fragment
            // onCreateView implementation
        }

        @SuppressWarnings("unchecked")
        @Override
        public void onFragmentActivityCreated(FragmentManager fm, Fragment f, Bundle savedInstanceState) {
            super.onFragmentActivityCreated(fm, f, savedInstanceState);

            if (guaranteeBinding(fm, f) && savedInstanceState != null) {
                FragmentT boundFragment = boundReference.get();

                boundFragment.restoreUiState(savedInstanceState);
            }
        }


        @SuppressWarnings("unchecked")
        @Override
        public void onFragmentResumed(FragmentManager fm, Fragment f) {
            super.onFragmentResumed(fm, f);

            if (guaranteeBinding(fm, f)) {
                FragmentT boundFragment = boundReference.get();

                boundFragment.getPresenter().onUiReady(boundFragment.getUi());
            }
        }


        @Override
        public void onFragmentStopped(FragmentManager fm, Fragment f) {
            super.onFragmentStopped(fm, f);

            if (guaranteeBinding(fm, f)) {
                FragmentT boundFragment = boundReference.get();

                boundFragment.getPresenter().unBindUi();
            }
        }

        @Override
        public void onFragmentViewDestroyed(FragmentManager fm, Fragment f) {
            super.onFragmentViewDestroyed(fm, f);

            if (guaranteeBinding(fm, f)) {
                FragmentT boundFragment = boundReference.get();

                boundFragment.unbindView();
            }
        }


        @Override
        public void onFragmentSaveInstanceState(FragmentManager fm, Fragment f, Bundle outState) {
            super.onFragmentSaveInstanceState(fm, f, outState);

            if (guaranteeBinding(fm, f)) {
                FragmentT boundFragment = boundReference.get();

                PresenterT presenter = boundFragment.getPresenter();
                if (presenter.shouldRetainPresenter()) {
                    String presenterKey = boundFragment.getRegistry().put(presenter);
                    if (presenterKey != null) {
                        // Registry has persisted a value, save its key
                        outState.putString(generateBundleKeyForUi(boundFragment), presenterKey);
                    }
                }

                boundFragment.saveUiState(outState);
            }
        }


        protected boolean guaranteeBinding(FragmentManager fm, Fragment f) {
            FragmentT boundFragment = boundReference.get();

            if (boundFragment != null && boundFragment == f) {
                return true;
            } else if (boundFragment == null) {
                fm.unregisterFragmentLifecycleCallbacks(this);
            }

            return false;
        }
    }
}
