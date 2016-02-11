package uk.ac.glasgow.dcs.psd.ComponentTests;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

public class ExtractDocxComponentTests {

    String fileName;

    @Before
    public void setUp(){
        fileName = "TestResources/test.docx";
    }


    @After
    public void tearDown(){}


    @Test
    public void getTablesTest(){

    }
}
