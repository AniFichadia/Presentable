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


import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


/**
 * A default implementation of {@link Registry}. Stores entries in a {@link HashMap}.
 *
 * @author Aniruddh Fichadia | Email: Ani.Fichadia@gmail.com | GitHub: AniFichadia (http://github.com/AniFichadia)
 * @date 2017-01-17
 */
public class ObjectRegistry
        implements Registry {
    private final Map<String, Object> objectRegistry;


    public ObjectRegistry() {
        objectRegistry = new HashMap<>();
    }


    @Override
    public <T> String put(T value) {
        String key = UUID.randomUUID().toString();
        objectRegistry.put(key, value);
        return key;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T get(String key) {
        return (T) objectRegistry.remove(key);
    }

    @Override
    public void clear() {
        objectRegistry.clear();
    }
}