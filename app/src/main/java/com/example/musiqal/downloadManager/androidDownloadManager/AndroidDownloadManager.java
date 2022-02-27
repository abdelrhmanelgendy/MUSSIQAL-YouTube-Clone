package com.example.musiqal.downloadManager.androidDownloadManager;

import static android.content.Context.DOWNLOAD_SERVICE;

import android.app.DownloadManager;
import android.app.NotificationManager;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;

import java.io.File;

public class AndroidDownloadManager {
private Context context;

    public AndroidDownloadManager(Context context) {
        this.context = context;
    }

    public void download(String url, String name) {

        File cashFile =
                new File(Environment.DIRECTORY_DOWNLOADS + "/" + "MUSSIQAL");
        String path = "";
        if (!cashFile.exists()) {
            path = (Environment.DIRECTORY_DOWNLOADS);
        } else {
            path = cashFile.getPath();
        }
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        request.setAllowedOverMetered(true);
        request.setAllowedOverRoaming(true)
                .setTitle(name)
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
                .setDescription("Mussiqal Downloading")
                .setDestinationInExternalPublicDir(path,
                        name + ".mp3");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            request.setRequiresCharging(false);
        }
        DownloadManager downloadManager=(DownloadManager)context.getSystemService(DOWNLOAD_SERVICE);
        downloadManager.enqueue(request);
    }
}
