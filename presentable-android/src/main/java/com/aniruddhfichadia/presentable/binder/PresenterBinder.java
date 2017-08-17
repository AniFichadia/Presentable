/**
 * Copyright (C) 2017 Aniruddh Fichadia
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
package com.aniruddhfichadia.presentable.binder;


import android.support.annotation.NonNull;


/**
 * @author Aniruddh Fichadia
 * @date 2017-08-17
 */
public abstract class PresenterBinder<BoundT> {
    private static final String KEY_PRESENTER = "presenter";


    public abstract void registerBinding(@NonNull BoundT bound);

    public abstract void unregisterBinding(@NonNull BoundT bound);


    /**
     * Generates a key that includes the UI classes name. This prevents the keys getting
     * overwritten by other UI elements. This may happen in UIs with nested fragments.
     */
    public static String generateBundleKeyForUi(@NonNull Object ui) {
        return ui.getClass().getName() + "." + KEY_PRESENTER;
    }
}