package com.aniruddhfichadia.presentable.component;


import org.jetbrains.annotations.Nullable;

import java.io.Serializable;


/**
 * @author Aniruddh Fichadia
 * @date 13/1/17
 */
public class MessageBuilder
        implements Serializable {
    /** Generic int. Implement as required and handle in your {@link MessageComponent} */
    private int          errorType;
    @Nullable
    private CharSequence title;
    @Nullable
    private CharSequence message;
    @Nullable
    private CharSequence positiveAction;
    @Nullable
    private CharSequence neutralAction;
    @Nullable
    private CharSequence negativeAction;

    private boolean dismissible = false;


    public int getErrorType() {
        return errorType;
    }

    public MessageBuilder setErrorType(int errorType) {
        this.errorType = errorType;
        return this;
    }

    @Nullable
    public CharSequence getTitle() {
        return title;
    }

    public MessageBuilder setTitle(@Nullable CharSequence title) {
        this.title = title;
        return this;
    }

    @Nullable
    public CharSequence getMessage() {
        return message;
    }

    public MessageBuilder setMessage(@Nullable CharSequence message) {
        this.message = message;
        return this;
    }

    @Nullable
    public CharSequence getPositiveAction() {
        return positiveAction;
    }

    public MessageBuilder setPositiveAction(@Nullable CharSequence positiveAction) {
        this.positiveAction = positiveAction;
        return this;
    }

    @Nullable
    public CharSequence getNeutralAction() {
        return neutralAction;
    }

    public MessageBuilder setNeutralAction(@Nullable CharSequence neutralAction) {
        this.neutralAction = neutralAction;
        return this;
    }

    @Nullable
    public CharSequence getNegativeAction() {
        return negativeAction;
    }

    public MessageBuilder setNegativeAction(@Nullable CharSequence negativeAction) {
        this.negativeAction = negativeAction;
        return this;
    }


    public boolean isDismissible() {
        return dismissible;
    }

    public MessageBuilder setDismissible(boolean dismissible) {
        this.dismissible = dismissible;
        return this;
    }


    void ensureValidState() throws IllegalStateException {
        if (title == null || message == null ||
                (positiveAction == null && negativeAction == null && neutralAction == null)) {
            throw new IllegalStateException(
                    "At minimum, message or title is required, with one action");
        }
    }
}