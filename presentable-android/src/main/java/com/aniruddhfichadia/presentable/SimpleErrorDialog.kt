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
package com.aniruddhfichadia.presentable


import android.app.Dialog
import android.content.DialogInterface.OnClickListener
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatDialogFragment
import com.aniruddhfichadia.presentable.component.MessageBuilder


/**
 * @author Aniruddh Fichadia | Email: Ani.Fichadia@gmail.com | GitHub: AniFichadia (http://github.com/AniFichadia)
 * @date 29/12/16
 */
class SimpleErrorDialog : AppCompatDialogFragment() {
    companion object {
        private const val KEY_TITLE = "key_title"
        private const val KEY_MESSAGE = "key_message"
        private const val KEY_POSITIVE_ACTION = "key_positive_actio"
        private const val KEY_NEGATIVE_ACTION = "key_negative_action"
        private const val KEY_NEUTRAL_ACTION = "key_neutral_action"
        private const val KEY_DISMISSIBLE = "key_dismissible"


        fun newInstance(messageBuilder: MessageBuilder): SimpleErrorDialog {
            val args = Bundle()
            args.putCharSequence(KEY_TITLE, messageBuilder.title)
            args.putCharSequence(KEY_MESSAGE, messageBuilder.message)
            args.putCharSequence(KEY_POSITIVE_ACTION, messageBuilder.positiveAction)
            args.putCharSequence(KEY_NEGATIVE_ACTION, messageBuilder.negativeAction)
            args.putCharSequence(KEY_NEUTRAL_ACTION, messageBuilder.neutralAction)
            args.putBoolean(KEY_DISMISSIBLE, messageBuilder.dismissible)

            val instance = SimpleErrorDialog()
            instance.arguments = args

            return instance
        }
    }

    var onClickListener: OnClickListener? = null


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val args = arguments!!
        val title = args.getCharSequence(KEY_TITLE)
        val message = args.getCharSequence(KEY_MESSAGE)
        val positiveAction = args.getCharSequence(KEY_POSITIVE_ACTION)
        val negativeAction = args.getCharSequence(KEY_NEGATIVE_ACTION)
        val neutralAction = args.getCharSequence(KEY_NEUTRAL_ACTION)
        val dismissible = args.getBoolean(KEY_DISMISSIBLE)


        val builder = AlertDialog.Builder(context!!)

        builder.setTitle(title)
        builder.setMessage(message)

        val delegatingOnClickListener = OnClickListener { dialog, which ->
            onClickListener?.onClick(dialog, which)
        }

        builder.setPositiveButton(positiveAction, delegatingOnClickListener)
        builder.setNegativeButton(negativeAction, delegatingOnClickListener)
        builder.setNeutralButton(neutralAction, delegatingOnClickListener)

        // Dismissible handling: prevent back-presses and external touches from dismissing the
        // dialog
        isCancelable = dismissible

        return builder.create()
    }
}
