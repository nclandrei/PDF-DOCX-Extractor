package uk.ac.glasgow.dcs.psd.Components;

import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import com.google.common.io.Files;
import org.springframework.stereotype.Component;
import uk.ac.glasgow.dcs.psd.Models.UploadZip;

import java.io.*;
import java.util.Date;

/**
 * Checksum component used for creating and accessing checksum of uploaded
 * files
 */

@Component
public class ChecksumComponent {

    /**
     * <h1>Get link of file with right checksum or null</h1> Returns a Linked
     * List of tables from a docx, which are each in turn a Linked List of rows,
     * each row is a comma-separated String of all the entries in that row.
     *
     * @param filename        file to get checksum of
     * @param originalFile    original File to get checksum of
     * @param dropboxUpload   option to upload file to dropbox
     * @param dropboxDownload option to download file from dropbox
     * @return String link to download file or null if no file exist
     */
    public static UploadZip getChecksum(final String filename,
                                        final File originalFile,
                                        final boolean dropboxUpload,
                                        final boolean dropboxDownload)
            throws IOException {
        HashCode hc = Files.hash(originalFile, Hashing.sha1());

        return checkChecksum(filename, originalFile, hc, dropboxUpload,
                dropboxDownload);
    }

    /**
     * <h1>Check checksum of a file and return right file or null</h1> Returns a
     * Linked List of tables from a docx, which are each in turn a Linked List
     * of rows, each row is a comma-separated String of all the entries in that
     * row.
     *
     * @param filename        file to get checksum of
     * @param originalFile    original File to get checksum of
     * @param dropboxUpload   option to upload file to dropbox
     * @param dropboxDownload option to download file from dropbox
     * @param hc              hashcode of checksum
     * @return String link to download file or null if no file exist
     */
    private static UploadZip checkChecksum(final String filename,
                                           final File originalFile,
                                           final HashCode hc,
                                           final boolean dropboxUpload,
                                           final boolean dropboxDownload) {
        try (BufferedReader br = new BufferedReader(
                new FileReader("checksums.txt"))) {
            String sCurrentLine;
            while ((sCurrentLine = br.readLine()) != null) {
                if (sCurrentLine.contains(hc.toString())) {
                    if (dropboxDownload) {
                        return DropboxComponent.dropboxDownload(filename);
                    }
                    String href = "/file/" + sCurrentLine.substring(
                            sCurrentLine.indexOf("FileName:") + 9,
                            sCurrentLine.indexOf(":FileName")).substring(
                            0, filename.lastIndexOf(".")
                    );
                    return new UploadZip(1, href, filename, 0,
                            "Upload and Conversion was successful");
                }
            }
            addChecksumToFile(filename, originalFile, hc, dropboxUpload);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * <h1>Add checksum to checksums file</h1> If checksum is not in checksums
     * file add it
     *
     * @param filename      file to get checksum of
     * @param originalFile  original File to get checksum of
     * @param dropboxUpload option to upload file to dropbox
     * @param hc            hashcode of checksum
     */
    private static void addChecksumToFile(final String filename,
                                          final File originalFile,
                                          final HashCode hc,
                                          final boolean dropboxUpload) {
        try (PrintWriter out = new PrintWriter(new BufferedWriter(
                new FileWriter("checksums.txt", true)))
        ) {
            if (dropboxUpload)
                out.println(hc + " " + DropboxComponent.dropboxUpload(
                        originalFile, filename, "/Apps/team-project/")
                        + ":id FileName:" + filename + ":FileName " + new Date()
                );
            else
                out.println(
                        hc + " FileName:" + filename + ":FileName " + new Date()
                );
        } catch (IOException ignored) {
        }
    }
}