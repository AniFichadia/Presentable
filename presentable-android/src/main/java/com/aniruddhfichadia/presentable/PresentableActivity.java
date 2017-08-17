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
import android.support.annotation.CallSuper;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.aniruddhfichadia.presentable.Contract.Presenter;
import com.aniruddhfichadia.presentable.Contract.Ui;
import com.aniruddhfichadia.presentable.util.NestableUtilAndroid;


/**
 * @author Aniruddh Fichadia | Email: Ani.Fichadia@gmail.com | GitHub: AniFichadia (http://github.com/AniFichadia)
 */
public abstract class PresentableActivity<PresenterT extends Presenter<UiT>, UiT extends Ui>
        extends AppCompatActivity
        implements PresentableUiAndroid<PresenterT, UiT>, Nestable {
    private PresenterT presenter;


    public PresentableActivity() {
        super();
    }


    //region Lifecycle
    @CallSuper
    @Override
    protected final void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        inject();

        beforeOnCreate(savedInstanceState);

        setContentView(getLayoutResource());

        View contentView = getWindow().getDecorView();
        bindView(contentView);
        afterBindView(contentView);

        presenter = PresentableUiDelegateImpl.createOrRestorePresenter(this, savedInstanceState);

        afterOnCreate(savedInstanceState);
    }


    @Override
    protected void onStart() {
        super.onStart();

        getPresenter().bindUi(getUi());
    }

    @Override
    protected void onResume() {
        super.onResume();

        getPresenter().onUiReady(getUi());
    }

    @Override
    protected void onStop() {
        super.onStop();

        getPresenter().unBindUi();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        getPresenter().unBindUi();

        // Unbinding is unnecessary in Activities, just use a no-op in your unbindView method unless
        // this is explicitly necessary
        unbindView();
    }

    @Override
    public final void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        PresentableUiDelegateImpl.savePresenter(this, outState);
    }
    //endregion

    //region PresentableUiAndroid
    @Override
    public void beforeOnCreate(@Nullable Bundle savedInstanceState) {
    }

    @Override
    public void afterOnCreate(@Nullable Bundle savedInstanceState) {
    }

    @Override
    public void saveUiState(@NonNull Bundle outState) {
    }

    @Override
    public void onNewInstance() {
    }

    @Override
    public void restoreUiState(@NonNull Bundle savedState) {
    }


    @Override
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

    @Override
    public void setPresenter(PresenterT presenter) {
        this.presenter = presenter;
    }

    @SuppressWarnings("unchecked")
    @Override
    public UiT getUi() {
        return (UiT) this;
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

    //region Nestable
    @Nullable
    @Override
    public Nestable getNestableParent() {
        return NestableUtilAndroid.getNestableParent(this);
    }
    //endregion
}