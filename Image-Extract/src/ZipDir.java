import java.io.*;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipDir {
    private String directoryToZip;
    private List<File> filesList;

    public ZipDir (String dir, List<File> list) {
        this.directoryToZip = dir;
        this.filesList = list;
    }

    public void getAllFiles(File dir) {
        try {
            File[] files = dir.listFiles();
            for (File file : files) {
                filesList.add(file);
                if (file.isDirectory()) {
                    System.out.println("directory:" + file.getCanonicalPath());
                    getAllFiles(file);
                } else {
                    System.out.println("     file:" + file.getCanonicalPath());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeZipFile(File directoryToZip, List<File> fileList) {
        try {
            FileOutputStream fos = new FileOutputStream(directoryToZip.getName() + ".zip");
            ZipOutputStream zos = new ZipOutputStream(fos);
            for (File file : fileList) {
                if (!file.isDirectory()) { // we only zip files, not directories
                    addToZip(directoryToZip, file, zos);
                }
            }
            zos.close();
            fos.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addToZip(File directoryToZip, File file, ZipOutputStream zos) throws FileNotFoundException,
            IOException {
        FileInputStream fis = new FileInputStream(file);
        // we want the zipEntry's path to be a relative path that is relative
        // to the directory being zipped, so chop off the rest of the path
        String zipFilePath = file.getCanonicalPath().substring(directoryToZip.getCanonicalPath().length() + 1,
                file.getCanonicalPath().length());
        System.out.println("Writing '" + zipFilePath + "' to zip file");
        ZipEntry zipEntry = new ZipEntry(zipFilePath);
        zos.putNextEntry(zipEntry);
        byte[] bytes = new byte[1024];
        int length;
        while ((length = fis.read(bytes)) >= 0) {
            zos.write(bytes, 0, length);
        }
        zos.closeEntry();
        fis.close();
    }
}