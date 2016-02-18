package uk.ac.glasgow.dcs.psd.ComponentTests;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import uk.ac.glasgow.dcs.psd.Components.ExtractPdfComponent;

import java.io.*;
import java.lang.reflect.Method;
import java.nio.file.Files;


public class ExtractPdfComponentTests {

    Class pdfExtract;
    String directory;
    String jsonFileWithoutExtension;

    @Before
    public void setUp(){
        pdfExtract = ExtractPdfComponent.class;
        directory = System.getProperty("user.dir");
        directory += "/src/test/java/uk/ac/glasgow/dcs/psd";
        jsonFileWithoutExtension = directory + "/Resources/sample7";
    }


    @After
    public void tearDown(){}


    //uses a sample json to generate a folder with the extracted table
    //and compare that against a pre-made table
    @Test
    public void processJSONTest(){

        String newJsonLoc = directory + "/ResourcesTests/sample7";
        File sourceJson = new File(jsonFileWithoutExtension + ".json");
        File copyJson = new File(directory + "/ResourcesTests/sample7.json");

        try{
            //copy the original json because it gets deleted
            Files.copy(sourceJson.toPath(), copyJson.toPath());

            Class[] cArg = new Class[1];
            cArg[0] = String.class;
            Method processJSON = pdfExtract.getDeclaredMethod("processJSON", cArg);
            processJSON.setAccessible(true);
            processJSON.invoke(null, newJsonLoc);
        }
        catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        String resultString;
        String oracleString;
        boolean comparison = true;
        try(BufferedReader resultReader = new BufferedReader(new FileReader(newJsonLoc + "/table0.csv"));
            BufferedReader oracleReader = new BufferedReader(new FileReader(directory + "/Resources/table0.csv"))){

            while((resultString = resultReader.readLine()) != null && (oracleString = oracleReader.readLine()) != null){
                if(!resultString.equals(oracleString)){
                    comparison = false;
                    break;
                }
            }
        }
        catch(FileNotFoundException e){
            e.printStackTrace();
        }
        catch(IOException e){
            e.printStackTrace();
        }

        //delete the newly created files
        (new File(newJsonLoc + "/table0.csv")).delete();
        (new File(newJsonLoc)).delete();

        assertTrue(comparison && !copyJson.exists());

    }


}
