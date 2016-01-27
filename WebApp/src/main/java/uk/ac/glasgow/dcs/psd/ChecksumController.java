package uk.ac.glasgow.dcs.psd;

import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import com.google.common.io.Files;
import org.springframework.stereotype.Component;
import java.io.*;
import java.util.Date;

@Component
public class ChecksumController {

    public static String getChecksum(String filename, File originalFile, boolean dropbox) throws IOException {
        HashCode hc = Files.hash(originalFile, Hashing.sha1());

        return checkChecksum(originalFile, filename, hc, dropbox);
    }

    private static String checkChecksum(File originalFile, String filename, HashCode hc, boolean dropbox) {
        try (BufferedReader br = new BufferedReader(new FileReader("checksums.txt")))
        {
            String sCurrentLine;
            while ((sCurrentLine = br.readLine()) != null) {
                if (sCurrentLine.contains(hc.toString())) {
                    return "/file/" + sCurrentLine.substring(sCurrentLine.indexOf("FileName:")+9,
                            sCurrentLine.indexOf(":FileName")).substring(0, filename.lastIndexOf("."));
                }
            }
            addChecksumToFile(originalFile, filename, hc, dropbox);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void addChecksumToFile(File originalFile, String filename, HashCode hc, boolean dropbox) {
        try(PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("checksums.txt", true)))) {
            if (dropbox) out.println(hc + " " + DropboxController.dropboxUpload(originalFile, filename, "/Apps/team-project/") + ":id FileName:"
                    + filename + ":FileName " + new Date());
            else out.println(hc + " FileName:" + filename + ":FileName " + new Date());
        }catch (IOException ignored) {
        }
    }
}