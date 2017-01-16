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
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;


/**
 * @author Aniruddh Fichadia | Email: Ani.Fichadia@gmail.com | GitHub: AniFichadia (http://github.com/AniFichadia)
 */
public abstract class PresentableActivity<P extends Presenter>
        extends AppCompatActivity
        implements ViewBindable {
    private static final String KEY_PRESENTER_MODEL = "key_presenter_model";


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


    //region Lifecycle
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(getLayoutResource());

        View contentView = getWindow().getDecorView();
        bindView(contentView);
        afterBindView(contentView);

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

        // Unbinding is unnecessary in Activities, just use a no-op in your unbindView method unless
        // this is explicitly necessary
        unbindView();
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        PresenterModel presenterModel = lifecycleHooks.onSave();
        if (presenterModel != null) {
            outState.putSerializable(generatePresenterModelKey(), presenterModel);
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        PresenterModel presenterModel = null;

        if (savedInstanceState != null) {
            presenterModel = (PresenterModel) savedInstanceState.getSerializable(
                    generatePresenterModelKey());
        }

        lifecycleHooks.onRestore(presenterModel);
    }


    protected String generatePresenterModelKey() {
        return getClass().getSimpleName() + "." + KEY_PRESENTER_MODEL;
    }
    //endregion


    //region Dependency Injection
    // Override as necessary
    protected void inject() {
    }
    //endregion


    //region Presenter

    /**
     * Provide your {@link Presenter} instance through this method. If no presenter is required, return {@link
     * com.aniruddhfichadia.presentable.Presenter.DoNotPresent}
     */
    @NonNull
    protected abstract P createPresenter();

    /**
     * Internal access to the {@link Presenter}
     */
    protected final P getPresenter() {
        return presenter;
    }
    //endregion


    //region ViewBindable
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
    //endregion
}