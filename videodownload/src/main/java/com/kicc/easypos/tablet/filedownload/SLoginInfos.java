package com.kicc.easypos.tablet.filedownload;


import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.List;

/**
 * Created by moonkikim on 2015-08-24.
 */
@XStreamAlias("POS")
public class SLoginInfos {

    @XStreamAlias("LOGIN_INFO")
    private SLoginInfo loginInfo;

    @XStreamImplicit(itemFieldName = "FILE_INFO")
    private List<SLoginInfoFile> loginInfoFileList;

    public SLoginInfo getLoginInfo() {
        return loginInfo;
    }

    public void setLoginInfo(SLoginInfo loginInfo) {
        this.loginInfo = loginInfo;
    }

    public List<SLoginInfoFile> getLoginInfoFileList() {
        return loginInfoFileList;
    }

    public void setLoginInfoFileList(List<SLoginInfoFile> loginInfoFileList) {
        this.loginInfoFileList = loginInfoFileList;
    }
}

