package com.kicc.easypos.tablet.filedownload;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

public class EasyPosDownloader {

    public static void startDownloader(Context context, int requestCode, String didFilePath) {

        Intent intent = new Intent(context, DownloadActivity.class);

        intent.putExtra(Constants.INTENT_EXTRA_FILE_PATH, didFilePath);

        ((Activity)context).startActivityForResult(intent, requestCode);

    }
}
