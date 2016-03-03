package uk.ac.glasgow.dcs.psd.Components;

import org.apache.poi.xwpf.usermodel.*;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

@Component
public class ExtractDocxComponent {


    /**
     * <h1>Extract Tables and Images from a docx</h1>
     * <p>
     * Extracts the tables from a given docx, creates a directory named after "output"
     * containing two subdirectories named "csv" and "images", containing csvs of all
     * the tables in the docx, and each of the images found in the docx respectively.
     *
     * @param input  the file name for the docx to extract the tables from
     * @param output the name of the directory that will contain all of the csv's and images
     */
    public static void extractTablesAndImages(String input, String output) {
        LinkedList<LinkedList<String>> tables = getTables(input);
        int counter = 0;
        File directory = new File(output);

        if (tables == null) {
            System.out.printf("ERROR: No Tables found for file: %s\n", input);
            return;
        }

        // creating the directory and adding all the csv file to the output directory
        try {
            directory.mkdir();
            for (LinkedList<String> table : tables) {
                File outFile = new File(output + File.separator + "table" + counter + ".csv");
                if (!outFile.exists()) //noinspection ResultOfMethodCallIgnored
                    outFile.createNewFile();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                        new FileOutputStream(outFile), "utf-8"));
                for (String row : table) {
                    writer.write(row);
                    writer.newLine();
                }
                writer.close();
                counter++;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        // we start extracting the images and adding them to the zip file
        try {
            //create file input stream to read from a binary file
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
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        ZipMakerComponent.createZip(output);

        try {
            HelperComponent.delete(directory);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }


    /**
     * <h1>Get Tables from a docx</h1>
     * Returns a Linked List of tables from a docx, which are each in turn a Linked
     * List of rows, each row is a comma-separated String of all the entries in that row.
     *
     * @param fileName the docx file to extract the tables from
     * @return a Linked List of rows, which are in turn a Linked List of
     * cells in a table each contains a String
     * @see LinkedList
     */
    private static LinkedList<LinkedList<String>> getTables(String fileName) {
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
        for (XWPFTable table : testFile.getTables()) {
            //iterate through each table in the docx
            results.add(new LinkedList<>());
            for (XWPFTableRow row : table.getRows()) {
                //iterate through each row in the table
                results.getLast().add("");
                for (XWPFTableCell cell : row.getTableCells()) {
                    //iterate through each cell in the table
                    if (!cell.getText().equals(" ") && !cell.getText().equals("")) {
                        //if the cell is not empty

                        rowString = results.getLast().getLast();
                        //get the current String for this row

                        if (rowString.equals("")) {
                            rowString += "\"" + cell.getText() + "\"";
                            //if this is the first entry in this row, add a "
                        } else {
                            rowString += ",\"" + cell.getText() + "\"";
                            //otherwise put a comma before the entry, and add
                            // it to the rowString
                        }
                        if (!rowString.equals("\n"))
                            results.getLast().set(results.getLast().size() - 1, rowString);
                        //if the rowString is not just a newline, update the entry
                        //in the Linked List of rows
                    }
                }
            }
        }
        return results;
    }
}
