package com.aniruddhfichadia.presentableexample.demo;


import com.aniruddhfichadia.presentable.PresenterUi;
import com.aniruddhfichadia.replayableinterface.ReplayStrategy;
import com.aniruddhfichadia.replayableinterface.Replayable;


/**
 * @author Aniruddh Fichadia
 * @date 17/1/17
 */
@Replayable
public interface DemoUi
        extends PresenterUi {
    @Replayable(ReplayStrategy.ENQUEUE_LAST_ONLY)
    void doSomething();

    @Replayable(ReplayStrategy.ENQUEUE_PARAM_UNIQUE)
    void doSomethingWithParameters(Object o1, Object o2);

    @Replayable(ReplayStrategy.NONE)
    void doNothing();
}