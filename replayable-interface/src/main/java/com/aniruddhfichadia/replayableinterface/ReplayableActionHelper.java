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
package com.aniruddhfichadia.replayableinterface;


import org.jetbrains.annotations.NotNull;

import java.util.UUID;


/**
 * @author Aniruddh Fichadia
 * @date 17/1/17
 */
public class ReplayableActionHelper {
    public static String generateKey(@NotNull String methodSignature, @NotNull ReplayStrategy strategy,
                                     Object... params) throws IllegalStateException {
        switch (strategy) {
            case ENQUEUE:
                return generateKeyForEnqueue();
            case ENQUEUE_LAST_ONLY:
                return generateKeyForEnqueueLastOnly(methodSignature);
            case ENQUEUE_PARAM_UNIQUE:
                return generateKeyForEnqueueParamUnique(methodSignature, params);
            case NONE:
            default:
                throw new IllegalStateException("Strategy (" + strategy + ") cannot be handled");
        }
    }

    public static String generateKeyForEnqueue() {
        return UUID.randomUUID().toString();
    }

    public static String generateKeyForEnqueueLastOnly(String methodSignature) {
        return methodSignature;
    }

    public static String generateKeyForEnqueueParamUnique(String methodSignature, Object... params) {
        return methodSignature + " [" + createSignatureFromParams(params) + "]";
    }


    private static String createSignatureFromParams(Object... params) {
        if (params.length == 0) {
            return "";
        } else {
            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < params.length; i++) {
                Object param = params[i];

                if (param != null) {
                    sb.append(param.getClass().getSimpleName())
                      .append(":")
                      .append(param.hashCode());
                } else {
                    sb.append("null");
                }

                if (i < params.length - 1) {
                    sb.append(",");
                }
            }

            return sb.toString();
        }
    }
}