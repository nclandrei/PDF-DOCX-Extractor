package uk.ac.glasgow.dcs.psd.ComponentTests;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import uk.ac.glasgow.dcs.psd.Components.HelperComponent;

import java.io.File;
import java.io.IOException;

public class HelperComponentTest {

    String fileName;
    String emptyDirName;
    String nonEmptyDirName;
    String fileInDirName;

    @Before
    public void setUp(){
        String resourcesDir = System.getProperty("user.dir");
        resourcesDir += "/src/test/java/uk/ac/glasgow/dcs/psd/Resources/";
        fileName = resourcesDir + "testDeleteFile.txt";
        emptyDirName = resourcesDir + "testDeleteEmptyFolder";
        nonEmptyDirName = resourcesDir + "testNonEmptyDeleteFolder";
        fileInDirName = resourcesDir + "testNonEmptyDeleteFolder/file.txt";
    }

    @After
    public void tearDown(){
        File file = new File(fileName);
        try {
            if(!file.exists())
                file.createNewFile();
        }catch(IOException e){
            System.out.printf("ERROR: Could not create file: %s\n", fileName);
            e.printStackTrace();
        }

        file = new File(emptyDirName);
        if(!file.exists()) {
            if(!file.mkdir())
                System.out.printf("ERROR: Could not create directory: %s\n", emptyDirName);
        }

        file = new File(nonEmptyDirName);
        if(!file.exists()) {
            if(!file.mkdir())
                System.out.printf("ERROR: Could not create directory: %s\n", nonEmptyDirName);
            file = new File(fileInDirName);
            try{
                file.createNewFile();
            }catch(IOException e){
                System.out.printf("ERROR: Could not create file: %s\n", fileInDirName);
                e.printStackTrace();
            }
        }
    }

    @Test
    public void deleteFileTest(){
        File deleteFile = new File(fileName);
        try {
            HelperComponent.delete(deleteFile);
        }catch (IOException e){
            System.out.printf("ERROR: Failed to delete %s\n",fileName);
            e.printStackTrace();
        }
        assertFalse(deleteFile.exists());
    }

    @Test
    public void deleteEmptyDirectoryTest(){
        File deleteFile = new File(emptyDirName);
        try {
            HelperComponent.delete(deleteFile);
        }catch (IOException e){
            System.out.printf("ERROR: Failed to delete %s\n",emptyDirName);
            e.printStackTrace();
        }
        assertFalse(deleteFile.exists());
    }

    @Test
    public void deleteNonEmptyDirectoryTest(){
        File deleteFile = new File(nonEmptyDirName);
        try {
            HelperComponent.delete(deleteFile);
        }catch (IOException e){
            System.out.printf("ERROR: Failed to delete %s\n",nonEmptyDirName);
            e.printStackTrace();
        }
        assertFalse(deleteFile.exists());
    }


    @Test
    public void deleteFileInDirTest(){
        File deleteFile = new File(fileInDirName);
        try {
            HelperComponent.delete(deleteFile);
        }catch (IOException e){
            System.out.printf("ERROR: Failed to delete %s\n",fileInDirName);
            e.printStackTrace();
        }
        assertFalse(deleteFile.exists());
    }

    @Test
    public void getFileLocationTest(){
        String testFileName = ".git plzzz";
        String actualLocation = System.getProperty("user.dir");
        actualLocation += "/src/main/resources/static/uploads/.git plzzz";

        assertEquals(actualLocation, HelperComponent.getFileLocation(testFileName));

    }

}
