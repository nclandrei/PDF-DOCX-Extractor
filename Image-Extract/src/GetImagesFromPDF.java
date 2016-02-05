import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDXObjectImage;

import java.io.File;
import java.util.List;
import java.util.Map;

@SuppressWarnings({ "unchecked", "rawtypes", "deprecation" })
public class GetImagesFromPDF {
    public static void main(String[] args) {
        try {
            String sourceDir = "./sample.pdf";// Paste pdf files in PDFCopy folder to read
            String destinationDir = ".";
            File oldFile = new File(sourceDir);
            if (oldFile.exists()) {
                PDDocument document = PDDocument.load(sourceDir);

                List<PDPage> list = document.getDocumentCatalog().getAllPages();

                String fileName = "image";
                int totalImages = 1;
                for (PDPage page : list) {
                    PDResources pdResources = page.getResources();

                    Map pageImages = pdResources.getImages();
                    if (pageImages != null) {

                        for (Object o : pageImages.keySet()) {
                            String key = (String) o;
                            PDXObjectImage pdxObjectImage = (PDXObjectImage) pageImages.get(key);
                            pdxObjectImage.write2file(fileName + totalImages);
                            totalImages++;
                        }
                    }
                }
            } else {
                System.err.println("File not exists");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
