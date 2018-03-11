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
import android.support.annotation.CallSuper
import android.support.annotation.LayoutRes
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.aniruddhfichadia.presentable.Contract.Presenter
import com.aniruddhfichadia.presentable.Contract.Ui
import com.aniruddhfichadia.presentable.binder.LifecycleBinderActivity.BindingLifecycleCallbacks
import com.aniruddhfichadia.presentable.util.NestableUtilAndroid


/**
 * @author Aniruddh Fichadia | Email: Ani.Fichadia@gmail.com | GitHub: AniFichadia (http://github.com/AniFichadia)
 */
abstract class PresentableActivity<PresenterT : Presenter<UiT>, UiT : Ui> :
        AppCompatActivity(),
        PresentableUiAndroid<PresenterT, UiT>,
        ViewBindable,
        Nestable {
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

        manualLifecycleCallbacks.onActivityCreated(this, savedInstanceState)
    }


    override fun onStart() {
        super.onStart()

        manualLifecycleCallbacks.onActivityStarted(this)
    }

    override fun onResume() {
        super.onResume()

        manualLifecycleCallbacks.onActivityResumed(this)
    }

    override fun onPause() {
        super.onPause()

        manualLifecycleCallbacks.onActivityPaused(this)
    }

    override fun onStop() {
        super.onStop()

        manualLifecycleCallbacks.onActivityStopped(this)
    }

    override fun onDestroy() {
        super.onDestroy()

        manualLifecycleCallbacks.onActivityDestroyed(this)
    }

    public override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)

        manualLifecycleCallbacks.onActivitySaveInstanceState(this, outState)
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
}
