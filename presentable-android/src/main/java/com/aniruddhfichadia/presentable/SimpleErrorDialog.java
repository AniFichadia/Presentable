package com.aniruddhfichadia.presentable;


import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;

import com.aniruddhfichadia.presentable.component.MessageBuilder;


/**
 * @author Aniruddh Fichadia | Email: Ani.Fichadia@gmail.com | GitHub: AniFichadia (http://github.com/AniFichadia)
 * @date 29/12/16
 */
public class SimpleErrorDialog
        extends AppCompatDialogFragment {
    private static final String KEY_ERROR_BUILDER = "key_error_builder";

    @Nullable
    private OnClickListener onClickListener;


    @NonNull
    public static SimpleErrorDialog newInstance(@NonNull MessageBuilder messageBuilder) {
        Bundle args = new Bundle();
        args.putSerializable(KEY_ERROR_BUILDER, messageBuilder);

        SimpleErrorDialog instance = new SimpleErrorDialog();
        instance.setArguments(args);

        return instance;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        MessageBuilder messageBuilder =
                (MessageBuilder) getArguments().getSerializable(KEY_ERROR_BUILDER);

        if (messageBuilder == null) {
            throw new IllegalStateException("MessageBuilder is not set");
        }

        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setTitle(messageBuilder.getTitle());
        builder.setMessage(messageBuilder.getMessage());

        OnClickListener delegatingOnClickListener = new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (onClickListener != null) {
                    onClickListener.onClick(dialog, which);
                }
            }
        };

        builder.setPositiveButton(messageBuilder.getPositiveAction(), delegatingOnClickListener);
        builder.setNegativeButton(messageBuilder.getNegativeAction(), delegatingOnClickListener);
        builder.setNeutralButton(messageBuilder.getNeutralAction(), delegatingOnClickListener);

        // Dismissible handling: prevent back-presses and external touches from dismissing the
        // dialog
        setCancelable(messageBuilder.isDismissible());

        return builder.create();
    }


    public void setOnClickListener(@Nullable OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }
}