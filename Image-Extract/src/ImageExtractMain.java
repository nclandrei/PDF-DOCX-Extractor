import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFPictureData;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Iterator;
import java.util.List;
import java.util.zip.Adler32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

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
            // initializing the zip archive
            int BUFFER = 2048;
            BufferedInputStream origin = null;
            FileOutputStream dest = new
                    FileOutputStream("D:/Desktop/imagesArchive.zip");
            CheckedOutputStream checksum = new
                    CheckedOutputStream(dest, new Adler32());
            ZipOutputStream out = new
                    ZipOutputStream(new
                    BufferedOutputStream(checksum));
            byte data[] = new byte[BUFFER];

            //create file inputstream to read from a binary file
            FileInputStream is = new FileInputStream(src);
            //create office word 2007+ document object to wrap the word file
            XWPFDocument docx = new XWPFDocument(is);
            //get all images from the document and store them in the list piclist
            List<XWPFPictureData> piclist=docx.getAllPictures();
            //traverse through the list and write each image to a file
            Iterator<XWPFPictureData> iterator=piclist.iterator();
            int i=0;
            while(iterator.hasNext()){
                XWPFPictureData pic=iterator.next();
                byte[] bytepic=pic.getData();
                BufferedImage imag = ImageIO.read(new ByteArrayInputStream(bytepic));
                switch (pic.getPictureType()) {
                    case 6:
                        File pngImage = new File("D:/Desktop/imageFromWord" + i + ".png");
                        ImageIO.write(imag, "png", pngImage);
                        FileInputStream fi = new FileInputStream(pngImage);
                        origin = new BufferedInputStream(fi, BUFFER);
                        ZipEntry entry = new ZipEntry(pngImage.toString());
                        out.putNextEntry(entry);
                        int count;
                        while ((count = origin.read(data, 0,
                                BUFFER)) != -1) {
                            out.write(data, 0, count);
                        }
                        origin.close();
                        i++;
                        break;
                    case 5:
                        File jpgImage = new File("D:/Desktop/imageFromWord" + i + ".jpg");
                        ImageIO.write(imag, "jpg", jpgImage);
                        FileInputStream fi1 = new FileInputStream(jpgImage);
                        origin = new BufferedInputStream(fi1, BUFFER);
                        ZipEntry entry1 = new ZipEntry(jpgImage.toString());
                        out.putNextEntry(entry1);
                        int count1;
                        while ((count1 = origin.read(data, 0,
                                BUFFER)) != -1) {
                            out.write(data, 0, count1);
                        }
                        origin.close();
                        i++;
                        break;
                    default:
                        break;
                }
            }
            out.close();
        }
        catch(Exception e) {
            System.exit(-1);
        }
    }
}