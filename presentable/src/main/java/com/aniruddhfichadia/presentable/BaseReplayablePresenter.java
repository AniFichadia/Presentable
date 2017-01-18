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


import org.jetbrains.annotations.NotNull;


/**
 * TODO: document
 *
 * @author Aniruddh Fichadia | Email: Ani.Fichadia@gmail.com | GitHub: AniFichadia (http://github.com/AniFichadia)
 * @date 2017-01-17
 */
public abstract class BaseReplayablePresenter<UiT extends PresenterUi>
        extends BasePresenter<UiT> {
    public BaseReplayablePresenter() {
        super();
    }


    @SuppressWarnings("unchecked")
    @Override
    public void bindUi(@NotNull UiT ui) {
        if (this.ui instanceof ReplayableUi) {
            // Apply all replayable UI updates
            ((ReplayableUi<UiT>) this.ui).replay(ui);
        }

        super.bindUi(ui);
    }

    @Override
    public void unBindUi() {
        ui = getUnboundUi();
    }

    @Override
    public boolean isUiAttached() {
        return super.isUiAttached() && !isReplayable();
    }

    @NotNull
    protected abstract UiT getUnboundUi();


    protected boolean isReplayable() {
        return ui instanceof ReplayableUi;
    }
}