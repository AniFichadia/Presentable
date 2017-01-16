package com.aniruddhfichadia.presentable.component;


import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;

import com.aniruddhfichadia.presentable.SimpleErrorDialog;


/**
 * @author Aniruddh Fichadia
 * @date 29/12/16
 */
public class AndroidDialogMessageComponent
        extends MessageComponent {
    @NonNull
    private final Context         context;
    @NonNull
    private final FragmentManager fragmentManager;

    @Nullable
    private DialogFragment currentlyShowing;


    public AndroidDialogMessageComponent(@NonNull Context context, @NonNull FragmentManager fragmentManager) {
        this.context = context;
        this.fragmentManager = fragmentManager;
    }


    @Override
    protected void showInternal(@NonNull MessageBuilder messageBuilder, @Nullable final OnActionClickListener listener) {
        dismissIfShowing();

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

        currentlyShowing = dialog;
    }


    private void dismissIfShowing() {
        if (currentlyShowing != null) {
            currentlyShowing.dismissAllowingStateLoss();
        }
    }
}