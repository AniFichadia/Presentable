package com.aniruddhfichadia.presentableexample.lifecyclebinding;


import com.aniruddhfichadia.presentable.Contract;


/**
 * @author Aniruddh Fichadia
 * @date 2017-08-28
 */
public interface BindingContract {
    interface Ui
            extends Contract.Ui {
    }

    interface Presenter
            extends Contract.Presenter<Ui> {
    }

    interface InterActor
            extends Contract.InterActor {
    }

    interface InterActorListener {
    }
}
