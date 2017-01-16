package com.aniruddhfichadia.presentable;


import java.io.Serializable;


/**
 * A savable model of a {@link Presenter}'s state. Allows {@link Presenter}'s to be saved
 * and restored based on lifecycle events, such as Android's Fragment.onSaveInstanceState
 * (Bundle) and Fragment.onViewStateRestore(Bundle)
 */
public interface PresenterModel
        extends Serializable {
}