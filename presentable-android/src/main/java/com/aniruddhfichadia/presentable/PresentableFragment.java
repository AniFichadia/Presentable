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
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aniruddhfichadia.presentable.LifecycleHooks.PresenterState;


/**
 * A {@link Fragment} with appropriate hook-ins and abstractions to interact with a {@link Presenter}. Also adds a bit
 * of convenience for view creation.
 * <p>
 * {@link Presenter} instantiation occurs in the constructor.
 * <p>
 * For applications that require dependency injection (eg. Dagger), you can implement the {@link #inject()} method.
 * Depending on how your dependency injection is configured (constructor vs post instantiation), you can to control the
 * point of injection using {@link #shouldInjectBeforeInitialisingPresenter()}.
 *
 * @author Aniruddh Fichadia | Email: Ani.Fichadia@gmail.com | GitHub: AniFichadia (http://github.com/AniFichadia)
 */
public abstract class PresentableFragment<P extends Presenter>
        extends Fragment {
    /**
     * A format for {@link PresenterState} persistence. Refer to {@link #generatePresenterStateKey()} for the actual key
     * used in the bundle for {@link #onSaveInstanceState(Bundle)} and {@link #onViewStateRestored(Bundle)}
     */
    private static final String KEY_PRESENTER_STATE = ".key_presenter_state";


    @NonNull
    protected final P              presenter;
    @NonNull
    private final   LifecycleHooks lifecycleHooks;


    public PresentableFragment() {
        super();

        boolean injectBeforeInitialisingPresenter = shouldInjectBeforeInitialisingPresenter();

        if (injectBeforeInitialisingPresenter) {
            inject();
        }

        presenter = createPresenter();
        lifecycleHooks = presenter.getLifecycleHooks();

        if (!injectBeforeInitialisingPresenter) {
            inject();
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        int layoutResource = getLayoutResource();

        if (layoutResource < 0) {
            return null;
        } else {
            View view = inflater.inflate(layoutResource, container, false);

            bind(view);

            return view;
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        lifecycleHooks.onCreate();
    }

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
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        PresenterState presenterState = lifecycleHooks.onSave();
        if (presenterState != null) {
            outState.putSerializable(generatePresenterStateKey(), presenterState);
        }
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        PresenterState presenterState = null;

        if (savedInstanceState != null) {
            presenterState = (PresenterState) savedInstanceState.getSerializable(generatePresenterStateKey());
        }

        lifecycleHooks.onRestore(presenterState);
    }


    /**
     * Generates a key unique to the {@link Fragment} class to persist {@link PresenterState} during {@link
     * #onSaveInstanceState(Bundle)} and {@link #onViewStateRestored(Bundle)}
     */
    private String generatePresenterStateKey() {
        return getClass().getSimpleName() + KEY_PRESENTER_STATE;
    }


    protected boolean shouldInjectBeforeInitialisingPresenter() {
        return true;
    }

    /**
     * Performs dependency injection for your fragment. You can override this as necessary or not implement it at all!
     * <p>
     * {@link #shouldInjectBeforeInitialisingPresenter()} controls when injection occurs.
     */
    protected void inject() {
    }

    /** Provide your {@link Presenter} instance through this method */
    @NonNull
    protected abstract P createPresenter();

    /** Provide your layout resource through this method. Use any negative number for no layout */
    @LayoutRes
    protected abstract int getLayoutResource();

    /**
     * Perform any view binding (eg. via ButterKnife, or using a bunch of {@link View#findViewById(int)} calls). Won't
     * be called if a layout isn't inflated
     *
     * @param view
     *         The inflated layout. Won't be null
     */
    protected void bind(@NonNull View view) {
    }


    /**
     * Internal access to the {@link Presenter}
     */
    protected final P getPresenter() {
        return presenter;
    }
}