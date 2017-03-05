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
 * @author Aniruddh Fichadia | Email: Ani.Fichadia@gmail.com | GitHub: AniFichadia (http://github.com/AniFichadia)
 * @date 2017-02-25
 */
public interface Contract {
    /** A generalised User Interface contract for a {@link Presenter} */
    interface Ui {
    }

    /**
     * The bare minimum lifecycle events required for a {@link Presenter}. These should be all
     * you actually need. If more fine-grained events are required, specify that in your
     * {@link Presenter} contract to the UI and implement as required
     */
    interface PresenterLifecycleEvents {
        void onPresenterBound();

        void onPresenterUnBound();
    }

    /**
     * A pure Java presenter.
     * <p>
     * Ideally the presenter implementation should be platform agnostic and have no or platform agnostic references to
     * things like Android resources, etc. This allows easier porting to multiple platforms using translation tools
     */
    interface Presenter<UiT extends Ui>
            extends PresenterLifecycleEvents {
        boolean shouldRetainPresenter();


        /**
         * Bind the {@link Ui} to the {@link Presenter}. Implementations should notify
         * appropriate lifecycle events after binding is complete
         * (i.e. {@link Presenter#onPresenterBound()}.
         */
        void bindUi(@NotNull UiT ui);

        /**
         * Unbind the {@link Ui} from the {@link Presenter}. Implementations should notify
         * appropriate lifecycle events after unbinding is complete
         * (i.e. {@link Presenter#onPresenterUnBound()}.
         */
        void unBindUi();


        boolean isUiAttached();

        UiT getUi();
    }

    /**
     * Clean architecture data layer interactor (inter-actor). Coordinates data retrieval and
     * notifies the {@link Presenter}
     */
    interface InterActor {
    }
}