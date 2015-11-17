import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * Created by richy734 on 11/11/15.
 */
public final class Util {

    private Util(){
    }

    public static LinkedList<String> numberTable(String text) {

        Scanner lineScan = new Scanner(text);
        boolean newLine = false;

        LinkedList<String> parsedTable = new LinkedList<>();
        int index = 0;

        String parsedRow = "";

        parsedTable.add(parsedRow);
        while (lineScan.hasNext()) {
            String token = lineScan.next();
            parsedRow = parsedTable.getLast();
            if (!token.equals("-") && !token.equals("and") && !token.equals("&")) {
                try {
                    Integer.parseInt(token);
                    parsedRow += token + " ";
                    parsedTable.set(index, parsedRow);
                    //System.out.printf(" %s ", token);
                    newLine = true;
                } catch (NumberFormatException e) {
                    if (newLine) {
                        index++;
                        parsedRow = (token + " ");
                        parsedTable.addLast(parsedRow);
                        //System.out.print("\n" + token + " ");
                        newLine = false;
                    } else {
                        parsedRow += token + " ";
                        parsedTable.set(index, parsedRow);
                        //System.out.print(token + " ");
                    }
                }
            }else if(token.equals("and") || token.equals("&")){
                parsedRow = parsedRow.substring(0, parsedRow.length()-1);
                parsedRow += "&";
                parsedTable.set(index,parsedRow);
            }else {
                parsedRow += token + " ";
                parsedTable.set(index,parsedRow);
                //System.out.printf(" - ");
            }
        }
        return getNumberHeading(parsedTable);
    }

    public static void isNumTableType(String text){
        //Used to find the type of table, Numerical, or Text
        //TODO
    }

    public static LinkedList<String> getNumberHeading(LinkedList<String> parsedTable){

        String lastEntry = parsedTable.getLast();
        int counter = lastEntry.split(" ").length;
        String firstEntry = parsedTable.getFirst();
        String header[] = firstEntry.split(" ");
        String firstRow = "";
        int length = header.length;
        for(int i=counter; i>1; i--){
            firstRow+=header[length - (i)] + " ";
        }

        String newFirstEntry = firstEntry.substring(0, firstEntry.length() - (3 + firstRow.length()));
        /** 3 is the magic number. */
        parsedTable.set(0, newFirstEntry);
        parsedTable.add(1, firstRow);

        return parsedTable;
    }

}
