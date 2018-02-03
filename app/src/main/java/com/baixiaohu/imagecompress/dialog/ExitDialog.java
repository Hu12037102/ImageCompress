package com.baixiaohu.imagecompress.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.TextView;

import com.baixiaohu.imagecompress.R;

/**
 * 项  目 :  ImageCompress
 * 包  名 :  com.baixiaohu.imagecompress.dialog
 * 类  名 :  ExitDialog
 * 作  者 :  胡庆岭
 * 时  间 :  2018/2/1 0001 下午 12:24
 * 描  述 :  ${TODO}
 *
 * @author ：
 */

public class ExitDialog extends AlertDialog {

    private CheckBox mDeleteCheck;
    private TextView mCancelView;
    private TextView mConfirmView;

    public void setOnExitDialogClickListener(OnExitDialogClickListener onExitDialogClickListener) {
        this.onExitDialogClickListener = onExitDialogClickListener;
    }

    private OnExitDialogClickListener onExitDialogClickListener;

    public ExitDialog(Context context) {
        super(context);
    }

    public ExitDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_exit_view);
        initView();
        initEvent();
    }

    private void initEvent() {
        mCancelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        mConfirmView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onExitDialogClickListener != null){
                    onExitDialogClickListener.onConfirmListener(mDeleteCheck.isChecked());
                }
            }
        });
    }

    private void initView() {
        mDeleteCheck = findViewById(R.id.delete_cb);
        mCancelView = findViewById(R.id.cancel_tv);
        mConfirmView = findViewById(R.id.confirm_tv);
    }

    public interface OnExitDialogClickListener {
        void onConfirmListener(boolean isChecked);
    }
}
