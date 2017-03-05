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


/**
 * @author Aniruddh Fichadia | Email: Ani.Fichadia@gmail.com | GitHub: AniFichadia (http://github.com/AniFichadia)
 */
public abstract class PresentableActivity<PresenterT extends Presenter, UiT extends Ui>
        extends AppCompatActivity
        implements PresentableUiAndroid<PresenterT> {
    private PresenterT     presenter;


    public PresentableActivity() {
        super();

        inject();
    }


    //region Lifecycle
    @CallSuper
    @SuppressWarnings("unchecked")
    @Override
    protected final void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(getLayoutResource());

        View contentView = getWindow().getDecorView();
        bindView(contentView);
        afterBindView(contentView);

        presenter = PresentableUiDelegateImpl.createOrRestorePresenter(this, savedInstanceState);


        getPresenter().bindUi((UiT) this);

        afterOnCreate(savedInstanceState);
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