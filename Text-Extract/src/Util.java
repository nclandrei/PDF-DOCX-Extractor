import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * Created by richy734 on 11/11/15.
 */

/**
 * This is just a utility class to extract a table from
 * a string, currently only handles number based tables
 * e.g.:
 *
 * given a string =  "header1 header2 header3 row1 1 2 3 row2 4 5 6 row3 7 8 9"
 *
 * It generates a linked list of strings representing a table of the form:
 *
 *          header1     header2     header3
 * row1        1          2            3
 * row2        4          5            6
 * row3        7          8            9
 *
 * with each string being a row of the table.
 * 
 */

public final class Util {

    private Util(){
    }

    /**
     * Takes a string of text, the transcript from a
     * table, and separates it into rows of a table,
     * calling getNumberHeading to separate the header
     * from the first row of the table.
     *
     * @param text
     * @return LinkedList<String>
     *
     */

    public static LinkedList<String> numberTable(String text) {

        Scanner lineScan = new Scanner(text);
        //creates a scanner for the given text

        boolean newLine = false;
        //if the next token should be on a new line

        LinkedList<String> parsedTable = new LinkedList<>();
        //the end result of parsing the table

        int index = 0;
        //the

        String parsedRow = "";

        parsedTable.add(parsedRow);
        while (lineScan.hasNext()) {
            String token = lineScan.next();
            parsedRow = parsedTable.getLast();
            if (!token.equals("-") && !token.equals("and") && !token.equals("&")) {
                try {
                    Double.parseDouble(token);
                    /** This is a horrible hack that relies on the fact that a row
                     * of numbers ends when the next row starts with a string */
                    parsedRow += token + " ";
                    parsedTable.set(index, parsedRow);
                    newLine = true;
                    //when there are no more numbers, then it must be a new row
                    //so their needs to be a new line
                } catch (NumberFormatException e) {
                    //if there is need for a new line
                    if (newLine) {
                        //increase the index
                        index++;
                        parsedRow = (token + " ");
                        parsedTable.addLast(parsedRow);
                        //insert the token at a new row
                        newLine = false;
                        //now there is no need for a new line
                    } else {
                        parsedRow += token + " ";
                        parsedTable.set(index, parsedRow);
                        //this is the case when the row heading has numerous words
                        //e.g. "a b c"  1 2 3 4 5
                    }
                }
            }else if(token.equals("and") || token.equals("&")){
                parsedRow = parsedRow.substring(0, parsedRow.length()-1);
                parsedRow += "&";
                //this makes splitting the header easier as it counts as just one
                //word when splitting on the space character
                parsedTable.set(index,parsedRow);
                //this sets the indexed element to this changed string
            }else {
                parsedRow += token + " ";
                //if the character is a '-', just add it
                parsedTable.set(index,parsedRow);
                //set the row to the updated string
            }
        }
        return getNumberHeading(parsedTable);
        //split the header from the first row, and return the finished table
    }

    public static void isNumTableType(String text){
        //Used to find the type of table, Numerical, or Text
        //TODO
    }

    /**
     * Takes a linked list of strings representing a table,
     * separates the heading of the table from the first row
     * of the table, ad returns the fixed table.
     *
     * @param  parsedTable
     * @return LinkedList<String>
     */

    public static LinkedList<String> getNumberHeading(LinkedList<String> parsedTable){

        String lastEntry = parsedTable.getLast();
        int index = 0;
        //where the new header should be added

        int counter = lastEntry.split(" ").length;
        /** yet another horrible hack, deciding how long a typical row is
         * relies on counting the spaces in the last row and then cuts the
         * header row by this amount, and takes the cut part and inserts it
         * as the first row of the table */

        String firstEntry = parsedTable.getFirst();
        String header[] = firstEntry.split(" ");
        String firstRow = "";
        int length = header.length;
        if(counter > length) {
            firstEntry = parsedTable.get(1);
            header = firstEntry.split(" ");
            length = header.length;
            index = 1;
        }
        for (int i = counter; i > 1; i--) {
            firstRow += header[length - (i)] + " ";
        }


        String newFirstEntry = firstEntry.substring(0, firstEntry.length() - (3 + firstRow.length()));
        /** 3 is the magic number. (for the first table anyway)
         * appears to be 5 for figure 3.4
         * requires further investigation*/
        parsedTable.set(index, newFirstEntry);
        parsedTable.add(index+1, firstRow);

        return parsedTable;
    }

}
