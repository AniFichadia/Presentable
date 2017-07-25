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
package com.aniruddhfichadia.presentable.provider;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.util.LruCache;

import com.aniruddhfichadia.presentable.provider.AndroidResourceProvider;

import org.jetbrains.annotations.NotNull;


/**
 * @author Aniruddh Fichadia | Email: Ani.Fichadia@gmail.com | GitHub: AniFichadia (http://github.com/AniFichadia)
 */
public class CachingAndroidResourceProvider
        extends AndroidResourceProvider {
    public static final int DEFAULT_CACHE_SIZE = 100;


    @NonNull
    private final LruCache<String, String> stringLookupCache;


    public CachingAndroidResourceProvider(@NonNull Context context) {
        this(context, DEFAULT_CACHE_SIZE);
    }

    public CachingAndroidResourceProvider(@NonNull Context context, int cacheSize) {
        super(context);

        stringLookupCache = new LruCache<>(cacheSize);
    }


    @Override
    protected String lookupString(@NotNull String identifier) {
        String string = stringLookupCache.get(identifier);

        if (string == null) {
            string = super.lookupString(identifier);

            stringLookupCache.put(identifier, string);
        }

        return string;
    }
}