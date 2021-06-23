package com.kicc.easypos.tablet.filedownload;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("LOGIN_INFO")
public class SLoginInfo {
    @XStreamAlias("HEAD_OFFICE_NO")
    private String headOfficeNo;                // 처리구분
    @XStreamAlias("SHOP_NO")
    private String shopNo;
    @XStreamAlias("POS_NO")
    private String posNo;
    @XStreamAlias("USER_ID")
    private String userId;
    @XStreamAlias("PASSWD")
    private String passwd;

    public String getHeadOfficeNo() {
        return headOfficeNo;
    }

    public void setHeadOfficeNo(String headOfficeNo) {
        this.headOfficeNo = headOfficeNo;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getPosNo() {
        return posNo;
    }

    public void setPosNo(String posNo) {
        this.posNo = posNo;
    }

    public String getShopNo() {
        return shopNo;
    }

    public void setShopNo(String shopNo) {
        this.shopNo = shopNo;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
