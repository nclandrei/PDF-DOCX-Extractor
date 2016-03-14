package uk.ac.glasgow.dcs.psd.ComponentTests;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import uk.ac.glasgow.dcs.psd.Components.HelperComponent;
import uk.ac.glasgow.dcs.psd.Components.ExtractDocxComponent;
import java.io.*;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ExtractDocxComponentTests {

    String fileName;
    String output;
    String testCsv;
    String directory;

    @Before
    public void setUp(){

        directory = System.getProperty("user.dir");
        directory += "/src/test/java/uk/ac/glasgow/dcs/psd";
        fileName = directory + "/Resources/test.docx";
        output = directory + "/Resources/test";
        testCsv = directory + "/Resources/test.csv";
    }


    @After
    public void tearDown(){
        try {
            HelperComponent.delete(new File(output+".zip"));
        }catch (IOException e){
            e.printStackTrace();
        }
    }


    @Test
    public void extractTablesTest(){
        BufferedReader testReader = null;
        BufferedReader zipReader = null;

        try {
            testReader = new BufferedReader(new FileReader(testCsv));
        }catch (FileNotFoundException e){
            System.out.printf("test.csv file was not found\n");
            e.printStackTrace();
            assertTrue(false);
        }

        ExtractDocxComponent.extractTablesAndImages(fileName, output);
        try {
            ZipFile zip = new ZipFile(output+".zip");
            Enumeration<? extends ZipEntry> entries = zip.entries();
            ZipEntry entry;
            while(entries.hasMoreElements()){
                entry = entries.nextElement();
                if (entry.getName().endsWith(".csv")) {
                    zipReader = new BufferedReader(new InputStreamReader(zip.getInputStream(entry)));
                    String testLine;
                    while((testLine = testReader.readLine()) != null) {
                        String zipLine = zipReader.readLine();
                        assertEquals(testLine, zipLine);
                    }
                }
            }
            zipReader.close();
            testReader.close();

        }catch (IOException e){
            System.out.printf("Error opening zip file %s\n", output);
            e.printStackTrace();
            assertTrue(false);
        }


    }


    @Test
    public void extractTablesInputFileDoesNotExistTest(){
        ExtractDocxComponent.extractTablesAndImages("does/not/exist.docx", output);
        assertFalse((new File(output)).exists());
    }


    @Test
    public void extractTableAndImageTest() {
        ExtractDocxComponent.extractTablesAndImages(directory+"/Resources/testImage.docx", output);
        int imageCount = 0;
        try {
            ZipFile zip = new ZipFile(output + ".zip");
            Enumeration<? extends ZipEntry> entries = zip.entries();
            ZipEntry entry;
            while (entries.hasMoreElements()) {
                entry = entries.nextElement();
                if (entry.getName().endsWith(".png") || entry.getName().endsWith(".jpg")) {
                    imageCount++;
                }
            }
        } catch (IOException e){
            e.printStackTrace();
        }
        // Magic number, yes it is horrible, but the testImage.docx contains 53 images.
        // 52 are .png 1 is .jpg
        assertTrue(imageCount==53);
    }
}
