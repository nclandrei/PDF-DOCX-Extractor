/**
 * @author Andrei-Mihai Nicolae
 */

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.apache.poi.xwpf.usermodel.XWPFPictureData;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

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
            //create file inputstream to read from a binary file
            FileInputStream is = new FileInputStream(src);
            //create office word 2007+ document object to wrap the word file
            XWPFDocument docx = new XWPFDocument(is);
            //get all images from the document and store them in the list piclist
            List<XWPFPictureData> piclist=docx.getAllPictures();
            //traverse through the list and write each image to a file
            Iterator<XWPFPictureData> iterator=piclist.iterator();
            System.out.println(piclist.size());
            int i=0;
            while(iterator.hasNext()){
                XWPFPictureData pic=iterator.next();
                byte[] bytepic=pic.getData();
                BufferedImage imag = ImageIO.read(new ByteArrayInputStream(bytepic));
                switch (pic.getPictureType()) {
                    case XWPFDocument.PICTURE_TYPE_PNG:
                        ImageIO.write(imag, "png", new File("/users/level3/2147392n/Desktop/imagefromword" + i + ".png"));
                        i++;
                        break;
                    case XWPFDocument.PICTURE_TYPE_JPEG:
                        ImageIO.write(imag, "jpg", new File("/users/level3/2147392n/Desktop/imagefromword" + i + ".jpg"));
                        i++;
                        break;
                    default:
                        break;
                }
            }
        }
        catch(Exception e) {
            System.exit(-1);
        }
    }
}
