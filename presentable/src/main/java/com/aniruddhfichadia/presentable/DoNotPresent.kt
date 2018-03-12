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


/**
 * Convenience class for when your UI doesn't require a [Presenter].
 *
 *
 *
 * @author Aniruddh Fichadia | Email: Ani.Fichadia@gmail.com | GitHub: AniFichadia (http://github.com/AniFichadia)
 */
class DoNotPresent<UiT : Ui> : Presenter<UiT> {
    override fun bindUi(ui: UiT) {
        // No-op
    }

    override fun unBindUi() {
        // No-op
    }


    override fun onPresenterBound(ui: UiT) {
        // No-op
    }

    override fun onPresenterUnBound() {
        // No-op
    }

    override fun onUiReady(ui: UiT) {
        // No-op
    }


    override fun shouldRetainPresenter(): Boolean {
        return true
    }


    override fun isUiAttached(): Boolean {
        return false
    }

    override fun getUi(): UiT? {
        return null
    }
}