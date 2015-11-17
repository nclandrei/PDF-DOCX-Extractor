/**
 * Created by richy734 on 11/11/15.
 */
import sun.security.provider.certpath.AdjacencyList;

import java.io.*;
import java.util.LinkedList;
import java.util.Scanner;

class test2{

    public static void main(String[] args) throws IOException {

        PDFManager pdfManager = new PDFManager();
        pdfManager.setFilePath("res/sample.pdf");
        //StringBuilder sb = new  StringBuilder();

        boolean important = false;

        String text = pdfManager.ToText();

        String tableText = "";
        Scanner scan = new Scanner(text);
        Scanner lineScan;
        while(scan.hasNext()) {
            lineScan = new Scanner(scan.nextLine());
            while (lineScan.hasNext()) {
                String token = lineScan.next();

                //a reference to the figure can be made before or after it is encountered
                //this finds the actual figure
                if (token.equals("Figure")) {
                    token = lineScan.next();
                    if (token.charAt(token.length() - 1) == ':') {
                        important = true;
                    }
                }
                if (token.equals("Source:")) {
                    important = false;
                }

                if(important){
                    tableText+=token + " ";
                }

            }
        }
        LinkedList<String> parsedTable = Util.numberTable(tableText);
        for(String s: parsedTable){
            System.out.println(s);
        }
    }
}
