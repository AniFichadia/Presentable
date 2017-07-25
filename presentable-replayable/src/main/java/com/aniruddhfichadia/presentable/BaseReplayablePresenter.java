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


import com.aniruddhfichadia.presentable.Contract.Ui;
import com.aniruddhfichadia.replayableinterface.Delegator;
import com.aniruddhfichadia.replayableinterface.ReplaySource;

import org.jetbrains.annotations.NotNull;


/**
 * @author Aniruddh Fichadia | Email: Ani.Fichadia@gmail.com | GitHub: AniFichadia (http://github.com/AniFichadia)
 * @date 2017-01-17
 */
public abstract class BaseReplayablePresenter<UiT extends Ui, UiProxyT extends Ui & Delegator<UiT> & ReplaySource<UiT>>
        extends BasePresenter<UiT> {
    @NotNull
    private final UiProxyT uiProxy;


    public BaseReplayablePresenter() {
        super();

        uiProxy = createUiProxy();
    }


    @NotNull
    protected abstract UiProxyT createUiProxy();


    @Override
    public void bindUi(@NotNull UiT ui) {
        uiProxy.bindDelegate(ui);

        onPresenterBound();
    }

    @Override
    public void unBindUi() {
        uiProxy.unBindDelegate();

        onPresenterUnBound();
    }

    @Override
    public void onUiReady(@NotNull UiT ui) {
        uiProxy.replay(ui);
    }

    @SuppressWarnings("unchecked")
    @Override
    public UiT getUi() {
        return (UiT) uiProxy;
    }

    @Override
    public boolean isUiAttached() {
        return uiProxy.isDelegateBound();
    }
}