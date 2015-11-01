import java.io.IOException;

class test{
    public static void main(String[] args) throws IOException {

        PDFManager pdfManager = new PDFManager();
        pdfManager.setFilePath("res/test.pdf");
        System.out.println(pdfManager.ToText());

    }
}