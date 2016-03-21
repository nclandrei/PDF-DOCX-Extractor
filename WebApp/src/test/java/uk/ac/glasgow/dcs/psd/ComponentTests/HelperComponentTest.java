package uk.ac.glasgow.dcs.psd.ComponentTests;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import uk.ac.glasgow.dcs.psd.Components.HelperComponent;

import java.io.File;
import java.io.IOException;

/**
 * This class tests the functionality of the HelperComponent class
 */
public class HelperComponentTest {

    String fileName;
    String emptyDirName;
    String nonEmptyDirName;
    String fileInDirName;

    @Before
    public void setUp() {
        String resourcesDir = System.getProperty("user.dir");
        resourcesDir += "/src/test/java/uk/ac/glasgow/dcs/psd/Resources/";
        fileName = resourcesDir + "testDeleteFile.txt";
        emptyDirName = resourcesDir + "testDeleteEmptyFolder";
        nonEmptyDirName = resourcesDir + "testNonEmptyDeleteFolder";
        fileInDirName = resourcesDir + "testNonEmptyDeleteFolder/file.txt";
    }

    @After
    public void tearDown() {
        File file = new File(fileName);
        try {
            if (!file.exists())
                file.createNewFile();
        } catch (IOException e) {
            System.out.printf("ERROR: Could not create file: %s\n", fileName);
            e.printStackTrace();
        }

        file = new File(emptyDirName);
        if (!file.exists()) {
            if (!file.mkdir())
                System.out.printf("ERROR: Could not create directory: %s\n", emptyDirName);
        }

        file = new File(nonEmptyDirName);
        if (!file.exists()) {
            if (!file.mkdir())
                System.out.printf("ERROR: Could not create directory: %s\n", nonEmptyDirName);
            file = new File(fileInDirName);
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.out.printf("ERROR: Could not create file: %s\n", fileInDirName);
                e.printStackTrace();
            }
        }
    }

    /**
     * Test a simple deletion of a regular file
     */
    @Test
    public void deleteFileTest() {
        File deleteFile = new File(fileName);
        try {
            HelperComponent.delete(deleteFile);
        } catch (IOException e) {
            System.out.printf("ERROR: Failed to delete %s\n", fileName);
            e.printStackTrace();
        }
        assertFalse(deleteFile.exists());
    }

    /**
     * Test the deletion of an empty folder
     */
    @Test
    public void deleteEmptyDirectoryTest() {
        File deleteFile = new File(emptyDirName);
        try {
            HelperComponent.delete(deleteFile);
        } catch (IOException e) {
            System.out.printf("ERROR: Failed to delete %s\n", emptyDirName);
            e.printStackTrace();
        }
        assertFalse(deleteFile.exists());
    }

    /**
     * Test the deletion of a folder with
     * other files inside of it
     */
    @Test
    public void deleteNonEmptyDirectoryTest() {
        File deleteFile = new File(nonEmptyDirName);
        try {
            HelperComponent.delete(deleteFile);
        } catch (IOException e) {
            System.out.printf("ERROR: Failed to delete %s\n", nonEmptyDirName);
            e.printStackTrace();
        }
        assertFalse(deleteFile.exists());
    }

    /**
     * Test the deletion of a single file inside of a dir
     */
    @Test
    public void deleteFileInDirTest() {
        File deleteFile = new File(fileInDirName);
        try {
            HelperComponent.delete(deleteFile);
        } catch (IOException e) {
            System.out.printf("ERROR: Failed to delete %s\n", fileInDirName);
            e.printStackTrace();
        }
        assertFalse(deleteFile.exists());
    }

    /**
     * Test that the string returned by getFileLocation
     * is as expected
     */
    @Test
    public void getFileLocationTest() {
        String testFileName = ".git plzzz";
        String actualLocation = System.getProperty("user.dir");
        String seperator = System.getProperty("file.separator");
        actualLocation += String.format("%ssrc%smain%sresources%sstatic%suploads%s.git plzzz", seperator, seperator, seperator, seperator, seperator, seperator);

        assertEquals(actualLocation, HelperComponent.getFileLocation(testFileName));

    }

    /**
     * Test that if the same filename is passed to
     * the RandomizeFilename function twice the
     * results will be different
     */
    @Test
    public void RandomizeFilenameTest() {
        String filename1 = "test";
        String filename2 = filename1;

        filename1 = HelperComponent.RandomizeFilename(filename1);
        filename2 = HelperComponent.RandomizeFilename(filename2);

        assertNotEquals(filename1,filename2);
    }

}
