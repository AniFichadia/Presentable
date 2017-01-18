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
package com.aniruddhfichadia.presentableexample.demo;


import com.aniruddhfichadia.presentable.PresenterUi;
import com.aniruddhfichadia.replayableinterface.Replayable;


/**
 * @author Aniruddh Fichadia
 * @date 17/1/17
 */
@Replayable
public interface DemoUi
        extends PresenterUi {
    void showLoading();

    void hideLoading();

    void setMessage(String text);

    void setLoadingAllowed(boolean allowed);
}