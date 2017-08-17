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
package com.aniruddhfichadia.presentable;


import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


/**
 * @author Aniruddh Fichadia | Email: Ani.Fichadia@gmail.com | GitHub: AniFichadia (http://github.com/AniFichadia)
 * @date 3/1/17
 */
public class ResourceVerifier {
    public static void verifyResourcesPresent(Class<?> androidResFileClass, Class<?> presentableResClass)
            throws RuntimeException {
        Field[] declaredFields = presentableResClass.getDeclaredFields();


        List<Field> missingFields = new ArrayList<>();
        for (Field field : declaredFields) {
            String fieldName = field.getName();
            try {
                androidResFileClass.getField(fieldName);
            } catch (NoSuchFieldException e) {
                missingFields.add(field);
            }
        }

        if (!missingFields.isEmpty()) {
            StringBuilder sb = new StringBuilder("The following fields are not declared in ")
                    .append(androidResFileClass.getName());

            for (Field missingField : missingFields) {
                sb.append('\n');
                sb.append("\t - ");
                sb.append(missingField.getName());
            }

            throw new RuntimeException(sb.toString());
        }
    }
}