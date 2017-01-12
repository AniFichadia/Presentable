package com.aniruddhfichadia.presentable;


import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.View;


/**
 * @author Aniruddh Fichadia
 * @date 28/12/16
 */
public interface ViewBindable {
    /** Provide your layout resource through this method. Use any negative number for no layout */
    @LayoutRes
    int getLayoutResource();

    /**
     * Perform any view binding (eg. via ButterKnife, or using a bunch of {@link View#findViewById(int)} calls). Won't
     * be called if a layout isn't inflated.
     *
     * @param view The inflated layout. Won't be null
     */
    void bindView(@NonNull View view);

    /**
     * Called after {@link #bindView(View)}. Configure your UI elements here
     *
     * @param view The inflated layout. Won't be null
     */
    void afterBindView(@NonNull View view);

    void unbindView();
}