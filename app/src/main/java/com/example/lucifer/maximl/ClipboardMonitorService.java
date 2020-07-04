package com.example.lucifer.maximl;

import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;



public class ClipboardMonitorService extends Service {

    private ClipboardManager mClipboardManager;
    Context ctx = this;
    String EXTRA_TEXT = "My_url";

    @Override
    public void onCreate() {
        super.onCreate();

        mClipboardManager =
                (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        mClipboardManager.addPrimaryClipChangedListener(
                mOnPrimaryClipChangedListener);

        System.out.println("Service started running..");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mClipboardManager != null) {
            mClipboardManager.removePrimaryClipChangedListener(
                    mOnPrimaryClipChangedListener);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    public ClipboardManager.OnPrimaryClipChangedListener mOnPrimaryClipChangedListener =
            new ClipboardManager.OnPrimaryClipChangedListener() {
                @Override
                public void onPrimaryClipChanged() {

                    if(mClipboardManager.hasPrimaryClip()) {
                        String charSequence = mClipboardManager.getPrimaryClip().getItemAt(0).getText().toString();
                        System.out.println("Copied Link : ====================" + charSequence);
                        if (charSequence != null) {

                            Intent intent = new Intent(com.example.lucifer.maximl.ClipboardMonitorService.this, com.example.lucifer.maximl.MainActivity.class);
                            intent.putExtra(EXTRA_TEXT, charSequence);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);

                        }
                    }
                }
            };



}
