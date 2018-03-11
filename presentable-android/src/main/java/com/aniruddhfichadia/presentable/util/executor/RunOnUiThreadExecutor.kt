/*
 * Copyright (C) 2017 Aniruddh Fichadia
 *
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 *
 * You should have received a copy of the GNU General Public License along with this program. If not, see
 * <http:></http:>//www.gnu.org/licenses/>.
 *
 *
 * If you use or enhance the code, please let me know using the provided author information or via email
 * Ani.Fichadia@gmail.com.
 */
package com.aniruddhfichadia.presentable.util.executor


import android.os.Handler
import android.os.Looper

import java.util.concurrent.Executor


/**
 * Imitates [android.app.Activity.runOnUiThread] behaviour when calling [.execute]
 *
 * @author Aniruddh Fichadia
 * @date 2017-07-25
 */
class RunOnUiThreadExecutor : Executor {
    private val mainHandler: Handler = Handler(Looper.getMainLooper())


    override fun execute(command: Runnable) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            // This is the main looper/thread, just execute the runnable
            command.run()
        } else {
            // Not the main looper, post event on it
            mainHandler.post(command)
        }
    }
}
