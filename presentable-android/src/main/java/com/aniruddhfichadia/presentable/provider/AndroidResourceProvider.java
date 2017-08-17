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
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

import com.aniruddhfichadia.presentable.provider.ResourceProvider.ColorProvider;
import com.aniruddhfichadia.presentable.provider.ResourceProvider.LocaleProvider;
import com.aniruddhfichadia.presentable.provider.ResourceProvider.StringProvider;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;


/**
 * @author Aniruddh Fichadia | Email: Ani.Fichadia@gmail.com | GitHub: AniFichadia (http://github.com/AniFichadia)
 */
public class AndroidResourceProvider
        implements StringProvider, ColorProvider, LocaleProvider {
    private static final String DEF_TYPE_STRING = "string";
    private static final String DEF_TYPE_COLOR  = "color";


    @NonNull
    private final Context context;
    @NonNull
    private final String  packageName;


    public AndroidResourceProvider(@NonNull Context context) {
        this.context = context;
        this.packageName = context.getPackageName();
    }


    //region String
    @Override
    public String getString(@NotNull String identifier) {
        return lookupString(identifier);
    }

    @Override
    public String getString(@NotNull String identifier, Object... formatArgs) {
        String string = getString(identifier);

        return formatArgs.length > 0 ? String.format(string, formatArgs)
                                     : string;
    }

    protected String lookupString(@NotNull String identifier) {
        return context.getString(context.getResources()
                                        .getIdentifier(identifier, DEF_TYPE_STRING, packageName));
    }
    //endregion

    //region Color
    @Override
    public int getColor(@NotNull String identifier) {
        return lookupColor(identifier);
    }

    protected int lookupColor(@NotNull String identifier) {
        return ContextCompat
                .getColor(context, context.getResources()
                                          .getIdentifier(identifier, DEF_TYPE_COLOR, packageName));
    }
    //endregion

    //region Locale
    @SuppressWarnings("deprecation")
    @Override
    public Locale getLocale() {
        if (VERSION.SDK_INT >= VERSION_CODES.N) {
            return context.getResources().getConfiguration().getLocales().get(0);
        } else {
            return context.getResources().getConfiguration().locale;
        }
    }
    //endregion
}