package com.aniruddhfichadia.presentableexample.lifecyclebinding;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.aniruddhfichadia.presentable.DoNotPresent;
import com.aniruddhfichadia.presentable.PresentableUiAndroid;
import com.aniruddhfichadia.presentable.Registry;
import com.aniruddhfichadia.presentable.binder.LifecycleBinder;
import com.aniruddhfichadia.presentable.binder.LifecycleBinderActivity;
import com.aniruddhfichadia.presentableexample.DemoApplication;
import com.aniruddhfichadia.presentableexample.lifecyclebinding.BindingContract.Ui;


/**
 * @author Aniruddh Fichadia
 * @date 2017-08-28
 */
public class BoundActivity
        extends AppCompatActivity
        implements PresentableUiAndroid<DoNotPresent<Ui>, Ui>, Ui {
    private final LifecycleBinder<BoundActivity, DoNotPresent<Ui>, Ui> binder;
    private       DoNotPresent<Ui>                                     presenter;


    public BoundActivity() {
        super();

        // TODO: initialise the binder
        binder = new LifecycleBinderActivity<>();
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        // TODO: register the binding
        binder.registerBinding(this);

        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // TODO: unregister the binding
        binder.unregisterBinding(this);
    }


    //region PresentableUiAndroid
    @Override
    public void beforeOnCreate(@Nullable Bundle savedInstanceState) {
    }

    @Override
    public void afterOnCreate(@Nullable Bundle savedInstanceState) {
    }

    @Override
    public void saveUiState(@NonNull Bundle outState) {
    }

    @Override
    public void onNewInstance() {
    }

    @Override
    public void restoreUiState(@NonNull Bundle savedState) {
    }

    @Override
    public void inject() {
    }

    @NonNull
    @Override
    public Registry getRegistry() {
        return ((DemoApplication) getApplication()).getRegistry();
    }

    @NonNull
    @Override
    public DoNotPresent<Ui> createPresenter() {
        return new DoNotPresent<>();
    }

    @Override
    public DoNotPresent<Ui> getPresenter() {
        return presenter;
    }

    @Override
    public void setPresenter(DoNotPresent<Ui> presenter) {
        this.presenter = presenter;
    }

    @Override
    public Ui getUi() {
        return this;
    }
    //endregion
}
