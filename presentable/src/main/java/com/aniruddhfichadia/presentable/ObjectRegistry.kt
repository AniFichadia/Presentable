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

import java.util.*


/**
 * A default implementation of [Registry]. Stores entries in a [HashMap].
 *
 * @author Aniruddh Fichadia | Email: Ani.Fichadia@gmail.com | GitHub: AniFichadia (http://github.com/AniFichadia)
 * @date 2017-01-17
 */
class ObjectRegistry : Registry {
    private val objectRegistry: MutableMap<String, Any>

    init {
        objectRegistry = HashMap()
    }


    override fun <T> put(value: T): String? {
        val key = UUID.randomUUID().toString()
        objectRegistry[key] = (value as Any)
        return key
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T> get(key: String): T? {
        return objectRegistry.remove(key) as T
    }

    override fun clear() {
        objectRegistry.clear()
    }


    override fun toString(): String {
        return "ObjectRegistry{" +
                "Size=" + objectRegistry.size +
                '}'.toString()
    }
}
