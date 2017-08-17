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


import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;

import com.aniruddhfichadia.presentable.SimpleErrorDialog;


/**
 * @author Aniruddh Fichadia | Email: Ani.Fichadia@gmail.com | GitHub: AniFichadia (http://github.com/AniFichadia)
 * @date 29/12/16
 */
public class AndroidDialogMessageComponent
        extends MessageComponent {
    @NonNull
    private final FragmentManager fragmentManager;


    public AndroidDialogMessageComponent(@NonNull FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }


    @Override
    protected void showInternal(@NonNull MessageBuilder messageBuilder,
                                @Nullable final OnActionClickListener listener) {
        SimpleErrorDialog dialog = SimpleErrorDialog.newInstance(messageBuilder);

        if (listener != null) {
            dialog.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            listener.onActionClicked(MessageComponent.ACTION_POSITIVE);
                            break;
                        case DialogInterface.BUTTON_NEGATIVE:
                            listener.onActionClicked(MessageComponent.ACTION_NEGATIVE);
                            break;
                        case DialogInterface.BUTTON_NEUTRAL:
                            listener.onActionClicked(MessageComponent.ACTION_NEUTRAL);
                            break;
                        default:
                            break;
                    }
                }
            });
        }

        dialog.show(fragmentManager, SimpleErrorDialog.class.getSimpleName());
    }
}