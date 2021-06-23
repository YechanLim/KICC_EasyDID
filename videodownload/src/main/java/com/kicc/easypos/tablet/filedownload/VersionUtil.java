package com.kicc.easypos.tablet.filedownload;

import android.content.Context;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by POS_KJH on 2017-12-29.
 */

public class VersionUtil {

    private static final String TAG = "VersionUtil";

    public static void makeVersionFile(Context context, String msg) {

        //File file = new File(Constants.DEFAULT_DOWNLOAD_PATH, "PosVersion.xml");
//        File file = new File(Constants.DEFAULT_APP_DATA_PATH, "PosVersion.xml");
        File file = context.getFileStreamPath("PosVersion.xml");

        try {
            if (!file.exists()) {
                file.createNewFile();
            }

            EasyUtil.writeFile(file, msg.getBytes(Constants.STRING_FORMAT_UTF_8));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<SLoginInfoFile> readVersionFile(Context context) {
        List<SLoginInfoFile> fileList = null;
        //File file = new File(Constants.DEFAULT_DOWNLOAD_PATH, "PosVersion.xml");
//        File filePath = context.getFilesDir();
        File file = context.getFileStreamPath("PosVersion.xml");
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            String versionInfo = EasyUtil.getStringFromFile(file);
            if (versionInfo == null || versionInfo.length() < 1) {
                return null;
            }
            SLoginInfos loginInfos = (SLoginInfos) ConvertUtil.convertXmlToObject(versionInfo, SLoginInfoFile.class, SLoginInfos.class);
            return loginInfos.getLoginInfoFileList();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileList;
    }

    public static void deleteVersionFile(Context context) {
        //File file = new File(Constants.DEFAULT_DOWNLOAD_PATH, "PosVersion.xml");
//        File file = new File(Constants.DEFAULT_APP_DATA_PATH, "PosVersion.xml");
        File file = context.getFileStreamPath("PosVersion.xml");
        if (file.exists()) {
            file.delete();
        }
    }

}
