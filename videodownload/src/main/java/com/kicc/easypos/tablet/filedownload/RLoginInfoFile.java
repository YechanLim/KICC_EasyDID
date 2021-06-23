package com.kicc.easypos.tablet.filedownload;


import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * Created by moonkikim on 2015-08-24.
 */
@XStreamAlias("FILE_INFO")
public class RLoginInfoFile {

    @XStreamAsAttribute
    @XStreamAlias("NO")
    private String no;                     // 순번
    @XStreamAlias("FILE_TYPE")
    private String fileType;               // 파일타입 zip, apk..
    @XStreamAlias("FILE_NAME")
    private String fileName;               // 파일명
    @XStreamAlias("FILE_VERSION")
    private String fileVersion;            // 파일버전
    @XStreamAlias("FILE_DIRECTORY")
    private String fileDirectory;          // 설치경로


    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileVersion() {
        return fileVersion;
    }

    public void setFileVersion(String fileVersion) {
        this.fileVersion = fileVersion;
    }

    public String getFileDirectory() {
        return fileDirectory;
    }

    public void setFileDirectory(String fileDirectory) {
        this.fileDirectory = fileDirectory;
    }

    @Override
    public String toString() {
        return "RLoginInfoFile{" +
                "no='" + no + '\'' +
                ", fileType='" + fileType + '\'' +
                ", fileName='" + fileName + '\'' +
                ", fileVersion='" + fileVersion + '\'' +
                ", fileDirectory='" + fileDirectory + '\'' +
                '}';
    }
}

