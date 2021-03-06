package com.bitmap.database.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

/**
 * Created by Administrator on 2018/7/10.
 */

public class ProgressDialogHandler extends Handler {

    public static final int SHOW_PROGRESS_DIALOG = 1;
    public static final int DISMISS_PROGRESS_DIALOG = 2;
    private ProgressDialog pd ;
    private Context mContext;
    private boolean cancelable;

    public ProgressDialogHandler(Context context,boolean cancelable){
        this.mContext = context;
        this.cancelable = cancelable;
    }

    private void initProgressDialog(String title){
        if(pd == null){
            pd = new ProgressDialog(mContext);
            if(!TextUtils.isEmpty(title)){
                title="加载中，请稍后...";
            }
            pd.setMessage(title);
            pd.setCancelable(cancelable);
            if(cancelable){
                pd.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        dismissProgress();
                    }
                });
            }
            if(!pd.isShowing()){
                pd.show();
            }
        }
    }

    private void dismissProgress() {
        if(pd != null){
            pd.dismiss();
            pd = null;
        }
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what){
            case SHOW_PROGRESS_DIALOG:
                String title = (String) msg.obj;
                initProgressDialog(title);
                break;
            case DISMISS_PROGRESS_DIALOG:
                dismissProgress();
                break;
        }
    }
}






















