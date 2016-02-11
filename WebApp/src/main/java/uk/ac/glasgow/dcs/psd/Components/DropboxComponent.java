package uk.ac.glasgow.dcs.psd.Components;

import com.dropbox.core.DbxAuthInfo;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.json.JsonReader;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.DbxFiles;
import com.dropbox.core.v2.DbxPathV2;
import com.dropbox.core.v2.DbxSharing;
import org.springframework.stereotype.Component;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

@Component
public class DropboxComponent {

    public static String dropboxUpload(File file, String fileName, String DropboxPath) {
        String argAuthFile = "dropbox.auth";
        String localPath = file.getAbsolutePath();
        String dropboxPath = DropboxPath + fileName;

        // Read auth info file.
        DbxAuthInfo authInfo;
        try {
            authInfo = DbxAuthInfo.Reader.readFromFile(argAuthFile);
        }
        catch (JsonReader.FileLoadException ex) {
            System.err.println("Error loading <auth-file>: " + ex.getMessage());
            return null;
        }

        String pathError = DbxPathV2.findError(dropboxPath);
        if (pathError != null) {
            System.err.println("Invalid <dropbox-path>: " + pathError);
            return null;
        }

        // Create a DbxClientV2
        String userLocale = Locale.getDefault().toString();
        DbxRequestConfig requestConfig = new DbxRequestConfig("Uploading-csv", userLocale);
        DbxClientV2 dbxClient = new DbxClientV2(requestConfig, authInfo.accessToken, authInfo.host);

        // Make the API call to upload the file.
        DbxFiles.FileMetadata metadata;
        try {
            try (InputStream in = new FileInputStream(localPath)) {
                metadata = dbxClient.files.uploadBuilder(dropboxPath).run(in);
            }
        }
        catch (DbxFiles.UploadException ex) {
            System.out.println("Error uploading to Dropbox: " + ex.getMessage());
            return null;
        }
        catch (DbxException ex) {
            System.out.println("Error uploading to Dropbox: " + ex.getMessage());
            return null;
        }
        catch (IOException ex) {
            System.out.println("Error reading from file \"" + localPath + "\": " + ex.getMessage());
            return null;
        }

        return metadata.id;
    }

    private static String dropboxDownload(String fileName) {
        String argAuthFile = "dropbox.auth";
        String filePath = "/Apps/team-project/" + fileName;

        // Read auth info file.
        DbxAuthInfo authInfo;
        try {
            authInfo = DbxAuthInfo.Reader.readFromFile(argAuthFile);
        }
        catch (JsonReader.FileLoadException ex) {
            System.err.println("Error loading <auth-file>: " + ex.getMessage());
            return null;
        }

        // Create a DbxClientV2
        String userLocale = Locale.getDefault().toString();
        DbxRequestConfig requestConfig = new DbxRequestConfig("Uploading-csv", userLocale);
        DbxClientV2 dbxClient = new DbxClientV2(requestConfig, authInfo.accessToken, authInfo.host);

        DbxSharing.GetSharedLinksArg getSharedLinksArg = new DbxSharing.GetSharedLinksArg(filePath);
        DbxSharing.CreateSharedLinkArg link = new DbxSharing.CreateSharedLinkArg(filePath, true, DbxSharing.PendingUploadMode.file);
        return link.toString();
    }

}