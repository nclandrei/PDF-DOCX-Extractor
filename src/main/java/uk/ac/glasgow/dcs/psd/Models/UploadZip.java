package uk.ac.glasgow.dcs.psd.Models;

/**
 * UploadZip is a class that models the attributes of a Zip file.
 */
public class UploadZip {
    private int status;
    private String href;
    private String filename;
    private String message;

    /**
     * Constructor for the UploadZip class.
     *
     * @param status   status code
     * @param href     a the file to be downloaded
     * @param filename the name of the zip file
     * @param message  to print out if needed
     */
    public UploadZip(final int status,
                     final String href,
                     final String filename,
                     final String message) {
        this.status = status;
        this.href = href;
        this.filename = filename;
        this.message = message;
    }

    /**
     * Return the Zip's message.
     *
     * @return a String with the Zip's message
     */
    public final String getMessage() {
        return message;
    }

    /**
     * Set the Zip's message.
     *
     * @param message string to be set to
     */
    public final void setMessage(final String message) {
        this.message = message;
    }

    /**
     * Get this Zip's status.
     *
     * @return a int with the Zip's status
     */
    public final int getStatus() {
        return status;
    }

    /**
     * Set this Zip's status.
     *
     * @param status int to set the status to.
     */
    public final void setStatus(final int status) {
        this.status = status;
    }

    /**
     * Get the Zip's href.
     *
     * @return a String with the Zip's href
     */
    public final String getHref() {
        return href;
    }

    /**
     * Set the Zip's href.
     *
     * @param href String to set the href to
     */
    public final void setHref(final String href) {
        this.href = href;
    }

    /**
     * Get the Zip's filename.
     *
     * @return a String with the Zip's filename
     */
    public final String getFilename() {
        return filename;
    }

    /**
     * Set this Zip's filename.
     *
     * @param filename String to set the filename to
     */
    public final void setFilename(final String filename) {
        this.filename = filename;
    }

}
