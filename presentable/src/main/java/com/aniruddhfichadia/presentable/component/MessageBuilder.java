/**
 * Copyright (C) 2016 Aniruddh Fichadia
 * <p/>
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * <p/>
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * <p/>
 * You should have received a copy of the GNU General Public License along with this program. If not, see
 * <http://www.gnu.org/licenses/>.
 * <p/>
 * If you use or enhance the code, please let me know using the provided author information or via email
 * Ani.Fichadia@gmail.com.
 */
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
    private int errorType;
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