package com.aniruddhfichadia.presentableexample.demo;


import com.aniruddhfichadia.presentable.ReplayableUi;
import com.aniruddhfichadia.replayableinterface.ReplayableAction;
import com.aniruddhfichadia.replayableinterface.ReplayableActionHelper;

import java.util.LinkedHashMap;
import java.util.Map.Entry;


/**
 * @author Aniruddh Fichadia
 * @date 17/1/17
 */
public class ReplayableDemoUi
        implements DemoUi,
                   ReplayableUi<DemoUi> {
    // generated
    private final LinkedHashMap<String, ReplayableAction<DemoUi>> actions = new LinkedHashMap<>();


    @Override
    public void doSomething() {
        String methodKey = ReplayableActionHelper.generateKeyForEnqueue();

        actions.remove(methodKey);
        actions.put(methodKey, new ReplayableAction<DemoUi>() {
            @Override
            public void replayOnTarget(DemoUi demoUi) {
                demoUi.doSomething();
            }
        });
    }


    @Override
    public void doSomethingWithParameters(final Object o1, final Object o2) {
        String methodKey = ReplayableActionHelper
                .generateKeyForEnqueueParamUnique("doSomethingWithParameters(Object, Object)", o1,
                                                  o2);

        actions.remove(methodKey);
        actions.put(methodKey, new ReplayableAction<DemoUi>() {
            @Override
            public void replayOnTarget(DemoUi demoUi) {
                demoUi.doSomethingWithParameters(o1, o2);
            }
        });
    }


    @Override
    public void doNothing() {
    }


    // generated
    @Override
    public void replay(DemoUi target) {
        for (Entry<String, ReplayableAction<DemoUi>> entry : actions.entrySet()) {
            entry.getValue().replayOnTarget(target);
        }

        actions.clear();
    }
}