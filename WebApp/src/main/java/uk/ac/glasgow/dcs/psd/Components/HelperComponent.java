package uk.ac.glasgow.dcs.psd.Components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.Random;

/**
 * Helper class designed to provide util methods to other components.
 */
@Component
public class HelperComponent {
    /**
     * Deletes the directory created with tables, images and README file.
     *
     * @param file the file/folder to be deleted
     * @throws IOException will throw an IOException if the file is not found
     */
    public static void delete(File file) throws IOException {

        if (file.isDirectory()) {
            //directory is empty, then delete it
            if (file.list().length == 0) {
                file.delete();
            } else {
                //list all the directory contents
                String files[] = file.list();

                for (String temp : files) {
                    //construct the file structure
                    File fileDelete = new File(file, temp);

                    //recursive delete
                    delete(fileDelete);
                }
                //check the directory again, if empty then delete it
                if (file.list().length == 0) {
                    file.delete();
                }
            }
        } else {
            //if file, then delete it
            file.delete();
        }
    }

    /**
     * Method that returns the full path of a file provided as a parameter.
     *
     * @param fileName the file/directory to find the path of
     * @return returns a string that represents the path of the file/directory
     */
    public static String getFileLocation(String fileName) {
        String separator = System.getProperty("file.separator");
        return (System.getProperty("user.dir") + separator) +
                String.format("src%smain%sresources%sstatic%suploads%s%s",
                        separator, separator, separator, separator, separator,
                        fileName);
    }

    /**
     * Randomise filename by adding a random
     * digit in front of it.
     * @param fileName original filename
     * @return fileName
     */
    public static String RandomizeFilename(String fileName) {
        int randomNumber = new Random().nextInt(1000000);
        fileName = randomNumber + " " + fileName;
        return fileName;
    }

    /**
     * Get base url of current application.
     * Local copy example http://localhost:8080
     * @return  baseUrl
     */
    public static String getBaseUrl(HttpServletRequest request) {
        return String.format("http://%s:%s",
                request.getServerName(),
                request.getServerPort() != 80 ? request.getServerPort() : "");
    }

}
