package com.kicc.easypos.tablet.filedownload;


import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.List;

/**
 * Created by moonkikim on 2015-08-24.
 */
@XStreamAlias("POS")
public class RLoginInfo {

    @XStreamAsAttribute
    @XStreamAlias("RESPONSE")
    private String response;
    @XStreamAsAttribute
    @XStreamAlias("SYSTEMTIME")
    private String systemTime;
    @XStreamAsAttribute
    @XStreamAlias("LAST_SALE_DATE")
    private String lastSaleDate;
    @XStreamAsAttribute
    @XStreamAlias("LAST_BILL_NO")
    private String lastBillNo;
    @XStreamAsAttribute
    @XStreamAlias("POS_COUNT")
    private String posCount;
    @XStreamAsAttribute
    @XStreamAlias("PATH")
    private String path;

    @XStreamImplicit(itemFieldName = "FILE_INFO")
    private List<RLoginInfoFile> fileInfos;

    public String getLastBillNo() {
        return lastBillNo;
    }

    public void setLastBillNo(String lastBillNo) {
        this.lastBillNo = lastBillNo;
    }

    public String getLastSaleDate() {
        return lastSaleDate;
    }

    public void setLastSaleDate(String lastSaleDate) {
        this.lastSaleDate = lastSaleDate;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getSystemTime() {
        return systemTime;
    }

    public void setSystemTime(String systemTime) {
        this.systemTime = systemTime;
    }

    public String getPosCount() {
        return posCount;
    }

    public void setPosCount(String posCount) {
        this.posCount = posCount;
    }

    public List<RLoginInfoFile> getFileInfos() {
        return fileInfos;
    }

    public void setFileInfos(List<RLoginInfoFile> fileInfos) {
        this.fileInfos = fileInfos;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "RLoginInfo{" +
                "response='" + response + '\'' +
                ", systemTime='" + systemTime + '\'' +
                ", lastSaleDate='" + lastSaleDate + '\'' +
                ", lastBillNo='" + lastBillNo + '\'' +
                ", posCount='" + posCount + '\'' +
                ", path='" + path + '\'' +
                '}';
    }
}

