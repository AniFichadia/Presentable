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
package com.aniruddhfichadia.presentable;


import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.View;


/**
 * @author Aniruddh Fichadia | Email: Ani.Fichadia@gmail.com | GitHub: AniFichadia (http://github.com/AniFichadia)
 * @date 28/12/16
 */
public interface ViewBindable {
    /** Provide your layout resource through this method. Use any negative number for no layout */
    @LayoutRes
    int getLayoutResource();

    /**
     * Perform any view binding (eg. via ButterKnife, or using a bunch of {@link View#findViewById(int)} calls). Won't
     * be called if a layout isn't inflated.
     *
     * @param view The inflated layout. Won't be null
     */
    void bindView(@NonNull View view);

    /**
     * Called after {@link #bindView(View)}. Configure your UI elements here
     *
     * @param view The inflated layout. Won't be null
     */
    void afterBindView(@NonNull View view);

    void unbindView();
}