package com.kicc.easypos.tablet.filedownload;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by POS_KJH on 2017-12-29.
 */

public class FileDownloader extends AsyncTask<RLoginInfoFile, String, ArrayList<String>> {

    private static final String TAG = "FileDownloader";

    private static final String DOWNLOAD_PATH = Environment.getExternalStorageDirectory() + "/easypos_download";
    private String mBaseUrl;
    private String mFolderNm; // Default.zip 파일 내 사용할 폴더명
    private String mFilePath; // 동영상이나 이미지 파일을 DID에서 사용할 경로
    private String mXmlResponse;

    private Context mContext;

    public interface OnDownloadListener {

        void onDownloadStart();

        void onProgressUpdate(String... progress);

        void onDownloadEnd(ArrayList<String> writeFilePath);

        void onDownloadError(Exception e);
    }



    public void setContext(Context context) {
        mContext = context;
    }

    private OnDownloadListener mOnDownloadListener;

    public void setOnDownloadListener(OnDownloadListener onDownloadListener) {
        mOnDownloadListener = onDownloadListener;
    }


    /**
     * Before starting background thread Show Progress Bar Dialog
     */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (mOnDownloadListener != null) {
            mOnDownloadListener.onDownloadStart();
        }
    }

    public void setBaseUrl(String baseUrl) {
        mBaseUrl = baseUrl;
    }

    public void setFolderNm(String folderNm) {
        mFolderNm = folderNm;
    }

    public void setFilePath(String filePath) {
        mFilePath = filePath;
    }

    public void setXmlResponse(String xmlResponse) {
        mXmlResponse = xmlResponse;
    }

    /**
     * @param downloadFileList 0: 다운로드할 URL
     *                         1. 저장할 파일풀 경로
     *                         2,3 부가정보
     * @return
     */

    @Override
    protected ArrayList<String> doInBackground(RLoginInfoFile... downloadFileList) {

        ArrayList<String> writeFilePathList = new ArrayList<>();

        // 기존 기존 버전정보 리스트에서
        List<SLoginInfoFile> versionInfoList = VersionUtil.readVersionFile(mContext);

        try {
            for (RLoginInfoFile fileInfo : downloadFileList) {

                if (fileInfo.getFileType().equals("apk")) {
                    continue;
                }

                File dirFile = new File(DOWNLOAD_PATH);
                if (dirFile.exists()) {
                    EasyUtil.deleteDir(dirFile);
                }

                String strUrl = String.format("%s%s/%s/%s/%s.%s", mBaseUrl, fileInfo.getFileType(), fileInfo.getFileName(), fileInfo.getFileVersion(), fileInfo.getFileName(), fileInfo.getFileType());

//                String writeFilePath = DOWNLOAD_PATH + "/" + fileInfo.getFileDirectory() + File.separator + fileInfo.getFileName() + "." + fileInfo.getFileType();
                String writeFilePath = DOWNLOAD_PATH + "/" + fileInfo.getFileName() + "." + fileInfo.getFileType();

                Log.d(TAG, "strUrl : " + strUrl);

                Log.d(TAG, "WriteFilePath : " + writeFilePath);

                int count = 0;

                URL url = new URL(strUrl);
                URLConnection conection = url.openConnection();
                conection.connect();

                // this will be useful so that you can show a tipical 0-100%
                // progress bar
                int lenghtOfFile = conection.getContentLength();

                // download the file
                InputStream input = new BufferedInputStream(url.openStream(),
                        8192);

                // Output stream
                int directoryPathLastIndex = writeFilePath.lastIndexOf("/");
                if (directoryPathLastIndex != -1) {
                    String directoryPath = writeFilePath.substring(0, directoryPathLastIndex);
                    File path = new File(directoryPath);
                    if (!path.exists()) {
                        path.mkdirs();
                    }
                }

                OutputStream output = new FileOutputStream(writeFilePath);

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
//                    publishProgress(writeFilePath + "\n\n" + (int) ((total * 100) / lenghtOfFile));
                    publishProgress(String.valueOf((total * 100) / lenghtOfFile));
                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();

//                // 이전 파일 삭제
//                File dirFile = new File(DOWNLOAD_PATH);
//                if (dirFile.exists()) {
//                    File[] fileList = dirFile.listFiles();
//                    for (File file : fileList) {
//                        if (file.isFile() && !file.getName().equals("Default.zip")) {
//                            file.delete();
//
//                        }
//                    }
//                }


                dirFile = new File(Environment.getExternalStorageDirectory() + mFilePath);
                if (dirFile.exists()) {
                    File[] fileList = dirFile.listFiles();
                    for (File file : fileList) {
                        if (file.isFile() && !file.getName().equals("Default.zip")) {
                            file.delete();
                        }
                    }
                }


                // Zip 확장자 풀기
                if ("zip".equals(fileInfo.getFileType())) {
                    if ("Default".equals(fileInfo.getFileName())) {

                        unzip(writeFilePath, DOWNLOAD_PATH, null);
                        if (mFolderNm == null || mFolderNm.equals("")) {
                            EasyUtil.copyAllFiles(DOWNLOAD_PATH, Environment.getExternalStorageDirectory() + mFilePath);
                        } else {
                            EasyUtil.copyAllFiles(DOWNLOAD_PATH + "/" + mFolderNm, Environment.getExternalStorageDirectory() + mFilePath);
                        }
                    }
                }

                // 하나의 파일을 완료 받은 후엔 해당항목에 대한 PosVersion.xml 을 업데이트 시켜줌
                if (versionInfoList != null) {
                    for (SLoginInfoFile versionFiles : versionInfoList) {
                        if (versionFiles.getFileName().equals(fileInfo.getFileName())) {
                            versionFiles.setFileName(fileInfo.getFileName());
                            versionFiles.setFileVersion(fileInfo.getFileVersion());
                            versionFiles.setFileType(fileInfo.getFileType());
                            break;
                        }
                    }
                }

                writeFilePathList.add(writeFilePath);
            }
        } catch (Exception e) {
            Log.e(TAG, "Exception ..\n" + e.getMessage());
            if (mOnDownloadListener != null) {
                mOnDownloadListener.onDownloadError(e);
            }
            return null;
        } finally {

            // 버전파일이 없는 상태에서 서버로 데이터 요청 시에는 서버에서 보내준 데이터로 PosVersion.xml 파일을 생성한다.
            if (versionInfoList == null) {
                versionInfoList = new ArrayList<>();
                for (RLoginInfoFile fileInfo : downloadFileList) {
                    SLoginInfoFile sFile = new SLoginInfoFile();
                    sFile.setFileName(fileInfo.getFileName());
                    sFile.setFileVersion(fileInfo.getFileVersion());
                    sFile.setFileType(fileInfo.getFileType());
                    versionInfoList.add(sFile);
                }
            } else {
                // 버전파일이 있는 상태에서 새로운 파일이 추가되었는지 여부를 확인한다.
                for (RLoginInfoFile rFile : downloadFileList){
                    boolean isNewFile = true;
                    for (SLoginInfoFile sFile : versionInfoList){
                        if (sFile.getFileName().equals(rFile.getFileName())){
                            isNewFile = false;
                            break;
                        }
                    }
                    if (isNewFile){
                        SLoginInfoFile sFile = new SLoginInfoFile();
                        sFile.setFileName(rFile.getFileName());
                        sFile.setFileVersion(rFile.getFileVersion());
                        sFile.setFileType(rFile.getFileType());
                        versionInfoList.add(sFile);
                    }
                }
            }

            SLoginInfos sLoginInfo = new SLoginInfos();
            sLoginInfo.setLoginInfoFileList(versionInfoList);

            String body = ConvertUtil.convertObjectToXml(sLoginInfo, SLoginInfos.class);
            VersionUtil.makeVersionFile(mContext, body);
        }
        return writeFilePathList;
    }

    /**
     * Updating progress bar
     */
    protected void onProgressUpdate(String... progress) {
        // setting progress percentage
        if (mOnDownloadListener != null) {
            mOnDownloadListener.onProgressUpdate(progress);
        }
    }

    /**
     * After completing background task Dismiss the progress dialog
     **/
    @Override
    protected void onPostExecute(ArrayList<String> writeFilePathList) {
        if(writeFilePathList != null) {
            if (mOnDownloadListener != null && writeFilePathList.size() > 0) {
                mOnDownloadListener.onDownloadEnd(writeFilePathList);
            }
        }
    }

    private void unzip(String source, String destination, String password) {
        try {
            ZipFile zipFile = new ZipFile(source);
            if (zipFile.isEncrypted() && password != null) {
                zipFile.setPassword(password);
            }
            zipFile.setFileNameCharset("euc-kr");
            zipFile.extractAll(destination);

            // ZipFile 압축해제 후 기존 파일 삭제
            File f = new File(source);
            if (f.exists()) {
                f.delete();
            }
        } catch (ZipException e) {
            e.printStackTrace();
        }
    }
}