/*
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
package com.aniruddhfichadia.presentable


import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.annotation.CallSuper
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aniruddhfichadia.presentable.Contract.Presenter
import com.aniruddhfichadia.presentable.Contract.Ui
import com.aniruddhfichadia.presentable.binder.LifecycleBinderFragment.BindingLifecycleCallbacks
import com.aniruddhfichadia.presentable.util.NestableUtilAndroid


/**
 * A [Fragment] with appropriate hook-ins and abstractions to interact with a [Presenter]. Also adds a bit
 * of convenience for view creation.
 *
 *
 * [Presenter] instantiation occurs in the constructor.
 *
 *
 * For applications that require dependency injection (eg. Dagger), you can implement the [.inject] method.
 *
 *
 *
 * @author Aniruddh Fichadia | Email: Ani.Fichadia@gmail.com | GitHub: AniFichadia (http://github.com/AniFichadia)
 */
abstract class PresentableFragment<PresenterT : Presenter<UiT>, UiT : Ui> :
        Fragment(),
        PresentableUiAndroid<PresenterT, UiT>,
        ViewBindable,
        Nestable {
    protected val uiHandler: Handler = Handler(Looper.getMainLooper())

    /**
     * Manually implemented lifecycle callbacks. Allows the callback to execute before any
     * overridden lifecycle method implementations
     */
    private val manualLifecycleCallbacks: BindingLifecycleCallbacks<*, *, *> = BindingLifecycleCallbacks(this)

    override lateinit var presenter: PresenterT

    @Suppress("UNCHECKED_CAST")
    override val ui: UiT
        get() = this as UiT


    //region Lifecycle
    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        manualLifecycleCallbacks.onFragmentCreated(fragmentManager, this, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        manualLifecycleCallbacks.onFragmentActivityCreated(
                fragmentManager, this, savedInstanceState
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val layoutResource = getLayoutResource()

        return if (layoutResource > 0) {
            val view = inflater.inflate(layoutResource, container, false)

            bindView(view)
            afterBindView(view)

            view
        } else {
            null
        }
    }

    override fun onStart() {
        super.onStart()

        manualLifecycleCallbacks.onFragmentStarted(fragmentManager, this)
    }

    override fun onResume() {
        super.onResume()

        manualLifecycleCallbacks.onFragmentResumed(fragmentManager, this)
    }

    override fun onPause() {
        super.onPause()

        manualLifecycleCallbacks.onFragmentPaused(fragmentManager, this)
    }

    override fun onStop() {
        super.onStop()

        manualLifecycleCallbacks.onFragmentStopped(fragmentManager, this)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        manualLifecycleCallbacks.onFragmentSaveInstanceState(fragmentManager, this, outState)
    }

    override fun onDestroyView() {
        super.onDestroyView()

        manualLifecycleCallbacks.onFragmentViewDestroyed(fragmentManager, this)
    }

    override fun onDestroy() {
        super.onDestroy()

        manualLifecycleCallbacks.onFragmentDestroyed(fragmentManager, this)
    }
    //endregion

    //region PresentableUiAndroid
    override fun beforeOnCreate(savedInstanceState: Bundle?) {}

    override fun afterOnCreate(savedInstanceState: Bundle?) {}

    override fun saveUiState(outState: Bundle) {}

    override fun onNewInstance() {}

    override fun restoreUiState(savedState: Bundle) {}


    override fun inject() {}

    abstract override fun getRegistry(): Registry


    abstract override fun createPresenter(): PresenterT
    //endregion

    //region ViewBindable
    @LayoutRes
    abstract override fun getLayoutResource(): Int

    override fun bindView(view: View) {}

    override fun afterBindView(view: View) {}

    override fun unbindView() {}
    //endregion

    //region Nestable
    override val nestableParent: Nestable?
        get() = NestableUtilAndroid.getNestableParent(this)
    //endregion


    /** A [Fragment] equivalent of [android.app.Activity.runOnUiThread].  */
    protected fun runOnUiThread(runnable: Runnable) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            // This is the main looper/thread, just execute the runnable
            runnable.run()
        } else {
            // Not the main looper, post event on it
            uiHandler.post(runnable)
        }
    }
}