package com.kicc.easypos.tablet.filedownload;

import android.os.Environment;

/**
 * Constants used for persisting the app's objects
 */
public class Constants {


    private Constants() {

    }



    /**
     * 문자열 포맷
     */
    public static final String STRING_FORMAT_EUC_KR = "EUC-KR";
    public static final String STRING_FORMAT_UTF_8 = "UTF-8";

    /**
     * 서버 요청 URL 정의
     */
    // URL
    public static final String DOMAIN_EASYPOS = "https://smart.easypos.net/servlet/EasyPosChannelSVL?";
    public static final String SEARCH_LOGIN_URL = DOMAIN_EASYPOS + "cmd=TlxEasyTabletLoginCMD";                        // 로그인


    /******************************************************************************************************************************
     * 가맹점 기본정보
     ******************************************************************************************************************************/

    public static final String PREF_KEY_SHOP_INFO = "pref_key_shop_info";                                   // 매장정보
    public static final String PREF_KEY_HEAD_OFFICE_NO = "pref_key_head_office_no";                         // 본부코드
    public static final String PREF_KEY_SHOP_NO = "pref_key_shop_no";                                       // 매장코드
    public static final String PREF_KEY_USER_ID = "pref_key_user_id";                                       // 아이디
    public static final String PREF_KEY_USER_PW = "pref_key_user_pw";                                       // 패스워드
    public static final String PREF_KEY_FOLDER_NM = "pref_key_folder_nm";                                   // Default.zip파일 내 사용할 폴더명



    public static final String INTENT_EXTRA_FILE_PATH = "intent_extra_file_path";

    public static final int INTENT_REQUEST_CODE_SETTING = 1;

}

