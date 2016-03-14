package uk.ac.glasgow.dcs.psd.Components;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;

/**
 * Helper class designed to provide util methods to other components
 */
@Component
public class HelperComponent {
    /**
     * Deletes the directory created with tables, images and README file
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
                    System.out.println(
                            "Directory is deleted : " + file.getAbsolutePath());
                }
            }
        } else {
            //if file, then delete it
            file.delete();
        }
    }

    /**
     * Method that returns the full path of a file provided as a parameter
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

}
