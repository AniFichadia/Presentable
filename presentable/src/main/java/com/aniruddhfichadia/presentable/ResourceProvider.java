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

import org.jetbrains.annotations.NotNull;

/**
 * @author Aniruddh Fichadia | Email: Ani.Fichadia@gmail.com | GitHub: AniFichadia (http://github.com/AniFichadia)
 */
public class ResourceProvider {
    @NotNull
    public final StringProvider string;
    @NotNull
    public final ColorProvider  color;
    // More to come?


    public ResourceProvider(@NotNull StringProvider string, @NotNull ColorProvider color) {
        this.string = string;
        this.color = color;
    }


    public interface StringProvider {
        String getString(@NotNull String identifier);

        String getString(@NotNull String identifier, Object... formatArgs);
    }


    public interface ColorProvider {
        int getColor(@NotNull String identifier);
    }
}