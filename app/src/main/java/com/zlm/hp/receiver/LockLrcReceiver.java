package com.zlm.hp.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;

import java.util.Date;

import base.utils.LoggerUtil;

/**
 * 锁屏歌词
 * Created by KathLine on 2018/1/15.
 */

public class LockLrcReceiver {

    private static final String base_action = "com.zlm.hp";
    private LoggerUtil logger;
    /**
     * 是否注册成功
     */
    private boolean isRegisterSuccess = false;
    private Context mContext;

    /**
     * 注册成功广播
     */
    private String ACTION_LOCKLRCSUCCESS = base_action + ".lock.lrc.success_" + new Date().getTime();
    /**
     * 显示锁屏歌词
     */
    public static String ACTION_SHOWLRCMESSAGE = base_action + ".lock.lrc.show";
    /**
     * 隐藏锁屏歌词
     */
    public static String ACTION_HIDELRCMESSAGE = base_action + ".lock.lrc.hide";

    private BroadcastReceiver mLockLrcReceiver;
    private IntentFilter mLockLrcIntentFilter;
    private LockLrcReceiverListener mLockLrcReceiverListener;

    public LockLrcReceiver(Context context) {
        mContext = context;
        logger = LoggerUtil.getZhangLogger(context);

        mLockLrcIntentFilter = new IntentFilter();
        mLockLrcIntentFilter.addAction(ACTION_LOCKLRCSUCCESS);
        mLockLrcIntentFilter.addAction(ACTION_SHOWLRCMESSAGE);
        mLockLrcIntentFilter.addAction(ACTION_HIDELRCMESSAGE);
        mLockLrcIntentFilter.addAction(Intent.ACTION_SCREEN_OFF);
    }

    /**
     *
     */
    private Handler mLockLrcHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            if (mLockLrcReceiverListener != null) {
                Intent intent = (Intent) msg.obj;

                if (intent.getAction().equals(ACTION_LOCKLRCSUCCESS)) {
                    isRegisterSuccess = true;
                } else {
                    mLockLrcReceiverListener.onReceive(mContext, intent);
                }
            }
        }
    };

    /**
     * 注册广播
     *
     * @param context
     */
    public void registerReceiver(Context context) {
        mLockLrcReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                Message msg = new Message();
                msg.obj = intent;
                mLockLrcHandler.sendMessage(msg);
            }
        };
        mContext.registerReceiver(mLockLrcReceiver, mLockLrcIntentFilter);
        //发送注册成功广播
        Intent successIntent = new Intent(ACTION_LOCKLRCSUCCESS);
        successIntent.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        mContext.sendBroadcast(successIntent);
    }

    /**
     * 取消注册广播
     */
    public void unregisterReceiver(Context context) {
        if (mLockLrcReceiver != null && isRegisterSuccess) {

            mContext.unregisterReceiver(mLockLrcReceiver);

        }
    }

    public void setLockLrcReceiverListener(LockLrcReceiverListener mLockLrcReceiverListener) {
        this.mLockLrcReceiverListener = mLockLrcReceiverListener;
    }

    ///////////////////////////////////
    public interface LockLrcReceiverListener {
        void onReceive(Context context, Intent intent);
    }
}
