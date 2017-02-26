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


/**
 * @author Aniruddh Fichadia | Email: Ani.Fichadia@gmail.com | GitHub: AniFichadia (http://github.com/AniFichadia)
 * @date 2017-02-25
 */
public class PresentableUiDelegateImpl {
    private static final String KEY_PRESENTER = "presenter";


    public static <PresenterT extends Presenter> PresenterT createOrRestorePresenter(@NonNull PresentableUiAndroid<PresenterT> ui,
                                                                                     @Nullable Bundle savedInstanceState) {
        PresenterT presenter;

        if (savedInstanceState == null || !savedInstanceState.containsKey(KEY_PRESENTER)) {
            presenter = ui.createPresenter();
        } else {
            ui.restoreUiState(savedInstanceState);

            presenter = ui.getRegistry().getAndRemove(savedInstanceState.getString(KEY_PRESENTER));
        }

        return presenter;
    }

    public static <PresenterT extends Presenter> void handleSave(@NonNull PresentableUiAndroid<PresenterT> ui,
                                                                 @NonNull Bundle outState) {
        if (ui.getPresenter().shouldRetainPresenter()) {
            String presenterKey = ui.getRegistry().put(ui.getPresenter());
            outState.putString(KEY_PRESENTER, presenterKey);
        }

        ui.saveUiState(outState);
    }
}