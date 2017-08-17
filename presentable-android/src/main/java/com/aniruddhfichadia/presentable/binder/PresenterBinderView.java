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
package com.aniruddhfichadia.presentable.binder;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.View.OnAttachStateChangeListener;

import com.aniruddhfichadia.presentable.Contract.Presenter;
import com.aniruddhfichadia.presentable.Contract.Ui;
import com.aniruddhfichadia.presentable.PresentableUiAndroid;

import java.lang.ref.WeakReference;


/**
 * @author Aniruddh Fichadia
 * @date 2017-08-17
 */
public class PresenterBinderView<
        ViewT extends View & PresentableUiAndroid<PresenterT, UiT>,
        PresenterT extends Presenter<UiT>,
        UiT extends Ui
        >
        extends PresenterBinder<ViewT> {
    @Nullable
    private BindingOnAttachStateChangeListener onAttachStateChangeListener;

    @Override
    public void registerBinding(@NonNull ViewT bound) {
        onAttachStateChangeListener = new BindingOnAttachStateChangeListener<>(bound);

        bound.addOnAttachStateChangeListener(onAttachStateChangeListener);
    }

    @Override
    public void unregisterBinding(@NonNull ViewT bound) {
        if (onAttachStateChangeListener != null) {
            bound.removeOnAttachStateChangeListener(onAttachStateChangeListener);
            onAttachStateChangeListener = null;
        }
    }


    public static class BindingOnAttachStateChangeListener<
            ViewT extends View & PresentableUiAndroid<PresenterT, UiT>,
            PresenterT extends Presenter<UiT>,
            UiT extends Ui
            >
            implements OnAttachStateChangeListener {
        // TODO: to weak reference or not to weak reference?
        @NonNull
        private final WeakReference<ViewT> boundReference;


        public BindingOnAttachStateChangeListener(@NonNull ViewT boundView) {
            this.boundReference = new WeakReference<>(boundView);

            Bundle savedInstanceState = null;


            boundView.inject();

            boundView.beforeOnCreate(savedInstanceState);

            boundView.bindView(boundView);
            boundView.afterBindView(boundView);

            // Create a new presenter instance
            boundView.setPresenter(boundView.createPresenter());

            boundView.onNewInstance();


            boundView.afterOnCreate(savedInstanceState);
        }


        @Override
        public void onViewAttachedToWindow(View v) {
            if (guaranteeBinding(v)) {
                ViewT boundView = boundReference.get();
                boundView.getPresenter().bindUi(boundView.getUi());
                boundView.getPresenter().onUiReady(boundView.getUi());
            }
        }

        @Override
        public void onViewDetachedFromWindow(View v) {
            if (guaranteeBinding(v)) {
                ViewT boundView = boundReference.get();
                boundView.getPresenter().unBindUi();
            }
        }

        protected boolean guaranteeBinding(View v) {
            ViewT boundView = boundReference.get();

            return boundView != null && boundView == v;
        }
    }
}
