package uk.ac.glasgow.dcs.psd.Components;

import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


/**
 * Class that creates a zip archive containing 2 folders, "csv" and "images",
 * and a README file explaining to the user what data is inside. It parses all the files
 * received from one of the PDF or DOCX extractors and adds these entries to the zip.
 */

@Component
public class ZipMakerComponent {

    /**
     * Method which creates the archive, args being the name of the zip
     * passed from one of the extractors
     * @param args
     */
    public static void createZip(String args) {

        if(args.equals(null)){
            return;
        }

        String local =  args;

        String zipFile = local + ".zip";

        File folder = new File(local);
        File[] listOfFiles = folder.listFiles();
        List <File> srcFiles = new ArrayList <>();

        for(int i = 0; i < listOfFiles.length; i++){
            //change this to .csv or other
            if(listOfFiles[i].getName().endsWith(".csv")
                    || listOfFiles[i].getName().endsWith(".png")
                    || listOfFiles[i].getName().endsWith(".jpg")){
                srcFiles.add(listOfFiles[i]);
            }
        }

        try {

            // create byte buffer
            byte[] buffer = new byte[1024];

            FileOutputStream fos = new FileOutputStream(zipFile);

            ZipOutputStream zos = new ZipOutputStream(fos);

            String zipFolder = "";

            for (int i=0; i < srcFiles.size(); i++) {

                File srcFile = srcFiles.get(i);

                FileInputStream fis = new FileInputStream(srcFile);

                //folder to place the extracted data - change here to place into alternative folders
                if(srcFile.getName().endsWith(".csv")){
                    zipFolder = "csv" + File.separator;
                }
                else if(srcFile.getName().endsWith(".png") || srcFile.getName().endsWith(".jpg")) {
                    zipFolder = "images" + File.separator;
                }

                // begin writing a new ZIP entry, positions the stream to the start of the entry data
                zos.putNextEntry(new ZipEntry(zipFolder + srcFile.getName()));

                int length;

                while ((length = fis.read(buffer)) > 0) {
                    zos.write(buffer, 0, length);
                }

                zos.closeEntry();

                // close the InputStream
                fis.close();

            }

            // adding the README file explaining to the user
            // the inside of the archive
            File readmeFile = new File("./README.txt");
            FileInputStream fis = new FileInputStream(readmeFile);
            zos.putNextEntry(new ZipEntry(readmeFile.getName()));

            int length;
            while ((length = fis.read(buffer)) > 0) {
                zos.write(buffer, 0, length);
            }

            zos.closeEntry();
            fis.close();

            // close the ZipOutputStream
            zos.close();

        }
        catch (IOException ioe) {
            System.out.println("Error creating zip file: " + ioe);
            return;
        }

        return;

    }
}
