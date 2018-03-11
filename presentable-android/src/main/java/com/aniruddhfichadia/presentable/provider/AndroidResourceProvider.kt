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
package com.aniruddhfichadia.presentable.provider


import android.content.Context
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.support.v4.content.ContextCompat
import com.aniruddhfichadia.presentable.provider.ResourceProvider.ColorProvider
import com.aniruddhfichadia.presentable.provider.ResourceProvider.LocaleProvider
import com.aniruddhfichadia.presentable.provider.ResourceProvider.StringProvider
import java.util.*


/**
 * @author Aniruddh Fichadia | Email: Ani.Fichadia@gmail.com | GitHub: AniFichadia (http://github.com/AniFichadia)
 */
open class AndroidResourceProvider(
        private val context: Context
) : StringProvider, ColorProvider, LocaleProvider {
    private val packageName: String = context.packageName


    //region String
    override fun getString(identifier: String): String {
        return lookupString(identifier)
    }

    override fun getString(identifier: String, vararg formatArgs: Any): String {
        val string = getString(identifier)

        return if (formatArgs.isNotEmpty())
            String.format(string, *formatArgs)
        else
            string
    }

    protected open fun lookupString(identifier: String): String {
        return context.getString(
                context.resources.getIdentifier(identifier, DEF_TYPE_STRING, packageName)
        )
    }
    //endregion

    //region Color
    override fun getColor(identifier: String): Int {
        return lookupColor(identifier)
    }

    protected fun lookupColor(identifier: String): Int {
        return ContextCompat.getColor(
                context, context.resources.getIdentifier(identifier, DEF_TYPE_COLOR, packageName)
        )
    }
    //endregion

    //region Locale
    override fun getLocale(): Locale {
        return if (VERSION.SDK_INT >= VERSION_CODES.N) {
            context.resources.configuration.locales.get(0)
        } else {
            context.resources.configuration.locale
        }
    }
    //endregion


    companion object {
        private const val DEF_TYPE_STRING = "string"
        private const val DEF_TYPE_COLOR = "color"
    }
}
