package uk.ac.glasgow.dcs.psd;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

import java.io.*;
import java.util.LinkedList;

public class ExtractDocx {
    public static void extractTables(String input, String output) {
        LinkedList<LinkedList<String>> tables = getTables(input);
        int counter = 0;
        File directory = new File(output);

        try {
            directory.mkdir();
            assert tables != null;
            for(LinkedList<String> table: tables){
                File outFile = new File(output + File.separator + counter + ".csv");
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

        }catch(Exception ignored){}

        ZipMaker.createZip(output);
        try{
            ZipMaker.delete(directory);
        }catch( Exception e){
            //pass
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
