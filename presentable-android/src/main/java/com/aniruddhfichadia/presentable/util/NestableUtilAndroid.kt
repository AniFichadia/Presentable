/*
 * Copyright (C) 2017 Aniruddh Fichadia
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
package com.aniruddhfichadia.presentable.util

import android.app.Activity
import android.support.v4.app.Fragment
import android.view.View
import com.aniruddhfichadia.presentable.Nestable


/**
 * @author Aniruddh Fichadia
 * @date 2017-08-17
 */
class NestableUtilAndroid {
    companion object {
        fun getNestableParent(activity: Activity): Nestable? {
            // Activities don't have a "parent"
            return null
        }

        fun getNestableParent(fragment: Fragment): Nestable? {
            var nestableParent: Any? = fragment.parentFragment
            if (nestableParent == null) {
                nestableParent = fragment.activity
            }

            return if (nestableParent is Nestable) {
                nestableParent
            } else {
                null
            }
        }

        fun getNestableParent(view: View): Nestable? {
            var nestableParent: Any = view.parent
            // Try the ViewParent
            if (nestableParent is Nestable) {
                return nestableParent
            }

            // Try the Context
            nestableParent = view.context
            return if (nestableParent is Nestable) {
                nestableParent
            } else {
                null
            }
        }
    }
}
