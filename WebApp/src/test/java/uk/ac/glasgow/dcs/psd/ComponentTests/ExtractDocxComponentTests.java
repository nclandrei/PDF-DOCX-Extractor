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

    @Before
    public void setUp(){

        String directory = System.getProperty("user.dir");
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
            assert(false);
        }

        ExtractDocxComponent.extractTablesAndImages(fileName, output);
        try {
            ZipFile zip = new ZipFile(output+".zip");
            Enumeration<? extends ZipEntry> entries = zip.entries();
            ZipEntry entry;
            while(entries.hasMoreElements()){
                entry = entries.nextElement();
                System.out.println(entry.getName());
                if (entry.getName().endsWith(".csv")) {
                    zipReader = new BufferedReader(new InputStreamReader(zip.getInputStream(entry)));
                    String testLine;
                    while((testLine = testReader.readLine()) != null) {
                        String zipLine = zipReader.readLine();
                        System.out.printf("Test: %s\n Zip:%s\n", testLine, zipLine);
                        assertEquals(testLine, zipLine);
                    }
                }
            }
            zipReader.close();
            testReader.close();

        }catch (IOException e){
            System.out.printf("Error opening zip file %s\n", output);
            e.printStackTrace();
            assert(false);
        }


    }
}
