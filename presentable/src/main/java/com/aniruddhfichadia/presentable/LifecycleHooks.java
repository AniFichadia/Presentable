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


import org.jetbrains.annotations.Nullable;

import java.io.Serializable;


/**
 * UI lifecycle hooks. Allows linking between a {@link Presenter} and its corresponding {@link PresenterUi}'s
 * lifecycle.
 * <p>
 * This is modelled similarly to Android's lifecycle events, but makes some abstractions for saving and restoring a
 * presenters state ({@link #onSave()} and {@link #onRestore(PresenterState)}). Each lifecycle event should be
 * relatively self explanatory.
 *
 * @author Aniruddh Fichadia | Email: Ani.Fichadia@gmail.com | GitHub: AniFichadia (http://github.com/AniFichadia)
 */
public interface LifecycleHooks {
    void onCreate();

    void onDestroy();

    void onResume();

    void onPause();

    @Nullable
    PresenterState onSave();

    void onRestore(@Nullable PresenterState savedState);


    /**
     * A savable representation of a {@link Presenter}'s state. Allows {@link Presenter}'s to be saved and restored
     * based on lifecycle events, such as Android's Fragment.onSaveInstanceState(Bundle) and
     * Fragment.onViewStateRestore(Bundle)
     */
    interface PresenterState
            extends Serializable {
    }


    /**
     * If you don't want to implement every lifecycle method. This class allows you to implement only the required
     * methods. Super calls won't be necessary ... since the method bodies are empty.
     */
    class SparseLifecycleHooks
            implements LifecycleHooks {
        @Override
        public void onCreate() {
        }

        @Override
        public void onResume() {
        }

        @Override
        public void onPause() {
        }

        @Nullable
        @Override
        public PresenterState onSave() {
            return null;
        }

        @Override
        public void onRestore(@Nullable PresenterState savedState) {
        }

        @Override
        public void onDestroy() {
        }
    }


    /**
     * Class name says it all, handles no lifecycle events. This is a convenience class since {@link Presenter}'s must
     * provide a non-null {@link LifecycleHooks}
     */
    final class NoLifecycleHooks
            extends SparseLifecycleHooks {
    }
}