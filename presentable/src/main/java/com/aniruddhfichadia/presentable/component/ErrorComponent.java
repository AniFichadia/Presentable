package com.aniruddhfichadia.presentable.component;


import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;


/**
 * @author Aniruddh Fichadia
 * @date 29/12/16
 */
public abstract class ErrorComponent {
    public static final int ACTION_POSITIVE = -1;
    public static final int ACTION_NEGATIVE = -2;
    public static final int ACTION_NEUTRAL  = -3;


    public void show(@NotNull ErrorBuilder errorBuilder, @Nullable OnActionClickListener listener)
            throws IllegalStateException {
        errorBuilder.ensureValidState();

        showInternal(errorBuilder, listener);
    }

    public void show(@NotNull ErrorBuilder errorBuilder) throws IllegalStateException {
        show(errorBuilder, null);
    }

    protected abstract void showInternal(@NotNull ErrorBuilder errorBuilder, @Nullable OnActionClickListener listener);


    public static class ErrorBuilder
            implements Serializable {
        // TODO: error style?
        @Nullable
        private String title;
        @Nullable
        private String message;
        @Nullable
        private String positiveAction;
        @Nullable
        private String neutralAction;
        @Nullable
        private String negativeAction;

        private boolean dismissible = false;


        @Nullable
        public String getTitle() {
            return title;
        }

        public ErrorBuilder setTitle(@Nullable String title) {
            this.title = title;
            return this;
        }

        @Nullable
        public String getMessage() {
            return message;
        }

        public ErrorBuilder setMessage(@Nullable String message) {
            this.message = message;
            return this;
        }

        @Nullable
        public String getPositiveAction() {
            return positiveAction;
        }

        public ErrorBuilder setPositiveAction(@Nullable String positiveAction) {
            this.positiveAction = positiveAction;
            return this;
        }

        @Nullable
        public String getNeutralAction() {
            return neutralAction;
        }

        public ErrorBuilder setNeutralAction(@Nullable String neutralAction) {
            this.neutralAction = neutralAction;
            return this;
        }

        @Nullable
        public String getNegativeAction() {
            return negativeAction;
        }

        public ErrorBuilder setNegativeAction(@Nullable String negativeAction) {
            this.negativeAction = negativeAction;
            return this;
        }


        public boolean isDismissible() {
            return dismissible;
        }

        public ErrorBuilder setDismissible(boolean dismissible) {
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


    public interface OnActionClickListener
            extends Serializable {
        void onActionClicked(int which);
    }
}