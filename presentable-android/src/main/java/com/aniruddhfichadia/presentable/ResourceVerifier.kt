/*
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
package com.aniruddhfichadia.presentable


import java.lang.reflect.Field
import java.util.*


/**
 * @author Aniruddh Fichadia | Email: Ani.Fichadia@gmail.com | GitHub: AniFichadia (http://github.com/AniFichadia)
 * @date 3/1/17
 */
class ResourceVerifier {
    companion object {
        private val IGNORED_FIELD_NAMES = setOf("serialVersionUID", "\$change")


        @Throws(RuntimeException::class)
        fun verifyResourcesPresent(androidResFileClass: Class<*>, presentableResClass: Class<*>) {
            val declaredFields = presentableResClass.declaredFields

            val missingFields = ArrayList<Field>()
            for (field in declaredFields) {
                val fieldName = field.name
                if (IGNORED_FIELD_NAMES.contains(fieldName)) {
                    continue
                }

                try {
                    androidResFileClass.getField(fieldName)
                } catch (e: NoSuchFieldException) {
                    missingFields.add(field)
                }
            }

            if (!missingFields.isEmpty()) {
                val sb = StringBuilder("The following fields are not declared in ")
                        .append(androidResFileClass.name)

                for (missingField in missingFields) {
                    sb.append('\n')
                    sb.append("\t - ")
                    sb.append(missingField.name)
                }

                throw RuntimeException(sb.toString())
            }
        }
    }
}
