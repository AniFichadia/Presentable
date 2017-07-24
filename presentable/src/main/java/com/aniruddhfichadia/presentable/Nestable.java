package com.aniruddhfichadia.presentable;


import org.jetbrains.annotations.Nullable;


/**
 * Represents a element (such as a UI) that can be nested within something else
 *
 * @author Aniruddh Fichadia
 * @date 2017-07-24
 */
public interface Nestable {
    @Nullable
    Nestable getNestableParent();
}