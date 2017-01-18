package com.aniruddhfichadia.presentableexample.demo;


import com.aniruddhfichadia.presentable.BaseReplayablePresenter;

import org.jetbrains.annotations.NotNull;


/**
 * @author Aniruddh Fichadia
 * @date 17/1/17
 */
public class DemoPresenterImpl
        extends BaseReplayablePresenter<DemoUi>
        implements DemoPresenter {
    @NotNull
    @Override
    protected DemoUi getUnboundUi() {
        // Alternatively, inject or retain an instance of this
        return new ReplayableDemoUi();
    }
}