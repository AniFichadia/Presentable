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
package com.aniruddhfichadia.presentable.util;


import android.app.Activity;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.aniruddhfichadia.presentable.Nestable;


/**
 * @author Aniruddh Fichadia
 * @date 2017-08-17
 */
public class NestableUtilAndroid {
    @Nullable
    public static Nestable getNestableParent(Activity activity) {
        // Activities don't have a "parent"
        return null;
    }

    @Nullable
    public static Nestable getNestableParent(Fragment fragment) {
        Object nestableParent = fragment.getParentFragment();
        if (nestableParent == null) {
            nestableParent = fragment.getActivity();
        }

        if (nestableParent instanceof Nestable) {
            return (Nestable) nestableParent;
        } else {
            return null;
        }
    }


    @Nullable
    public static Nestable getNestableParent(View view) {
        Object nestableParent = view.getParent();

        if (nestableParent instanceof Nestable) {
            return (Nestable) nestableParent;
        } else {
            return null;
        }
    }
}
