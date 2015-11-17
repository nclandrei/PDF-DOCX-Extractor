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
            if (!token.equals("-")) {
                try {
                    Integer.parseInt(token);
                    parsedRow += token + " ";
                    parsedTable.set(index,parsedRow);
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
                        parsedTable.set(index,parsedRow);
                        //System.out.print(token + " ");
                    }
                }
            } else {
                parsedRow += token + " ";
                parsedTable.set(index,parsedRow);
                //System.out.printf(" - ");
            }
        }
        return parsedTable;
    }

    public static void isNumTableType(String text){
        //Used to find the type of table, Numerical, or Text
        //TODO
    }

    public static String getHeading(String text){



        return "";
    }

}
