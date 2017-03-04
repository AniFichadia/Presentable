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
package com.aniruddhfichadia.presentableexample;


import android.app.Application;

import com.aniruddhfichadia.presentable.ObjectRegistry;
import com.aniruddhfichadia.presentable.Registry;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


/**
 * @author Aniruddh Fichadia | Email: Ani.Fichadia@gmail.com | GitHub: AniFichadia (http://github.com/AniFichadia)
 * @date 2017-02-25
 */
public class DemoApplication
        extends Application {
    private final Registry registry;
    private final Executor sharedExecutor;


    public DemoApplication() {
        registry = new ObjectRegistry();
        sharedExecutor = Executors.newCachedThreadPool();
    }


    public Registry getRegistry() {
        return registry;
    }

    public Executor getSharedExecutor() {
        return sharedExecutor;
    }
}