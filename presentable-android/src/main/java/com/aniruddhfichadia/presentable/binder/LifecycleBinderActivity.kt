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


import android.app.Activity
import android.app.Application.ActivityLifecycleCallbacks
import android.os.Bundle
import com.aniruddhfichadia.presentable.Contract.Presenter
import com.aniruddhfichadia.presentable.Contract.Ui
import com.aniruddhfichadia.presentable.PresentableUiAndroid
import com.aniruddhfichadia.presentable.ViewBindable
import java.lang.ref.WeakReference


/**
 * @author Aniruddh Fichadia
 * @date 2017-07-25
 */
class LifecycleBinderActivity<in ActivityT, PresenterT, UiT> :
        LifecycleBinder<ActivityT, PresenterT, UiT>()
        where ActivityT : Activity,
              ActivityT : PresentableUiAndroid<PresenterT, UiT>,
              PresenterT : Presenter<UiT>,
              UiT : Ui {
    private var activityLifecycleCallbacks: BindingLifecycleCallbacks<ActivityT, PresenterT, UiT>? = null


    /** Call this in [Activity.onCreate] before calling super  */
    override fun registerBinding(bound: ActivityT) {
        activityLifecycleCallbacks = BindingLifecycleCallbacks(bound)
        bound.application.registerActivityLifecycleCallbacks(activityLifecycleCallbacks)
    }

    /** Call this in [Activity.onDestroy]  */
    override fun unregisterBinding(bound: ActivityT) {
        if (activityLifecycleCallbacks != null) {
            bound.application.unregisterActivityLifecycleCallbacks(activityLifecycleCallbacks)
            activityLifecycleCallbacks = null
        }
    }


    class BindingLifecycleCallbacks<out ActivityT, PresenterT, UiT>(boundActivity: ActivityT) :
            ActivityLifecycleCallbacks
            where ActivityT : Activity,
                  ActivityT : PresentableUiAndroid<PresenterT, UiT>,
                  PresenterT : Presenter<UiT>,
                  UiT : Ui {
        private val boundReference: WeakReference<ActivityT> = WeakReference(boundActivity)


        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            val boundActivity = getOperableBinding(activity)

            if (boundActivity != null) {
                boundActivity.inject()

                boundActivity.beforeOnCreate(savedInstanceState)

                if (boundActivity is ViewBindable) {
                    val viewBindableBoundActivity = boundActivity as ViewBindable?

                    val layoutResource = viewBindableBoundActivity!!.getLayoutResource()
                    if (layoutResource > 0) {
                        boundActivity.setContentView(layoutResource)
                    }

                    val contentView = boundActivity.window.decorView
                    viewBindableBoundActivity.bindView(contentView)
                    viewBindableBoundActivity.afterBindView(contentView)
                }

                var presenter: PresenterT? = null
                val bundleKey = LifecycleBinder.generateBundleKeyForUi(boundActivity)
                if (savedInstanceState != null && savedInstanceState.containsKey(bundleKey)) {
                    presenter = boundActivity.getRegistry().get<PresenterT>(savedInstanceState.getString(bundleKey)!!)

                    presenter?.let {
                        boundActivity.presenter = presenter
                    }
                }

                if (presenter == null) {
                    // Create a new presenter instance
                    boundActivity.presenter = boundActivity.createPresenter()

                    boundActivity.onNewInstance()
                } else {
                    if (savedInstanceState != null) {
                        // The presenter has been retained, restore the UI state
                        boundActivity.restoreUiState(savedInstanceState)
                    }
                }

                boundActivity.afterOnCreate(savedInstanceState)
            }
        }


        override fun onActivityStarted(activity: Activity) {
            val boundActivity = getOperableBinding(activity)

            boundActivity?.presenter?.bindUi(boundActivity.ui)
        }

        override fun onActivityResumed(activity: Activity) {
            val boundActivity = getOperableBinding(activity)

            boundActivity?.presenter?.onUiReady(boundActivity.ui)
        }

        override fun onActivityPaused(activity: Activity) {}

        override fun onActivityStopped(activity: Activity) {
            val boundActivity = getOperableBinding(activity)

            boundActivity?.presenter?.unBindUi()
        }

        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
            val boundActivity = getOperableBinding(activity)

            if (boundActivity != null) {
                val presenter = boundActivity.presenter
                if (presenter.shouldRetainPresenter()) {
                    val presenterKey = boundActivity.getRegistry().put<Any>(presenter)
                    if (presenterKey != null) {
                        // Registry has persisted a value, save its key
                        outState.putString(LifecycleBinder.generateBundleKeyForUi(boundActivity), presenterKey)
                    }
                }

                boundActivity.saveUiState(outState)
            }
        }

        override fun onActivityDestroyed(activity: Activity) {
            val boundActivity = getOperableBinding(activity)

            if (boundActivity != null) {
                boundActivity.presenter.unBindUi()

                if (boundActivity is ViewBindable) {
                    // Unbinding is unnecessary in Activities, just use a no-op in your
                    // unbindView method unless this is explicitly necessary
                    (boundActivity as ViewBindable).unbindView()
                }
            }
        }


        protected fun getOperableBinding(activity: Activity): ActivityT? {
            return if (isSameInstance(activity)) {
                boundReference.get()
            } else {
                unregisterIfInstanceUnbound(activity)
                null
            }
        }

        protected fun isSameInstance(activity: Activity): Boolean {
            val boundActivity = boundReference.get()

            return boundActivity !== null && boundActivity === activity
        }

        protected fun unregisterIfInstanceUnbound(activity: Activity) {
            val boundActivity = boundReference.get()

            if (boundActivity == null) {
                activity.application.unregisterActivityLifecycleCallbacks(this)
            }
        }
    }
}
