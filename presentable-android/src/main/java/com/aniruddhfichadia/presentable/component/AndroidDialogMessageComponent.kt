/*
 * Copyright (C) 2016 Aniruddh Fichadia
 *
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 *
 * You should have received a copy of the GNU General Public License along with this program. If not, see
 * <http:></http:>//www.gnu.org/licenses/>.
 *
 *
 * If you use or enhance the code, please let me know using the provided author information or via email
 * Ani.Fichadia@gmail.com.
 */
package com.aniruddhfichadia.presentable.component


import android.content.DialogInterface
import android.content.DialogInterface.OnClickListener
import android.support.v4.app.FragmentManager
import com.aniruddhfichadia.presentable.SimpleErrorDialog


/**
 * @author Aniruddh Fichadia | Email: Ani.Fichadia@gmail.com | GitHub: AniFichadia (http://github.com/AniFichadia)
 * @date 29/12/16
 */
class AndroidDialogMessageComponent(private val fragmentManager: FragmentManager) : MessageComponent() {
    override fun showInternal(messageBuilder: MessageBuilder, listener: OnActionClickListener?) {
        val dialog = SimpleErrorDialog.newInstance(messageBuilder)

        listener?.let {
            dialog.onClickListener = OnClickListener { _, which ->
                val remappedWhich = when (which) {
                    DialogInterface.BUTTON_POSITIVE -> MessageComponent.ACTION_POSITIVE
                    DialogInterface.BUTTON_NEGATIVE -> MessageComponent.ACTION_NEGATIVE
                    DialogInterface.BUTTON_NEUTRAL -> MessageComponent.ACTION_NEUTRAL
                    else -> null
                }

                remappedWhich?.let {
                    listener.onActionClicked(remappedWhich)
                }
            }
        }

        dialog.show(fragmentManager, SimpleErrorDialog::class.java.simpleName)
    }
}
