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


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentManager.FragmentLifecycleCallbacks
import android.view.View
import com.aniruddhfichadia.presentable.Contract.Presenter
import com.aniruddhfichadia.presentable.Contract.Ui
import com.aniruddhfichadia.presentable.PresentableUiAndroid
import com.aniruddhfichadia.presentable.ViewBindable
import java.lang.ref.WeakReference


/**
 * @author Aniruddh Fichadia
 * @date 2017-07-25
 */
class LifecycleBinderFragment<in FragmentT, PresenterT, UiT> :
        LifecycleBinder<FragmentT, PresenterT, UiT>()
        where FragmentT : Fragment,
              FragmentT : PresentableUiAndroid<PresenterT, UiT>,
              PresenterT : Presenter<UiT>,
              UiT : Ui {
    private var binder: BindingLifecycleCallbacks<FragmentT, PresenterT, UiT>? = null


    /** Call this in [Fragment.onAttach]  */
    override fun registerBinding(bound: FragmentT) {
        binder = BindingLifecycleCallbacks(bound)
        bound.fragmentManager!!.registerFragmentLifecycleCallbacks(binder, false)
    }

    /** Call this in [Fragment.onDetach]  */
    override fun unregisterBinding(bound: FragmentT) {
        if (binder != null) {
            bound.fragmentManager!!.unregisterFragmentLifecycleCallbacks(binder)
            binder = null
        }
    }


    class BindingLifecycleCallbacks<out FragmentT, PresenterT, UiT>(boundFragment: FragmentT) :
            FragmentLifecycleCallbacks()
            where FragmentT : Fragment,
                  FragmentT : PresentableUiAndroid<PresenterT, UiT>,
                  PresenterT : Presenter<UiT>,
                  UiT : Ui {
        private val boundReference: WeakReference<FragmentT> = WeakReference(boundFragment)


        override fun onFragmentCreated(fm: FragmentManager?, f: Fragment?, savedInstanceState: Bundle?) {
            super.onFragmentCreated(fm, f, savedInstanceState)

            val boundFragment = getOperableBinding(fm, f)
            if (boundFragment != null) {
                boundFragment.inject()

                boundFragment.beforeOnCreate(savedInstanceState)

                var presenter: PresenterT? = null
                val bundleKey = LifecycleBinder.generateBundleKeyForUi(boundFragment)
                if (savedInstanceState != null && savedInstanceState.containsKey(bundleKey)) {
                    presenter = boundFragment.getRegistry().get<PresenterT>(savedInstanceState.getString(bundleKey))

                    presenter?.let {  boundFragment.presenter = it }
                }

                if (presenter == null) {
                    // Create a new presenter instance
                    presenter = boundFragment.createPresenter()
                    boundFragment.presenter = presenter

                    boundFragment.onNewInstance()
                }

                boundFragment.afterOnCreate(savedInstanceState)
            }
        }

        override fun onFragmentStarted(fm: FragmentManager?, f: Fragment?) {
            super.onFragmentStarted(fm, f)

            val boundFragment = getOperableBinding(fm, f)
            boundFragment?.presenter?.bindUi(boundFragment.ui)
        }

        override fun onFragmentViewCreated(fm: FragmentManager?, f: Fragment?, v: View, savedInstanceState: Bundle?) {
            super.onFragmentViewCreated(fm, f, v, savedInstanceState)

            // Note: View inflation and binding cannot be handled here. Do this in your fragment
            // onCreateView implementation
        }

        override fun onFragmentActivityCreated(fm: FragmentManager?, f: Fragment?, savedInstanceState: Bundle?) {
            super.onFragmentActivityCreated(fm, f, savedInstanceState)

            val boundFragment = getOperableBinding(fm, f)
            if (boundFragment != null && savedInstanceState != null) {
                boundFragment.restoreUiState(savedInstanceState)
            }
        }


        override fun onFragmentResumed(fm: FragmentManager?, f: Fragment?) {
            super.onFragmentResumed(fm, f)

            val boundFragment = getOperableBinding(fm, f)
            boundFragment?.presenter?.onUiReady(boundFragment.ui)
        }


        override fun onFragmentStopped(fm: FragmentManager?, f: Fragment?) {
            super.onFragmentStopped(fm, f)

            val boundFragment = getOperableBinding(fm, f)
            boundFragment?.presenter?.unBindUi()
        }

        override fun onFragmentViewDestroyed(fm: FragmentManager?, f: Fragment?) {
            super.onFragmentViewDestroyed(fm, f)

            val boundFragment = getOperableBinding(fm, f)
            if (boundFragment != null && boundFragment is ViewBindable) {
                (boundFragment as ViewBindable).unbindView()
            }
        }


        override fun onFragmentSaveInstanceState(fm: FragmentManager?, f: Fragment?, outState: Bundle?) {
            super.onFragmentSaveInstanceState(fm, f, outState)

            val boundFragment = getOperableBinding(fm, f)
            if (boundFragment != null) {
                val presenter = boundFragment.presenter
                if (presenter.shouldRetainPresenter()) {
                    val presenterKey = boundFragment.getRegistry().put<Any>(presenter)
                    if (presenterKey != null) {
                        // Registry has persisted a value, save its key
                        outState!!.putString(LifecycleBinder.generateBundleKeyForUi(boundFragment), presenterKey)
                    }
                }

                boundFragment.saveUiState(outState!!)
            }
        }


        protected fun getOperableBinding(fm: FragmentManager?, f: Fragment?): FragmentT? {
            if (isSameInstance(f)) {
                return boundReference.get()
            } else {
                unregisterIfInstanceUnbound(fm)
                return null
            }
        }

        protected fun isSameInstance(f: Fragment?): Boolean {
            val boundFragment = boundReference.get()

            return boundFragment !== null && boundFragment === f
        }

        protected fun unregisterIfInstanceUnbound(fm: FragmentManager?) {
            val boundFragment = boundReference.get()

            if (boundFragment == null) {
                fm!!.unregisterFragmentLifecycleCallbacks(this)
            }
        }
    }
}
