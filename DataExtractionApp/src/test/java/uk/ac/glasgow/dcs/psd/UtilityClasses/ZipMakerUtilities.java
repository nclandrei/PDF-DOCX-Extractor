package uk.ac.glasgow.dcs.psd.UtilityClasses;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Utility class that provides methods used for testing
 * the zip maker component
 */
public class ZipMakerUtilities {

    /**
     * Decompresses a zip file and sends contents to a specified folder
     * @param zipFile input zip file
     * @param outputFolder zip file output folder
     */
    public void unzip (String zipFile, String outputFolder) {
        byte[] buffer = new byte[1024];
        try {
            //create output directory is not exists
            File folder = new File(outputFolder);
            if(!folder.exists()) {
                folder.mkdir();
            }

            //get the zip file content
            ZipInputStream zis =
                    new ZipInputStream(new FileInputStream(zipFile));

            //get the zipped file list entry
            ZipEntry ze = zis.getNextEntry();

            while(ze!=null) {
                String fileName = ze.getName();
                File newFile = new File(outputFolder + File.separator + fileName);

                //create all non exists folders
                //else you will hit FileNotFoundException for compressed folder

                new File(newFile.getParent()).mkdirs();
                FileOutputStream fos = new FileOutputStream(newFile);

                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }

                fos.close();
                ze = zis.getNextEntry();
            }
            zis.closeEntry();
            zis.close();
        }
        catch(IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Utility function for deleting files
     * @param file
     */
    public void deleteFiles (File file) {
        File[] contents = file.listFiles();
        if (contents != null) {
            for (File f : contents) {
                deleteFiles(f);
            }
        }
        file.delete();
    }
}