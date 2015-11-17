import java.util.Scanner;

/**
 * Created by richy734 on 11/11/15.
 */
public final class Util {

    private Util(){
    }

    public static void numberTable(String text) {

        Scanner lineScan = new Scanner(text);
        boolean newLine = false;

        while (lineScan.hasNext()) {
            String token = lineScan.next();
            if (!token.equals("-")) {
                try {
                    Integer.parseInt(token);
                    System.out.printf(" %s ", token);
                    newLine = true;
                } catch (NumberFormatException e) {
                    if (newLine) {
                        System.out.print("\n" + token + " ");
                        newLine = false;
                    } else {
                        System.out.print(token + " ");
                    }
                }
            } else {
                System.out.printf(" - ");
            }
        }
    }

    public static void isNumTableType(String text){
        //Used to find the type of table, Numerical, or Text
        //TODO
    }

    public static String getHeading(String text){



        return "";
    }

}
