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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.aniruddhfichadia.presentable.LifecycleHooks.PresenterState;


/**
 * @author Aniruddh Fichadia | Email: Ani.Fichadia@gmail.com | GitHub: AniFichadia (http://github.com/AniFichadia)
 */
public abstract class PresentableActivity<P extends Presenter>
        extends AppCompatActivity {
    private static final String KEY_PRESENTER_STATE = ".key_presenter_state";


    @NonNull
    protected final P              presenter;
    @NonNull
    private final   LifecycleHooks lifecycleHooks;


    public PresentableActivity() {
        super();

        inject();

        presenter = createPresenter();
        lifecycleHooks = presenter.getLifecycleHooks();
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        lifecycleHooks.onCreate();
    }

    @Override
    protected void onResume() {
        super.onResume();

        lifecycleHooks.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();

        lifecycleHooks.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        lifecycleHooks.onDestroy();
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        PresenterState presenterState = lifecycleHooks.onSave();
        if (presenterState != null) {
            outState.putSerializable(generatePresenterStateKey(), presenterState);
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        PresenterState presenterState = null;

        if (savedInstanceState != null) {
            presenterState = (PresenterState) savedInstanceState.getSerializable(generatePresenterStateKey());
        }

        lifecycleHooks.onRestore(presenterState);
    }


    private String generatePresenterStateKey() {
        return getClass().getSimpleName() + KEY_PRESENTER_STATE;
    }


    // Override as necessary
    protected void inject() {
    }


    @NonNull
    protected abstract P createPresenter();

    protected final P getPresenter() {
        return presenter;
    }
}