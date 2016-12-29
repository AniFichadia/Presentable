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


import com.aniruddhfichadia.presentable.LifecycleHooks.NoLifecycleHooks;

import org.jetbrains.annotations.NotNull;


/**
 * A convenient, extensible implementation of the {@link Presenter} interface. The constructor associates a {@link
 * PresenterUi} with it and provides {@link NoLifecycleHooks} by default.
 *
 * @author Aniruddh Fichadia | Email: Ani.Fichadia@gmail.com | GitHub: AniFichadia (http://github.com/AniFichadia)
 */
public class BasePresenter<U extends PresenterUi>
        implements Presenter {
    @NotNull
    protected final U ui;


    public BasePresenter(@NotNull U ui) {
        this.ui = ui;
    }


    @NotNull
    @Override
    public LifecycleHooks getLifecycleHooks() {
        return new NoLifecycleHooks();
    }
}