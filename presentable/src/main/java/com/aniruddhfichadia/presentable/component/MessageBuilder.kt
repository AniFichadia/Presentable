/*
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
package com.aniruddhfichadia.presentable.component


import java.io.Serializable


/**
 * @author Aniruddh Fichadia | Email: Ani.Fichadia@gmail.com | GitHub: AniFichadia (http://github.com/AniFichadia)
 * @date 13/1/17
 */
class MessageBuilder : Serializable {
    /** Generic int. Implement as required and handle in your [MessageComponent]  */
    var messageType: Int = 0
        private set
    var title: CharSequence? = null
        private set
    var message: CharSequence? = null
        private set
    var positiveAction: CharSequence? = null
        private set
    var neutralAction: CharSequence? = null
        private set
    var negativeAction: CharSequence? = null
        private set
    var dismissible = false
        private set


    fun messageType(messageType: Int) = apply {
        this.messageType = messageType
    }

    fun title(title: CharSequence?) = apply {
        this.title = title
    }

    fun message(message: CharSequence?) = apply {
        this.message = message
    }

    fun positiveAction(positiveAction: CharSequence?) = apply {
        this.positiveAction = positiveAction
    }

    fun neutralAction(neutralAction: CharSequence?) = apply {
        this.neutralAction = neutralAction
    }

    fun negativeAction(negativeAction: CharSequence?) = apply {
        this.negativeAction = negativeAction
    }

    fun dismissible(dismissible: Boolean) = apply {
        this.dismissible = dismissible
    }


    @Throws(IllegalStateException::class)
    internal fun ensureValidState() {
        if (title == null || message == null ||
                (positiveAction == null && negativeAction == null && neutralAction == null)) {
            throw IllegalStateException("At minimum, message or title is required, with one action")
        }
    }
}
