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


/**
 * @author Aniruddh Fichadia | Email: Ani.Fichadia@gmail.com | GitHub: AniFichadia (http://github.com/AniFichadia)
 * @date 2017-02-25
 */
interface Contract {
    /** A generalised User Interface contract for a [Presenter] */
    interface Ui

    /**
     * A pure Java presenter.
     *
     *
     * Ideally the presenter implementation should be platform agnostic and have no or platform agnostic references to
     * things like Android resources, etc. This allows easier porting to multiple platforms using translation tools
     */
    interface Presenter<UiT : Ui> {
        /**
         * Bind the [Ui] to the [Presenter]. Implementations should notify
         * appropriate lifecycle events after binding is complete
         * (i.e. [Presenter.onPresenterBound].
         */
        fun bindUi(ui: UiT)

        /**
         * Unbind the [Ui] from the [Presenter]. Implementations should notify
         * appropriate lifecycle events after unbinding is complete
         * (i.e. [Presenter.onPresenterUnBound].
         */
        fun unBindUi()


        fun onPresenterBound(ui: UiT)

        fun onPresenterUnBound()

        /** Update the UI when it is ready/stable  */
        fun onUiReady(ui: UiT)


        fun shouldRetainPresenter(): Boolean


        fun isUiAttached(): Boolean

        fun getUi(): UiT
    }

    /**
     * Clean architecture data layer interactor (inter-actor). Coordinates data retrieval and
     * notifies the [Presenter]
     */
    interface InterActor

    interface InterActorListener
}
