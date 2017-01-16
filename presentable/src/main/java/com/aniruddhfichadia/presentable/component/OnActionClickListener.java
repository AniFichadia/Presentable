package com.aniruddhfichadia.presentable.component;


import java.io.Serializable;


/**
 * @author Aniruddh Fichadia
 * @date 13/1/17
 */
public interface OnActionClickListener
        extends Serializable {
    void onActionClicked(int which);
}