/*
 * Copyright (C) 2016 Aniruddh Fichadia
 *
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 *
 * You should have received a copy of the GNU General Public License along with this program. If not, see
 * <http:></http:>//www.gnu.org/licenses/>.
 *
 *
 * If you use or enhance the code, please let me know using the provided author information or via email
 * Ani.Fichadia@gmail.com.
 */
package com.aniruddhfichadia.presentable


import com.aniruddhfichadia.presentable.Contract.Ui
import com.aniruddhfichadia.replayableinterface.Delegator
import com.aniruddhfichadia.replayableinterface.ReplaySource


/**
 * @author Aniruddh Fichadia | Email: Ani.Fichadia@gmail.com | GitHub: AniFichadia (http://github.com/AniFichadia)
 * @date 2017-01-17
 */
abstract class BaseReplayablePresenter<UiT, out UiProxyT> : BasePresenter<UiT>()
        where UiT : Ui,
              UiProxyT : Ui,
              UiProxyT : Delegator<UiT>,
              UiProxyT : ReplaySource<UiT> {
    private val uiProxy: UiProxyT = this.createUiProxy()


    protected abstract fun createUiProxy(): UiProxyT


    override fun bindUi(ui: UiT) {
        uiProxy.bindDelegate(ui)

        onPresenterBound(ui)
    }

    override fun unBindUi() {
        uiProxy.unBindDelegate()

        onPresenterUnBound()

        isFirstTimeBound = false
    }

    override fun onUiReady(ui: UiT) {
        uiProxy.replay(ui)
    }


    @Suppress("UNCHECKED_CAST")
    override fun getUi(): UiT = uiProxy as UiT

    override fun isUiAttached(): Boolean = uiProxy.isDelegateBound
}
