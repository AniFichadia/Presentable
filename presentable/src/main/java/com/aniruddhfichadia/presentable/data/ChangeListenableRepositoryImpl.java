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
package com.aniruddhfichadia.presentable.data;


import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;


/**
 * Basic implementation of {@link ChangeListenableRepository}. Either extend from or compose your
 * repository implementation using this
 *
 * @author Aniruddh Fichadia | Email: Ani.Fichadia@gmail.com | GitHub: AniFichadia (http://github.com/AniFichadia)
 * @date 27/12/16
 */
public class ChangeListenableRepositoryImpl
        implements ChangeListenableRepository {
    @Nullable
    private List<RepositoryChangeListener> listeners;


    @Override
    public void notifyRepositoryUpdated(String key) {
        if (listeners != null) {
            for (RepositoryChangeListener listener : listeners) {
                listener.onRepositoryChanged(key);
            }
        }
    }

    @Override
    public void addListener(RepositoryChangeListener listener) {
        if (listeners == null) {
            listeners = new ArrayList<>();
        }

        listeners.add(listener);
    }

    @Override
    public void removeListener(RepositoryChangeListener listener) {
        if (listeners != null) {
            listeners.remove(listener);
        }
    }
}