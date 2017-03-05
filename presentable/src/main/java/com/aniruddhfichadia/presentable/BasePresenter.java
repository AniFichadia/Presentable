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
import com.aniruddhfichadia.presentable.LifecycleHooks.NoLifecycleHooks;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * A convenient, extensible implementation of the {@link Presenter} interface. The constructor associates a {@link
 * Ui} with it and provides {@link NoLifecycleHooks} by default.
 *
 * @author Aniruddh Fichadia | Email: Ani.Fichadia@gmail.com | GitHub: AniFichadia (http://github.com/AniFichadia)
 */
public class BasePresenter<UiT extends Ui>
        implements Presenter<UiT> {
    @Nullable
    private UiT ui;


    public BasePresenter() {
        super();
    }

    /**
     * @deprecated Really should not use this. There are consequences for binding too early in the
     * UI lifecycle, such as NPEs.
     */
    @Deprecated
    public BasePresenter(UiT ui) {
        this();

        // TODO prevents calling afterBind when the UIs lifecycle hasn't properly bound the
        // presenter
        this.ui = ui;
    }


    @NotNull
    @Override
    public LifecycleHooks getLifecycleHooks() {
        return new NoLifecycleHooks();
    }


    @Override
    public boolean shouldRetainPresenter() {
        return true;
    }


    @Override
    public boolean isUiAttached() {
        return ui != null;
    }

    @Override
    public void bindUi(@NotNull UiT ui) {
        this.ui = ui;

        afterBindUi();
    }

    @Override
    public void afterBindUi() {
    }

    @Override
    public void unBindUi() {
        ui = null;

        afterUnBindUi();
    }

    @Override
    public void afterUnBindUi() {
    }

    @Override
    public UiT getUi() {
        return ui;
    }
}