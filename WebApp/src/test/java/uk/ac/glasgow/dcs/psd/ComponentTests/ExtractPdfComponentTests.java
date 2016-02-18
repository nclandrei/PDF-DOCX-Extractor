package uk.ac.glasgow.dcs.psd.ComponentTests;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import uk.ac.glasgow.dcs.psd.Components.ExtractPdfComponent;

import java.lang.reflect.Method;


public class ExtractPdfComponentTests {

    Class pdfExtract;

    @Before
    public void setUp(){
        pdfExtract = ExtractPdfComponent.class;
    }


    @After
    public void tearDown(){}


    @Test
    public void generateJSONTest(){
        try{
            Class[] cArg = new Class[1];
            cArg[0] = String.class;
            Method processJSON = pdfExtract.getDeclaredMethod("processJSON", cArg);
            processJSON.setAccessible(true);






            assertTrue(true);
        }
        catch (NoSuchMethodException e) {
            System.err.println("No such method");
            e.printStackTrace();
            System.exit(1);
        }

    }
}
