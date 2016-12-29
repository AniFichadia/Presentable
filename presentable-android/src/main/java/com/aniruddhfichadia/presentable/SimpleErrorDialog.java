package com.aniruddhfichadia.presentable;


import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;

import com.aniruddhfichadia.presentable.component.ErrorComponent.ErrorBuilder;


/**
 * @author Aniruddh Fichadia
 * @date 29/12/16
 */
public class SimpleErrorDialog
        extends AppCompatDialogFragment {
    private static final String KEY_ERROR_BUILDER = "key_error_builder";

    @Nullable
    private OnClickListener onClickListener;


    @NonNull
    public static SimpleErrorDialog newInstance(@NonNull ErrorBuilder errorBuilder) {
        Bundle args = new Bundle();
        args.putSerializable(KEY_ERROR_BUILDER, errorBuilder);

        SimpleErrorDialog instance = new SimpleErrorDialog();
        instance.setArguments(args);

        return instance;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        ErrorBuilder errorBuilder =
                (ErrorBuilder) getArguments().getSerializable(KEY_ERROR_BUILDER);

        if (errorBuilder == null) {
            throw new IllegalStateException("ErrorBuilder is not set");
        }

        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setTitle(errorBuilder.getTitle());
        builder.setMessage(errorBuilder.getMessage());

        OnClickListener delegatingOnClickListener = new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (onClickListener != null) {
                    onClickListener.onClick(dialog, which);
                }
            }
        };

        builder.setPositiveButton(errorBuilder.getPositiveAction(), delegatingOnClickListener);
        builder.setNegativeButton(errorBuilder.getNegativeAction(), delegatingOnClickListener);
        builder.setNeutralButton(errorBuilder.getNeutralAction(), delegatingOnClickListener);

        // Dismissible handling: prevent back-presses and external touches from dismissing the
        // dialog
        setCancelable(errorBuilder.isDismissible());

        return builder.create();
    }


    public void setOnClickListener(@Nullable OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }
}