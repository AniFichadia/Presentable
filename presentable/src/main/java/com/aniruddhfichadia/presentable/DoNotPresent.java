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


/**
 * Convenience class for when your UI doesn't require a {@link Presenter}.
 * <p>
 * Returns {@link NoLifecycleHooks} for {@link #getLifecycleHooks()}.
 *
 * @author Aniruddh Fichadia | Email: Ani.Fichadia@gmail.com | GitHub: AniFichadia (http://github.com/AniFichadia)
 */
public final class DoNotPresent<UiT extends Ui>
        implements Presenter<UiT> {
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
    public void bindUi(@NotNull UiT ui) {
        // No-op
    }

    @Override
    public void afterBindUi() {
        // No-op
    }

    @Override
    public void unBindUi() {
        // No-op
    }

    @Override
    public void afterUnBindUi() {
        // No-op
    }

    @Override
    public boolean isUiAttached() {
        return false;
    }

    @Override
    public UiT getUi() {
        return null;
    }
}