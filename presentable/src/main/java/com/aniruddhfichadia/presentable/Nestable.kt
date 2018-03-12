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
package com.aniruddhfichadia.presentable

import kotlin.reflect.full.isSubclassOf


/**
 * Represents a element (such as a UI) that can be nested within something else
 *
 * @author Aniruddh Fichadia
 * @date 2017-07-24
 */
interface Nestable {
    val nestableParent: Nestable?


    fun <ClassT> findParentWithImplementation(nestable: Nestable, clazz: Class<ClassT>): ClassT? {
        var parent = nestable.nestableParent
        while (parent != null && !parent::class.isSubclassOf(clazz::class)) {
            parent = parent.nestableParent
        }

        return if (parent != null) {
            @Suppress("UNCHECKED_CAST")
            parent as ClassT
        } else {
            null
        }
    }
}
