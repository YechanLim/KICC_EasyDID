package com.kicc.easypos.tablet.filedownload;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DownloadActivity extends AppCompatActivity {

    private Button btnReDownload;
    private Button btnClose;
    private Button btnConfig;
    private Context mContext;

    private ProgressBar mPbReceiveStatus;
    private TextView mTvReceiveStatus;
    private TextView mTvMsg;

    private FileDownloader mFileDownloader;

    private SharedPreferences mPreference;
    private String mHeadOfficeNo;
    private String mShopNo;
    private String mUserId;
    private String mUserPw;
    private String mFolderNm;
    private String mFilePath;

    private boolean mIsClose = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.download_activity);

        mFilePath = getIntent().getStringExtra(Constants.INTENT_EXTRA_FILE_PATH);
        if (mFilePath == null || mFilePath.equals("")) {
            mFilePath = "/easypos_did";
        }

        mContext = this;

        mPreference = PreferenceManager.getDefaultSharedPreferences(mContext);

        mPbReceiveStatus = findViewById(R.id.pbReceiveStatus);
        mTvReceiveStatus = findViewById(R.id.tvReceiveStatus);
        mTvMsg = findViewById(R.id.tvMsg);

        mPbReceiveStatus.setMax(100);
        mPbReceiveStatus.setProgress(0);
        mTvReceiveStatus.setText(0 + "%");

        btnConfig = findViewById(R.id.btnConfig);
        btnConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIsClose = false;

                if (mFileDownloader != null) {
                    mFileDownloader.cancel(true);
                }
                startActivityForResult(new Intent(mContext, SettingActivity.class), Constants.INTENT_REQUEST_CODE_SETTING);
            }
        });

        btnReDownload = findViewById(R.id.btnReDownload);
        btnReDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIsClose = false;
                VersionUtil.deleteVersionFile(mContext);
                startDownload();
            }
        });

        btnClose = findViewById(R.id.btnClose);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mFileDownloader != null) {
                    mFileDownloader.cancel(true);
                }
                setResult(RESULT_OK);
                finish();
            }
        });

        startDownload();
    }

    private void startDownload() {

        mHeadOfficeNo = mPreference.getString(Constants.PREF_KEY_HEAD_OFFICE_NO, null);
        mShopNo = mPreference.getString(Constants.PREF_KEY_SHOP_NO, null);
        mUserId = mPreference.getString(Constants.PREF_KEY_USER_ID, null);
        mUserPw = mPreference.getString(Constants.PREF_KEY_USER_PW, null);
        mFolderNm = mPreference.getString(Constants.PREF_KEY_FOLDER_NM, "");

        if ((mHeadOfficeNo == null || mHeadOfficeNo.equals(""))
                || (mShopNo == null || mShopNo.equals(""))
                || (mUserId == null || mUserId.equals(""))
                || (mUserPw == null || mUserPw.equals(""))) {

            startActivity(new Intent(mContext, SettingActivity.class));
            return;
        }

        if (EasyUtil.checkAndRequestPermission(this, 0x01, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            downloadFile(mContext);
        }
    }

    private void downloadFile(final Context context) {

        mTvMsg.setText("다운로드 매장 정보를 조회 중 입니다.");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.SEARCH_LOGIN_URL,
                new Response.Listener<String>() {
                    @TargetApi(Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onResponse(String response) {
                        RLoginInfo mLoginInfo = (RLoginInfo) ConvertUtil.convertXmlToObject(response, RLoginInfo.class);

                        List<RLoginInfoFile> fileList = mLoginInfo.getFileInfos();
                        if (mLoginInfo.getResponse().equals("0000")) {

                            if (fileList == null || fileList.size() < 1) {

                                mTvMsg.setText("다운로드 파일이 없습니다. 3초 후 창이 닫힙니다.");

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (mIsClose) {
                                            setResult(RESULT_OK);
                                            finish();
                                        } else {
                                            mIsClose = true;
                                        }
                                    }
                                }, 3000);

                                return;
                            }
                            mFileDownloader = new FileDownloader();
                            mFileDownloader.setContext(context);
                            mFileDownloader.setBaseUrl(mLoginInfo.getPath());
                            mFileDownloader.setXmlResponse(response);
                            mFileDownloader.setFolderNm(mFolderNm);
                            mFileDownloader.setFilePath(mFilePath);

                            mFileDownloader.setOnDownloadListener(new FileDownloader.OnDownloadListener() {
                                @Override
                                public void onDownloadStart() {
                                    mPbReceiveStatus.setProgress(0);
                                    mTvReceiveStatus.setText(0 + "%");
                                    mTvMsg.setText("다운로드를 시작합니다.");
//                                    mProgressViewer.updateMessage(getString(R.string.easy_login_message_04));
                                }

                                @Override
                                public void onProgressUpdate(String... progress) {
                                    mPbReceiveStatus.setProgress(Integer.parseInt(progress[0]));
                                    mTvReceiveStatus.setText(progress[0] + "%");
                                    mTvMsg.setText("다운로드 중 입니다.");
                                }

                                @Override
                                public void onDownloadEnd(ArrayList<String> writeFilePath) {

                                    mTvMsg.setText("다운로드가 완료 되었습니다. 3초 후 창이 닫힙니다.");

                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (mIsClose) {
                                                setResult(RESULT_OK);
                                                finish();
                                            } else {
                                                mIsClose = true;
                                            }
                                        }
                                    }, 3000);

                                }

                                @Override
                                public void onDownloadError(final Exception e) {


                                }
                            });

                            mIsClose = true;
                            mFileDownloader.execute(fileList.toArray(new RLoginInfoFile[fileList.size()]));

                        } else {

                            mTvMsg.setText("아이디 또는 패스워드가 틀립니다.");

//                            EasyToast.showText(EasyPosApplication.getInstance().getGlobal().context, "아이디 또는 패스워드가 틀립니다.", Toast.LENGTH_SHORT);
//                            mProgressViewer.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        mProgressViewer.dismiss();

                        if (error instanceof NoConnectionError) {
                            goFinish();
//                            EasyToast.showText(EasyPosApplication.getInstance().getGlobal().context, getString(R.string.message_1001), Toast.LENGTH_SHORT);
                        } else if (error instanceof TimeoutError) {
                            goFinish();
//                            EasyToast.showText(EasyPosApplication.getInstance().getGlobal().context, getString(R.string.message_1002), Toast.LENGTH_SHORT);
                        } else {
                            goFinish();
//                            EasyToast.showText(EasyPosApplication.getInstance().getGlobal().context, getString(R.string.message_1003), Toast.LENGTH_SHORT);
                        }

                    }
                }) {
            @Override
            public byte[] getBody() throws AuthFailureError {

                SLoginInfos sLoginInfo = new SLoginInfos();

                SLoginInfo sLogin = new SLoginInfo();
                sLogin.setHeadOfficeNo(mHeadOfficeNo);
                sLogin.setShopNo(mShopNo);
                sLogin.setPosNo("01");
                sLogin.setUserId(mUserId);
                sLogin.setPasswd(mUserPw);
                sLoginInfo.setLoginInfo(sLogin);

                List<SLoginInfoFile> sLoginInfoFile = VersionUtil.readVersionFile(context);
                sLoginInfo.setLoginInfoFileList(sLoginInfoFile);

                String body = ConvertUtil.convertObjectToXml(sLoginInfo, SLoginInfos.class);

                return body.getBytes();
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return getHeader();
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    public Map<String, String> getHeader() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "text/xml;charset=euc-kr");
        return headers;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case Constants.INTENT_REQUEST_CODE_SETTING:

        }
    }


    private void goFinish() {
        mTvMsg.setText("인터넷 연결에 문제가 있습니다. 3초 후 창이 닫힙니다.");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setResult(RESULT_OK);
                finish();
            }
        }, 3000);
    }

    private boolean isConnect() {
        ConnectivityManager manager = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        if (networkInfo != null) {
            return true;
        } else {
            return false;
        }
    }
}
