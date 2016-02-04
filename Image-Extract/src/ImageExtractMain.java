import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFPictureData;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ImageExtractMain {

    public static void main(String[] args) {
        selectWord();
    }
    //allow office word file selection for extracting
    public static void selectWord() {
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("DOCX","docx");
        chooser.setFileFilter(filter);
        chooser.setMultiSelectionEnabled(false);
        int returnVal = chooser.showOpenDialog(null);
        /* this allows to select .docx file via clicking on a GUI
                   - will be removed when integrated -        */
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            File file=chooser.getSelectedFile();
            System.out.println(file.toString());
            System.out.println("Please wait...");
            extractImages(file.toString());
            System.out.println("Extraction complete");
        }
    }

    public static void extractImages(String src) {
        try{
            //create file inputstream to read from a binary file
            FileInputStream is = new FileInputStream(src);
            //create office word 2007+ document object to wrap the word file
            XWPFDocument docx = new XWPFDocument(is);
            //get all images from the document and store them in the list piclist
            List<XWPFPictureData> piclist=docx.getAllPictures();
            //traverse through the list and write each image to a file
            Iterator<XWPFPictureData> iterator=piclist.iterator();
            // list of strings containing the paths to the images
            List<File> filesList = new ArrayList<File>();
            createDir();

            int i=0;
            while(iterator.hasNext()){
                XWPFPictureData pic=iterator.next();
                byte[] bytepic=pic.getData();
                BufferedImage imag = ImageIO.read(new ByteArrayInputStream(bytepic));
                // parsing through each image, checking if type is either PNG or JPEG, then
                // writing it on disk and adding it to the list of paths
                switch (pic.getPictureType()) {
                    case 6:
                        File pngImage = new File(getSrcDirectory() + "/images/imageFromWord" + i + ".png");
                        ImageIO.write(imag, "png", pngImage);
                        i++;
                        filesList.add(pngImage);
                        break;
                    case 5:
                        File jpgImage = new File(getSrcDirectory() + "/images/imageFromWord" + i + ".jpg");
                        ImageIO.write(imag, "jpg", jpgImage);
                        i++;
                        filesList.add(jpgImage);
                        break;
                    default:
                        break;
                }
            }
            ZipDir zipper = new ZipDir(getSrcDirectory() + "/images", filesList);
            zipper.getAllFiles();
            zipper.writeZipFile();
            // adding images to archive then downloading it on disk
        }
        catch(Exception e) {
            System.exit(-1);
        }
    }

    private static String getSrcDirectory() {
        String dirPath = System.getProperty("user.dir") + System.getProperty("file.separator")
                + "extractedFiles" + System.getProperty("file.separator");
        File dir = new File(dirPath);
        dir.mkdir();
        return dirPath;
    }

    public static void createDir () {
        File file = new File(getSrcDirectory() + "/images");
        if (!file.exists()) {
            if (file.mkdir()) {
                System.out.println("Dir was created!");
            }
            else {
                System.out.println("Failed to create dir!");
            }
        }
    }
}