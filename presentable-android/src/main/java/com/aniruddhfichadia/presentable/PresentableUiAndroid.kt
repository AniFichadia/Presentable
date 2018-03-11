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


import android.os.Bundle
import com.aniruddhfichadia.presentable.Contract.Presenter
import com.aniruddhfichadia.presentable.Contract.Ui


/**
 * @author Aniruddh Fichadia | Email: Ani.Fichadia@gmail.com | GitHub: AniFichadia (http://github.com/AniFichadia)
 * @date 2017-02-25
 */
interface PresentableUiAndroid<PresenterT : Presenter<UiT>, UiT : Ui> {
    //region Lifecycle
    fun beforeOnCreate(savedInstanceState: Bundle?)

    /**
     * Since onCreate... methods are intentionally final, this allows you to perform initialisation
     */
    fun afterOnCreate(savedInstanceState: Bundle?)

    /**
     * Persist the important UI state using the provided [Bundle].
     *
     *
     * You may have to persist properties of views that don't immediately restore their state,
     * such as [android.widget.TextView]'s text value.
     */
    fun saveUiState(outState: Bundle)

    fun onNewInstance()

    /** Restore important UI state.  */
    fun restoreUiState(savedState: Bundle)
    //endregion


    fun inject()

    fun getRegistry(): Registry


    //region Presenter
    /**
     * Provide your [Presenter] instance through this method. If no presenter is required,
     * return [DoNotPresent]
     */
    fun createPresenter(): PresenterT

    var presenter: PresenterT
    //endregion


    val ui: UiT
}
