package uk.ac.glasgow.dcs.psd.ComponentTests;

import org.junit.Before;
import org.junit.Test;
import uk.ac.glasgow.dcs.psd.Components.DropboxComponent;
import uk.ac.glasgow.dcs.psd.Models.UploadZip;
import java.io.File;
import static org.junit.Assert.assertTrue;

/**
 * Test cases for Dropbox functionality
 */
public class DropboxComponentTest {

    private File toUpload;

    /**
     * Initializing file to be uploaded/downloaded from Dropbox
     */
    @Before
    public void setUp () {
        toUpload = new File (System.getProperty("user.dir") + "/src/test/java/uk/ac/glasgow/dcs/psd/Resources/test.docx");
    }

    /**
     * Tests if a file can be uploaded to Dropbox
     */
    @Test
    public void testDropboxUpload () {
        String metadata = DropboxComponent.dropboxUpload(toUpload,
                        System.getProperty("user.dir") + "/src/test/java/uk/ac/glasgow/dcs/psd/Resources/dropbox.auth",
                        "test.docx", "/Apps/CrichtonInstituteExtractionTool/");
        assertTrue(metadata != null);
    }

    /**
     * Tests if a file can be downloaded from Dropbox
     */
    @Test
    public void testDropboxDownload () {
        UploadZip zip = DropboxComponent.dropboxDownload("test.docx",
                System.getProperty("user.dir") + "/src/test/java/uk/ac/glasgow/dcs/psd/Resources/dropbox.auth",
                "/Apps/CrichtonInstituteExtractionTool/");
        assertTrue(zip != null);
    }
}
