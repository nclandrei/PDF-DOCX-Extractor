package uk.ac.glasgow.dcs.psd;

import org.apache.poi.xwpf.usermodel.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ExtractDocx {
    public static void extractTablesAndImages (String input, String output) {
        LinkedList<LinkedList<String>> tables = getTables(input);
        int counter = 0;
        File directory = new File(output);

        // creating the directory and adding all the csv file to the output directory
        try {
            directory.mkdir();
            assert tables != null;
            for(LinkedList<String> table: tables){
                File outFile = new File(output + File.separator + "table" + counter + ".csv");
                if(!outFile.exists()) //noinspection ResultOfMethodCallIgnored
                    outFile.createNewFile();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                        new FileOutputStream(outFile), "utf-8"));
                for(String row: table){
                    writer.write(row);
                    writer.newLine();
                }
                writer.close();
                counter++;
            }

        }
        catch(Exception e) {
            e.printStackTrace();
        }

        // we start extracting the images and adding them to the zip file
        try {
            //create file inputstream to read from a binary file
            FileInputStream is = new FileInputStream(input);
            //create office word 2007+ document object to wrap the word file
            XWPFDocument docx = new XWPFDocument(is);
            //get all images from the document and store them in the list piclist
            List<XWPFPictureData> piclist = docx.getAllPictures();
            //traverse through the list and write each image to a file
            Iterator<XWPFPictureData> iterator = piclist.iterator();
            // list of strings containing the paths to the images
            List<File> filesList = new ArrayList<File>();

            int i = 0;
            while (iterator.hasNext()) {
                XWPFPictureData pic = iterator.next();
                byte[] bytepic = pic.getData();
                BufferedImage imag = ImageIO.read(new ByteArrayInputStream(bytepic));
                // parsing through each image, checking if type is either PNG or JPEG, then
                // writing it on disk and adding it to the list of paths
                switch (pic.getPictureType()) {
                    case 6:
                        File pngImage = new File(output + File.separator + "image" + i + ".png");
                        ImageIO.write(imag, "png", pngImage);
                        i++;
                        filesList.add(pngImage);
                        break;
                    case 5:
                        File jpgImage = new File(output + File.separator + "image" + i + ".jpg");
                        ImageIO.write(imag, "jpg", jpgImage);
                        i++;
                        filesList.add(jpgImage);
                        break;
                    default:
                        break;
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        ZipMaker.createZip(output);

        try{
            ZipMaker.delete(directory);
        }
        catch( Exception e){
            e.printStackTrace();
        }

    }

    public static LinkedList<LinkedList<String>> getTables(String fileName){
        XWPFDocument testFile;
        try {
            testFile = new XWPFDocument(new FileInputStream(fileName));
        } catch (IOException e) {
            System.out.printf("ERROR: File <%s> not found\n", fileName);
            e.printStackTrace();
            return null;
        }

        LinkedList<LinkedList<String>> results = new LinkedList<>();
        String rowString;
        for(XWPFTable table: testFile.getTables()){
            results.add(new LinkedList<>());
            for(XWPFTableRow row:table.getRows()){
                results.getLast().add("");
                for(XWPFTableCell cell: row.getTableCells()){
                    if(!cell.getText().equals(" ") && !cell.getText().equals("")) {
                        rowString = results.getLast().getLast();
                        if(rowString.equals("")){
                            rowString += "\"" + cell.getText() + "\"";
                        }else{
                            rowString += ",\"" + cell.getText()+"\"";
                        }
                        if(!rowString.equals("\n"))
                            results.getLast().set(results.getLast().size()-1, rowString);
                    }
                }
            }
        }
        return results;
    }
}
