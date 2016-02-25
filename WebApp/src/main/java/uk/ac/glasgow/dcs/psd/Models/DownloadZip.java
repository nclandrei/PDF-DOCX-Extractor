package uk.ac.glasgow.dcs.psd.Models;

public class DownloadZip {
    int status;
    String href;
    String filename;
    int fileSize;
    String message;

    public DownloadZip(int status, String href, String filename, int fileSize, String message) {
        this.status = status;
        this.href = href;
        this.filename = filename;
        this.fileSize = fileSize;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public int getFileSize() {
        return fileSize;
    }

    public void setFileSize(int fileSize) {
        this.fileSize = fileSize;
    }
}
