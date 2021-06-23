package com.kicc.easypos.tablet.filedownload;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("FILE_INFO")
public class SLoginInfoFile {

    @XStreamAlias("FILE_NAME")
    private String fileName;
    @XStreamAlias("FILE_TYPE")
    private String fileType;
    @XStreamAlias("FILE_VERSION")
    private String fileVersion;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFileVersion() {
        return fileVersion;
    }

    public void setFileVersion(String fileVersion) {
        this.fileVersion = fileVersion;
    }
}
