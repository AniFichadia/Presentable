/*
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
package com.aniruddhfichadia.presentable.binder


import android.view.View
import android.view.View.OnAttachStateChangeListener
import com.aniruddhfichadia.presentable.Contract.Presenter
import com.aniruddhfichadia.presentable.Contract.Ui
import com.aniruddhfichadia.presentable.PresentableUiAndroid
import java.lang.ref.WeakReference


/**
 * @author Aniruddh Fichadia
 * @date 2017-08-17
 */
class LifecycleBinderView<in ViewT, PresenterT, UiT> :
        LifecycleBinder<ViewT, PresenterT, UiT>()
        where ViewT : View,
              ViewT : PresentableUiAndroid<PresenterT, UiT>,
              PresenterT : Presenter<UiT>,
              UiT : Ui {
    private var onAttachStateChangeListener: BindingOnAttachStateChangeListener<ViewT, PresenterT, UiT>? = null


    /** Call this in your [View]'s constructor  */
    override fun registerBinding(bound: ViewT) {
        onAttachStateChangeListener = BindingOnAttachStateChangeListener(bound)

        bound.addOnAttachStateChangeListener(onAttachStateChangeListener)
    }

    override fun unregisterBinding(bound: ViewT) {
        if (onAttachStateChangeListener != null) {
            bound.removeOnAttachStateChangeListener(onAttachStateChangeListener)
            onAttachStateChangeListener = null
        }
    }


    class BindingOnAttachStateChangeListener<out ViewT, PresenterT, UiT>(boundView: ViewT) :
            OnAttachStateChangeListener
            where ViewT : View,
                  ViewT : PresentableUiAndroid<PresenterT, UiT>,
                  PresenterT : Presenter<UiT>,
                  UiT : Ui {
        private val boundReference: WeakReference<ViewT> = WeakReference(boundView)


        init {
            boundView.inject()

            boundView.beforeOnCreate(null)

            // Create a new presenter instance
            boundView.presenter = boundView.createPresenter()

            boundView.onNewInstance()

            boundView.afterOnCreate(null)
        }


        override fun onViewAttachedToWindow(v: View) {
            val boundView = getOperableBinding(v)

            if (boundView != null) {
                boundView.presenter.bindUi(boundView.ui)
                boundView.presenter.onUiReady(boundView.ui)
            }
        }

        override fun onViewDetachedFromWindow(v: View) {
            val boundView = getOperableBinding(v)

            boundView?.presenter?.unBindUi()
        }


        protected fun getOperableBinding(v: View): ViewT? {
            return if (isSameInstance(v)) boundReference.get() else null
        }

        protected fun isSameInstance(v: View): Boolean {
            val boundView = boundReference.get()

            return boundView !== null && boundView === v
        }
    }
}
