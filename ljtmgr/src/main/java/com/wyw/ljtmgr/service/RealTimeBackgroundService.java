package com.wyw.ljtmgr.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.util.Log;

import com.wyw.ljtmgr.R;
import com.wyw.ljtmgr.biz.UserBiz;
import com.wyw.ljtmgr.config.AppConfig;
import com.wyw.ljtmgr.config.MyApplication;
import com.wyw.ljtmgr.config.PreferenceCache;
import com.wyw.ljtmgr.ui.ActivitySplash;


/**
 * 拉取服务器消息的后台服务
 */
public class RealTimeBackgroundService extends IntentService {
    private static final String ACTION_PLAYVOICE = "ACTION_PLAYVOICE";
    private static final String ACTION_STARTAPP = "ACTION_STARTAPP";
    private MyApplication myApp;
    MediaPlayer mediaplayer;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public RealTimeBackgroundService() {
        super("PullService");
    }

    public static void playVoice(Context context, boolean on) {
        Log.e(AppConfig.TAG_ERR, "setServiceStat play background .........." + on);
        Intent it = new Intent(context, RealTimeBackgroundService.class);
        it.setAction(ACTION_PLAYVOICE);
        context.startService(it);
    }

    public static void openActivity(Context context, boolean on) {
        Log.e(AppConfig.TAG_ERR, "setServiceStat play background .........." + on);
        Intent it = new Intent(context, RealTimeBackgroundService.class);
        it.setAction(ACTION_STARTAPP);
        context.startService(it);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        Log.e(AppConfig.TAG_ERR, "play background intent.........." + intent);

        if (intent != null) {
            final String action = intent.getAction();
            Log.e(AppConfig.TAG_ERR, "action:" + action);
            String act = intent.getAction();
            if (ACTION_PLAYVOICE.equals(act)) {
                if (!PreferenceCache.getActived() && UserBiz.isLogined()) {
                    stopPlay();
                    mediaplayer = MediaPlayer.create(getApplicationContext(), R.raw.prompt);
                    mediaplayer.start();
                    mediaplayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            Log.e(AppConfig.TAG_ERR, "msgfrag complete");
                            stopPlay();
                        }
                    });
                }
            } else if (ACTION_STARTAPP.equals(act)) {
                Log.e(AppConfig.TAG_ERR, "ACTION_STARTAPP");
                Intent it = new Intent(getApplicationContext(), ActivitySplash.class);
                it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(it);
            }
//            setServiceStat(getApplicationContext(), false);
        }
    }

    void stopPlay() {
        if (mediaplayer != null) {
            mediaplayer.release();
            mediaplayer = null;
            Log.e(AppConfig.TAG_ERR, "msgfrag stop");
        }
    }
}
