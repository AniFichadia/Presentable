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


import com.aniruddhfichadia.presentable.Contract.Presenter;
import com.aniruddhfichadia.presentable.Contract.Ui;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.ref.WeakReference;


/**
 * A convenient, extensible implementation of the {@link Presenter} interface.
 *
 * @author Aniruddh Fichadia | Email: Ani.Fichadia@gmail.com | GitHub: AniFichadia (http://github.com/AniFichadia)
 */
public class BasePresenter<UiT extends Ui>
        implements Presenter<UiT> {
    @NotNull
    private WeakReference<UiT> uiReference;
    private boolean            firstTimeBound;


    public BasePresenter() {
        super();

        this.uiReference = new WeakReference<>(null);
        this.firstTimeBound = true;
    }


    @Override
    public boolean shouldRetainPresenter() {
        return true;
    }


    @Override
    public void bindUi(@NotNull UiT ui) {
        this.uiReference = new WeakReference<>(ui);

        onPresenterBound(ui);
    }

    @Override
    public void unBindUi() {
        uiReference = new WeakReference<>(null);

        onPresenterUnBound();

        setFirstTimeBound(false);
    }


    @Override
    public void onPresenterBound(@NotNull UiT ui) {
    }

    @Override
    public void onPresenterUnBound() {
    }

    @Override
    public void onUiReady(@NotNull UiT ui) {
    }


    @Override
    public boolean isUiAttached() {
        return uiReference.get() != null;
    }

    @Nullable
    @Override
    public UiT getUi() {
        return uiReference.get();
    }


    protected boolean isFirstTimeBound() {
        return firstTimeBound;
    }

    protected void setFirstTimeBound(boolean firstTimeBound) {
        this.firstTimeBound = firstTimeBound;
    }
}