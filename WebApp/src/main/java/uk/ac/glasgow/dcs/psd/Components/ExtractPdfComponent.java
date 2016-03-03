package uk.ac.glasgow.dcs.psd.Components;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.ParseException;
import technology.tabula.CommandLineApp;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDXObjectImage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Component;
import java.io.FileWriter;
import java.io.File;
import java.io.FileReader;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import java.io.PrintStream;
import java.io.BufferedWriter;

/**
 * This class provides an API for extracting information from tables
 * into CSVs as well as extracting the images.
 * The CSVs and images are then placed into separate folders and a zip with
 * both of them is returned.
 */

@Component
public class ExtractPdfComponent {

    /**
     * <h1>Extract Tables and Images from a pdf</h1>
     * <p>
     * The process function combines the functionality of a number of helper functions
     * that make use of the Tabula jar to generate a JSON file and convert that into a CSV.
     * The Image extraction tool is then used to provide all of the images from the pdf.
     * The resulting CSVs and images are then placed into a zip using the ZipMaker utility
     * and the original files are deleted.
     *
     * @param fileName the file name for the pdf to extract the tables from (without the extension)
     */

    public static void process(String fileName) {
        generateJSON(fileName);
        processJSON(fileName);
        extractImages(fileName);
        ZipMakerComponent.createZip(fileName);
        try {
            HelperComponent.delete(new File(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * <h1>Generates a JSON with the Table data</h1>
     * <p>
     * generateJSON makes use of Tabula to generate a JSON file
     * with all the data from the tables in the PDF
     *
     * @param fileName the file name for the pdf to extract the tables from (without the extension)
     */

    private static void generateJSON(String fileName) {

        String pdfFile = fileName + ".pdf";
        String jsonFile = fileName + ".json";
        String[] tabulaArgs = {pdfFile, "-i", "-pall", "-r", "-f", "JSON"};

        GnuParser parser = new GnuParser();
        try {
            PrintStream ps = new PrintStream(new File(jsonFile));
            CommandLine exp = parser.parse(CommandLineApp.buildOptions(), tabulaArgs);
            if (exp.getArgs().length != 1) {
                throw new ParseException("Need one filename\nTry --help for help");
            }
            (new CommandLineApp(ps)).extractTables(exp);
            ps.flush();
            ps.close();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }

    }

    /**
     * <h1>Generates CSVs from a JSON</h1>
     * <p>
     * processJSON makes use of a json file to generate a folder
     * of CSVs where each csv holds the extracted information for one table
     *
     * @param fileName the file name of the JSON file used for processing (without the extension)
     */

    private static void processJSON(String fileName) {

        File outDir = new File(fileName);
        outDir.mkdir();

        JSONParser parser = new JSONParser();
        StringBuilder sb = new StringBuilder();

        try {
            FileReader fr = new FileReader(fileName + ".json");
            JSONArray array = (JSONArray) parser.parse(fr);
            int csvName = 0;

            for (Object obj : array) {
                JSONObject table = (JSONObject) obj;
                JSONArray cells = (JSONArray) table.get("data");

                for (Object c : cells) {
                    JSONArray cellProperties = (JSONArray) c;

                    for (Object p : cellProperties) {
                        JSONObject property = (JSONObject) p;
                        String text = (String) property.get("text");
                        if (text.compareTo(" ") == 0 || text.compareTo("") == 0 || text.length() > 100) {
                            break;
                        }
                        sb.append("\"").append(text).append("\",");
                    }

                    if (sb.toString().compareTo("") == 0) {
                        continue;
                    }
                    sb.append("\n");
                }

                if (!sb.toString().isEmpty()) {
                    try (BufferedWriter buffer = new BufferedWriter(new FileWriter(fileName +
                            File.separator + "table" + csvName + ".csv"))) {
                        sb.deleteCharAt(sb.length() - 1);
                        sb.deleteCharAt(sb.length() - 1);
                        buffer.write(sb.toString());
                        csvName++;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    sb = new StringBuilder();
                }
            }

            fr.close();
            //delete the used json
            HelperComponent.delete(new File(fileName + ".json"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * <h1>Extract images from a pdf document<h1/>
     * <p>
     * extractImages provides the images in a PDF as standalone files
     *
     * @param fileName the file name for the pdf to extract the tables from (without the extension)
     */

    private static void extractImages(String fileName) {
        try {
            String sourceDir = fileName;

            //safety for tabula function
            File outDir = new File(sourceDir);
            if (!outDir.exists()) {
                outDir.mkdir();
            }

            PDDocument document = PDDocument.load(sourceDir + ".pdf");
            List<PDPage> list = document.getDocumentCatalog().getAllPages();

            String imageName = "image";
            int totalImages = 1;
            for (PDPage page : list) {
                PDResources pdResources = page.getResources();

                Map pageImages = pdResources.getImages();
                if (pageImages != null) {

                    for (Object o : pageImages.keySet()) {
                        String key = (String) o;
                        PDXObjectImage pdxObjectImage = (PDXObjectImage) pageImages.get(key);
                        pdxObjectImage.write2file(fileName + File.separator + imageName + totalImages);
                        totalImages++;
                    }
                }
            }
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}