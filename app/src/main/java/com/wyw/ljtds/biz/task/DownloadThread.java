package com.wyw.ljtds.biz.task;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;
import android.webkit.DownloadListener;

import com.wyw.ljtds.config.AppConfig;
import com.wyw.ljtds.utils.Utils;

import java.util.ConcurrentModificationException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.RunnableFuture;

/**
 * Created by wsy on 17-9-13.
 */

public class DownloadThread<T> extends HandlerThread {
    private boolean isQuit = false;
    private final int MESSAGE_DOWNLOAD = 0;
    private ConcurrentMap<T, String> map = new ConcurrentHashMap<>();
    private Handler requestHandler;
    private Handler responseHandler;
    private DownloadListener downloadListner;

    public DownloadThread(String name, Handler responseHandler) {
        super(name);
        this.responseHandler = responseHandler;
    }

    public void setDownloadListner(DownloadListener downloadListner) {
        this.downloadListner = downloadListner;
    }

    public interface DownloadListener<T> {
        void completeDownload(T token, String url, Bitmap bitMap);
    }

    @Override
    public boolean quit() {
        isQuit = true;
        return super.quit();
    }

    public void queneObj(T token, String url) {
        if (url == null)
            map.remove(token);
        else {
            map.put(token, url);
            //添加鑫的消息道 消息队列
            requestHandler.obtainMessage(MESSAGE_DOWNLOAD, token).sendToTarget();
        }
    }

    @Override
    protected void onLooperPrepared() {
        Log.e(AppConfig.ERR_TAG, "onLooperPrepared......create requestHandler");
        requestHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == MESSAGE_DOWNLOAD) {
                    handleDownload((T) msg.obj);
                }

            }
        };
    }

    private void handleDownload(final T token) {
        if (isQuit) return;
        final String url = map.get(token);
        if (url == null) return;
        Log.e(AppConfig.ERR_TAG, this.getClass().getName() + " loading....." + url);
        final Bitmap bitMap = Utils.getBitMapFromUrl(url);

        responseHandler.post(new Runnable() {
            @Override
            public void run() {
                if (isQuit) return;
                if (downloadListner != null) {
                    downloadListner.completeDownload(token, url, bitMap);
                }
            }
        });
    }

}
