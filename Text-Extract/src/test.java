import java.io.IOException;
import java.util.Scanner;

class test{
    public static void main(String[] args) throws IOException {

        PDFManager pdfManager = new PDFManager();
        pdfManager.setFilePath("res/sample.pdf");
        //StringBuilder sb = new  StringBuilder();

        boolean important = false;

        String text = pdfManager.ToText();


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

                if (important) {
                    System.out.print(token + " ");
                }
            }
            System.out.print("\n");

        }
    }
}