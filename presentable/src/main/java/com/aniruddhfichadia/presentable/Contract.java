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

import java.io.Serializable;


/**
 * @author Aniruddh Fichadia | Email: Ani.Fichadia@gmail.com | GitHub: AniFichadia (http://github.com/AniFichadia)
 * @date 2017-02-25
 */
public interface Contract {
    /** A generalised User Interface contract for a {@link Presenter} */
    interface Ui {
    }

    /**
     * A pure Java presenter.
     * <p>
     * Ideally the presenter implementation should be platform agnostic and have no or platform agnostic references to
     * things like Android resources, etc. This allows easier porting to multiple platforms using translation tools
     */
    interface Presenter<UiT extends Ui> {
        boolean shouldRetainPresenter();


        /**
         * JUST bind the {@link Ui} to the {@link Presenter}. Implementations should call
         * {@link #afterBindUi()} after binding is complete.
         * <p>
         * Work required after binding the {@link Ui} should use use {@link #afterBindUi()}
         */
        void bindUi(@NotNull UiT ui);

        void afterBindUi();

        /**
         * JUST perform unbinding of the {@link Ui} from the {@link Presenter}. Implementations
         * should call {@link #afterUnBindUi()} after unbinding is complete.
         * <p>
         * Work required after unbinding the {@link Ui} should use use {@link #afterBindUi()}
         */
        void unBindUi();

        void afterUnBindUi();

        boolean isUiAttached();

        UiT getUi();
    }

    interface PresenterState
            extends Serializable {
    }

    /**
     * Clean architecture data layer interactor (inter-actor). Coordinates data retrieval and
     * notifies the {@link Presenter}
     */
    interface InterActor {
    }
}