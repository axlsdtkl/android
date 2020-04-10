package com.example.artshoes2;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.widget.Toast;

import java.io.File;

/**
 * 后台下载服务
 */
public class DownLoadService extends Service {

    private DownLoadTask downLoadTask;
    private String downLoadUrl;

    private DownLoadListener downLoadListener = new DownLoadListener() {
        @Override
        public void onProgress(int progress) {
            //getNotificationManager().notify(1, getNotification("正在下载", progress));
        }

        @Override
        public void onSuccess() {
            downLoadTask = null;
            stopForeground(true);
            //getNotificationManager().notify(1, getNotification("下载完成", -1));
            Toast.makeText(DownLoadService.this, "下载完成！", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onFailed() {
            downLoadTask = null;
            stopForeground(true);
            //getNotificationManager().notify(1, getNotification("下载失败", -1));
            Toast.makeText(DownLoadService.this, "下载完成！", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPaused() {
            downLoadTask = null;
            Toast.makeText(DownLoadService.this, "下载暂停！", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCanceled() {
            downLoadTask = null;
            stopForeground(true);
            Toast.makeText(DownLoadService.this, "下载取消！", Toast.LENGTH_SHORT).show();
        }
    };

    public DownLoadService() {
    }

    /**
     * 自定义Binder类
     */
    class DownLoadBinder extends Binder {

        /**
         * 开始下载
         *
         * @param url
         */
        public void startDownLoad(String url) {
            if (downLoadTask == null) {
                downLoadUrl = url;
                downLoadTask = new DownLoadTask(downLoadListener);
                downLoadTask.execute(downLoadUrl);
                //startForeground(1, getNotification("开始下载", 0));
            }
        }

        /**
         * 暂停下载
         */
        public void pauseDownLoad() {
            if (downLoadTask != null) {
                downLoadTask.pauseDownLoad();
            }
        }

        /**
         * 取消下载
         */
        public void cancelDownLoad() {
            if (downLoadTask != null) {
                downLoadTask.cancelDownLoad();
            }
            //文件删除
            if (downLoadUrl != null) {
                String fileName = downLoadUrl.substring(downLoadUrl.lastIndexOf("/"));
                String directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
                File file = new File(directory, fileName);
                if (file.exists()) {
                    file.delete();
                }
                getNotificationManager().cancel(1);
                stopForeground(true);
                Toast.makeText(DownLoadService.this, "下载取消！", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private DownLoadBinder downLoadBinder = new DownLoadBinder();

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return downLoadBinder;
    }

    private NotificationManager getNotificationManager() {
        return (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }
}
