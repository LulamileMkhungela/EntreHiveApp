package com.example.academy_intern.sampledesign.Services;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import com.example.academy_intern.sampledesign.R;

public class InputMessageDialog extends AlertDialog.Builder {

    public interface InputSenderDialogListener{
        public abstract void onOK(String message);
        public abstract void onCancel();
    }

    private EditText etMessage;

    public InputMessageDialog(Activity activity, final InputSenderDialogListener listener) {
        super( new ContextThemeWrapper(activity, R.style.AppTheme) );

        @SuppressLint("InflateParams") // It's OK to use NULL in an AlertDialog it seems...
                View dialogLayout = LayoutInflater.from(activity).inflate(R.layout.input_message_dialog, null);
        setView(dialogLayout);

        etMessage = dialogLayout.findViewById(R.id.et_message);

        setPositiveButton("Send", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                if( listener != null )
                    listener.onOK(String.valueOf(etMessage.getText()));

            }
        });

        setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                if( listener != null )
                    listener.onCancel();
            }
        });
    }

    public InputMessageDialog setMessage(String message)
    {
        etMessage.setText(message);
        return this;
    }

    @Override
    public AlertDialog show()
    {
        AlertDialog dialog = super.show();
        Window window = dialog.getWindow();
        if( window != null )
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        return dialog;
    }
}