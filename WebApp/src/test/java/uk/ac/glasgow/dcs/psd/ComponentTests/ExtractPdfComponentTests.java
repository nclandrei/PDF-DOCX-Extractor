package uk.ac.glasgow.dcs.psd.ComponentTests;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import uk.ac.glasgow.dcs.psd.Components.ExtractPdfComponent;
import uk.ac.glasgow.dcs.psd.Components.HelperComponent;

import java.io.*;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;


public class ExtractPdfComponentTests {

    Class pdfExtract;
    String directory;
    String jsonFileWithoutExtension;
    private PrintStream originalOut;
    private ByteArrayOutputStream collectedOut;


    /**
     * <h1>Compare the contents of two files</h1>
     *
     * @param file1 input file 1
     * @param file2 input file 2
     * @return true if the files have the same content, false otherwise
     */
    private boolean compareFiles(String file1, String file2) {
        String resultString;
        String oracleString;
        boolean comparison = true;
        try (BufferedReader resultReader = new BufferedReader(new FileReader(file1));
             BufferedReader oracleReader = new BufferedReader(new FileReader(file2))) {

            while ((resultString = resultReader.readLine()) != null && (oracleString = oracleReader.readLine()) != null) {
                if (!resultString.equals(oracleString)) {
                    comparison = false;
                    break;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return comparison;
    }

    @Before
    public void setUp() {
        //disable System.out to prevent tabula component from doing debug printing
        originalOut = System.out;
        collectedOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(collectedOut));

        pdfExtract = ExtractPdfComponent.class;
        directory = System.getProperty("user.dir");
        directory += "/src/test/java/uk/ac/glasgow/dcs/psd";
        jsonFileWithoutExtension = directory + "/Resources/sample7";
    }


    @After
    public void tearDown() {
        System.setOut(originalOut);
    }

    /**
     * <h1>Tests the operation of processJSON</h1>
     * <p>
     * This function uses a sample json to generate a folder with the extracted table
     * and compare that against a pre-made table.
     */
    @Test
    public void processJSONTest() {

        File workFolder = new File(directory + "/ResourcesTests/");
        workFolder.mkdir();

        String newJsonLoc = directory + "/ResourcesTests/sample7";
        File sourceJson = new File(jsonFileWithoutExtension + ".json");
        File copyJson = new File(directory + "/ResourcesTests/sample7.json");

        try {
            //copy the original json because it gets deleted
            Files.copy(sourceJson.toPath(), copyJson.toPath());

            Class[] cArg = new Class[1];
            cArg[0] = String.class;
            Method processJSON = pdfExtract.getDeclaredMethod("processJSON", cArg);
            processJSON.setAccessible(true);
            processJSON.invoke(null, newJsonLoc);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        boolean comparison = compareFiles(newJsonLoc + "/table0.csv", directory + "/Resources/table0.csv");

        //delete the newly created files
        (new File(newJsonLoc + "/table0.csv")).delete();
        (new File(newJsonLoc)).delete();
        workFolder.delete();

        assertTrue(comparison && !copyJson.exists());

    }

    /**
     * <h1>Tests the operation of generateJSON</h1>
     * <p>
     * This function uses a sample pdf to generate an extracted json
     * and compare that against a pre-made json.
     */
    @Test
    public void generateJSONTest() {
        try {
            Class[] cArg = new Class[1];
            cArg[0] = String.class;
            Method generateJSON = pdfExtract.getDeclaredMethod("generateJSON", cArg);
            generateJSON.setAccessible(true);
            generateJSON.invoke(null, jsonFileWithoutExtension + "0");
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        boolean comparison = compareFiles(jsonFileWithoutExtension + ".json", jsonFileWithoutExtension + "0.json");

        (new File(jsonFileWithoutExtension + "0.json")).delete();

        assertTrue(comparison);

    }

    /**
     * <h1>Tests the operation of main process functio</h1>
     * <p>
     * This function uses a sample pdf to generate a zip
     * and compare its contents against a pre-made csv which represents
     * the only table in the sample pdf.
     */
    @Test
    public void processTest() {
        String pdfWithoutExtension = directory + "/Resources/sample70";
        ExtractPdfComponent.process(pdfWithoutExtension);
        boolean success = true;

        BufferedReader testReader = null;
        BufferedReader zipReader = null;
        try {
            testReader = new BufferedReader(new FileReader(directory + "/Resources/table0.csv"));
        } catch (FileNotFoundException e) {
            System.err.printf("table0.csv file was not found\n");
            e.printStackTrace();
            assertTrue(false);
        }

        try {
            ZipFile zip = new ZipFile(pdfWithoutExtension + ".zip");
            Enumeration<? extends ZipEntry> entries = zip.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                if (entry.getName().endsWith(".csv")) {
                    zipReader = new BufferedReader(new InputStreamReader(zip.getInputStream(entry)));
                    String testLine;
                    while ((testLine = testReader.readLine()) != null) {
                        String zipLine = zipReader.readLine();
                        if (!testLine.equals(zipLine)) {
                            success = false;
                            break;
                        }
                    }
                }
            }
            zipReader.close();
            testReader.close();
            (new File(pdfWithoutExtension + ".zip")).delete();
        } catch (IOException e) {
            System.err.printf("Error opening zip file %s\n", pdfWithoutExtension);
            e.printStackTrace();
            success = false;
        }

        assertTrue(success);
    }

    /**
     * <h1>Tests the operation of extractImages</h1>
     * <p>
     * This function uses a sample pdf to extract three images
     * and then checks that they are all present.
     */
    @Test
    public void extractImagesTest() {

        String imageDir = directory + "/Resources/images";

        try {
            Class[] cArg = new Class[1];
            cArg[0] = String.class;
            Method extractImages = pdfExtract.getDeclaredMethod("extractImages", cArg);
            extractImages.setAccessible(true);
            extractImages.invoke(null, imageDir);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        File image1 = new File(imageDir + "/image1.png");
        File image2 = new File(imageDir + "/image2.png");
        File image3 = new File(imageDir + "/image3.jpg");

        boolean success = image1.exists() && image2.exists() && image3.exists();

        try {
            HelperComponent.delete(new File(imageDir));
        } catch (IOException e) {
            e.printStackTrace();
        }

        assertTrue(success);
    }

}
