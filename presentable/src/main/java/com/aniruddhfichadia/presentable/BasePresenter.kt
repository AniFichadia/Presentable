/*
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
package com.aniruddhfichadia.presentable


import com.aniruddhfichadia.presentable.Contract.Presenter
import com.aniruddhfichadia.presentable.Contract.Ui

import java.lang.ref.WeakReference


/**
 * A convenient, extensible implementation of the [Presenter] interface.
 *
 * @author Aniruddh Fichadia | Email: Ani.Fichadia@gmail.com | GitHub: AniFichadia (http://github.com/AniFichadia)
 */
open class BasePresenter<UiT : Ui> : Presenter<UiT> {
    override var isFirstTimeBound: Boolean = true

    private var uiReference: WeakReference<UiT> = WeakReference<UiT>(null)


    override fun bindUi(ui: UiT) {
        this.uiReference = WeakReference(ui)

        onPresenterBound(ui)
    }

    override fun unBindUi() {
        uiReference = WeakReference<UiT>(null)

        onPresenterUnBound()

        isFirstTimeBound = false
    }


    override fun onPresenterBound(ui: UiT) {}

    override fun onPresenterUnBound() {}

    override fun onUiReady(ui: UiT) {}


    override fun shouldRetainPresenter(): Boolean = true

    override fun getUi(): UiT = uiReference.get()!!

    override fun isUiAttached(): Boolean = uiReference.get() != null
}