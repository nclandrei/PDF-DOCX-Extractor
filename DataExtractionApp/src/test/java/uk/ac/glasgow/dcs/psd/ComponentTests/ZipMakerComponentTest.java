package uk.ac.glasgow.dcs.psd.ComponentTests;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;


import uk.ac.glasgow.dcs.psd.Components.ZipMakerComponent;
import uk.ac.glasgow.dcs.psd.UtilityClasses.ZipMakerUtilities;

import java.io.File;
import java.io.IOException;

/**
 * Test class for the ZipMaker component that zips images, tables (csv files) and README
 */
public class ZipMakerComponentTest {

    private String pathToZipFolder;
    private String pathToZipFile;
    private String pathToZipContents;
    private ZipMakerUtilities helper;

    @Before
    public void setUp () {
        pathToZipFile = "src/test/java/uk/ac/glasgow/dcs/psd/Resources/zipMakerFolder.zip";
        pathToZipFolder = "src/test/java/uk/ac/glasgow/dcs/psd/Resources/zipMakerFolder";
        pathToZipContents = "src/test/java/uk/ac/glasgow/dcs/psd/Resources/zipMakerFolder/zipContents";
        helper = new ZipMakerUtilities();
    }

    /**
     * Tests if there is an actual zip with files inside the folder
     */
    @Test
    public void testCreateZip () {
        ZipMakerComponent.createZip(pathToZipFolder);
        assertTrue(new File(pathToZipFile).exists());
    }

    /**
     * Checks if you unzip the file created by ZipMakerComponent,
     * you get the correct structure and files
     * @throws IOException
     */
    @Test
    public void testZipContents () throws IOException {
        ZipMakerComponent.createZip(pathToZipFolder);
        boolean assertion = false;

        helper.unzip(pathToZipFile, pathToZipContents);

        assertion |= (new File (pathToZipContents + "/csv")).exists();
        assertion |= (new File (pathToZipContents + "/images")).exists();
        assertion |= (new File (pathToZipContents + "/README.txt")).exists();

        assertTrue(assertion);
    }

    /**
     * Checks if no exception is thrown when name of zipped folder is null
     */
    @Test
    public void testCreateNullZip () {
        ZipMakerComponent.createZip(null);
        assertFalse(new File(pathToZipFile).exists());
    }

    /**
     *
     * @throws IOException
     */
    @After
    public void tearDown () throws IOException {
        (new File(pathToZipFile)).delete();
        (new File("some_inexistent_name")).delete();
        helper.deleteFiles(new File(pathToZipContents));
    }
}