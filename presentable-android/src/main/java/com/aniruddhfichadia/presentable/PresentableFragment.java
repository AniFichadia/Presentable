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
package com.aniruddhfichadia.presentable;


import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.CallSuper;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aniruddhfichadia.presentable.Contract.Presenter;
import com.aniruddhfichadia.presentable.Contract.Ui;


/**
 * A {@link Fragment} with appropriate hook-ins and abstractions to interact with a {@link Presenter}. Also adds a bit
 * of convenience for view creation.
 * <p>
 * {@link Presenter} instantiation occurs in the constructor.
 * <p>
 * For applications that require dependency injection (eg. Dagger), you can implement the {@link #inject()} method.
 * <p>
 *
 * @author Aniruddh Fichadia | Email: Ani.Fichadia@gmail.com | GitHub: AniFichadia (http://github.com/AniFichadia)
 */
public abstract class PresentableFragment<PresenterT extends Presenter, UiT extends Ui>
        extends Fragment
        implements PresentableUiAndroid<PresenterT> {
    private PresenterT     presenter;
    private LifecycleHooks lifecycleHooks;

    @Nullable
    private Handler uiHandler;


    public PresentableFragment() {
        super();

        inject();
    }


    protected void runOnUiThread(@NonNull Runnable runnable) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            // This is the main looper/thread, just execute the runnable
            runnable.run();
        } else {
            // Not the main looper, post event on it
            if (uiHandler == null) {
                uiHandler = new Handler(Looper.getMainLooper());
            }
            uiHandler.post(runnable);
        }
    }


    //region Lifecycle
    @CallSuper
    @Override
    public final void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = PresentableUiDelegateImpl.createOrRestorePresenter(this, savedInstanceState);


        lifecycleHooks = presenter.getLifecycleHooks();

        afterOnCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public final View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                                   @Nullable Bundle savedInstanceState) {
        int layoutResource = getLayoutResource();

        if (layoutResource > 0) {
            View view = inflater.inflate(layoutResource, container, false);

            bindView(view);
            afterBindView(view);

            return view;
        } else {
            return null;
        }
    }


    @SuppressWarnings("unchecked")
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getPresenter().bindUi((UiT) this);

        lifecycleHooks.onCreate();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onResume() {
        super.onResume();

        lifecycleHooks.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();

        lifecycleHooks.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        lifecycleHooks.onDestroy();

        getPresenter().unBindUi();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        unbindView();
    }


    @Override
    public final void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        PresentableUiDelegateImpl.handleSave(this, outState);
    }
    //endregion

    //region Overrideable lifecycle events
    public void afterOnCreate(@Nullable Bundle savedInstanceState) {
    }

    public void saveUiState(@NonNull Bundle outState) {
    }

    public void restoreUiState(@NonNull Bundle savedState) {
    }
    //endregion


    public void inject() {
    }

    @NonNull
    public abstract Registry getRegistry();


    @NonNull
    public abstract PresenterT createPresenter();

    @Override
    public final PresenterT getPresenter() {
        return presenter;
    }


    @LayoutRes
    @Override
    public abstract int getLayoutResource();

    @Override
    public void bindView(@NonNull View view) {
    }

    @Override
    public void afterBindView(@NonNull View view) {
    }

    @Override
    public void unbindView() {
    }
}