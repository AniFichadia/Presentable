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
 * TODO-ani: re-implement as a 'binder'
 *
 * @author Aniruddh Fichadia | Email: Ani.Fichadia@gmail.com | GitHub: AniFichadia (http://github.com/AniFichadia)
 * @date 2017-02-25
 */
public class PresentableUiDelegateImpl {
    private static final String KEY_PRESENTER = "presenter";


    public static <PresenterT extends Presenter> PresenterT createOrRestorePresenter(
            @NonNull PresentableUiAndroid<PresenterT> ui, @Nullable Bundle savedInstanceState) {
        PresenterT presenter = null;

        String bundleKey = generateBundleKeyForUi(ui);
        if (savedInstanceState != null && savedInstanceState.containsKey(bundleKey)) {
            presenter = ui.getRegistry().get(savedInstanceState.getString(bundleKey));
        }

        if (presenter == null) {
            // Create a new presenter instance
            presenter = ui.createPresenter();

            ui.onNewInstance();
        } else {
            // The presenter has been retained, restore the UI state
            ui.restoreUiState(savedInstanceState);
        }

        return presenter;
    }

    public static <PresenterT extends Presenter> void savePresenter(@NonNull PresentableUiAndroid<PresenterT> ui,
                                                                    @NonNull Bundle outState) {
        PresenterT presenter = ui.getPresenter();
        if (presenter.shouldRetainPresenter()) {
            String presenterKey = ui.getRegistry().put(presenter);
            if (presenterKey != null) {
                // Registry has persisted a value, save its key
                outState.putString(generateBundleKeyForUi(ui), presenterKey);
            }
        }

        ui.saveUiState(outState);
    }


    /**
     * Generates a key that includes the UI classes name. This prevents the keys getting
     * overwritten by other UI elements. This may happen in UIs with nested fragments.
     */
    public static String generateBundleKeyForUi(@NonNull PresentableUiAndroid<?> ui) {
        return ui.getClass().getName() + "." + KEY_PRESENTER;
    }
}