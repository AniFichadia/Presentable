package com.aniruddhfichadia.presentable.component;


import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * @author Aniruddh Fichadia
 * @date 29/12/16
 */
public abstract class MessageComponent {
    public static final int ACTION_POSITIVE = -1;
    public static final int ACTION_NEGATIVE = -2;
    public static final int ACTION_NEUTRAL  = -3;


    public void show(@NotNull MessageBuilder messageBuilder, @Nullable OnActionClickListener listener)
            throws IllegalStateException {
        messageBuilder.ensureValidState();

        showInternal(messageBuilder, listener);
    }

    public void show(@NotNull MessageBuilder messageBuilder) throws IllegalStateException {
        show(messageBuilder, null);
    }

    protected abstract void showInternal(@NotNull MessageBuilder messageBuilder, @Nullable OnActionClickListener listener);
}