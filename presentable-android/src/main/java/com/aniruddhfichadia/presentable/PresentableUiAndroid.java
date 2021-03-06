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


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.aniruddhfichadia.presentable.Contract.Presenter;
import com.aniruddhfichadia.presentable.Contract.Ui;


/**
 * @author Aniruddh Fichadia | Email: Ani.Fichadia@gmail.com | GitHub: AniFichadia (http://github.com/AniFichadia)
 * @date 2017-02-25
 */
public interface PresentableUiAndroid<PresenterT extends Presenter<UiT>, UiT extends Ui> {
    //region Lifecycle
    void beforeOnCreate(@Nullable Bundle savedInstanceState);

    /**
     * Since onCreate... methods are intentionally final, this allows you to perform initialisation
     */
    void afterOnCreate(@Nullable Bundle savedInstanceState);

    /**
     * Persist the important UI state using the provided {@link Bundle}.
     * <p>
     * You may have to persist properties of views that don't immediately restore their state,
     * such as {@link android.widget.TextView}'s text value.
     */
    void saveUiState(@NonNull Bundle outState);

    void onNewInstance();

    /** Restore important UI state. */
    void restoreUiState(@NonNull Bundle savedState);
    //endregion


    void inject();

    @NonNull
    Registry getRegistry();


    //region Presenter

    /**
     * Provide your {@link Presenter} instance through this method. If no presenter is required,
     * return {@link DoNotPresent}
     */
    @NonNull
    PresenterT createPresenter();

    PresenterT getPresenter();

    void setPresenter(PresenterT presenter);
    //endregion


    UiT getUi();
}