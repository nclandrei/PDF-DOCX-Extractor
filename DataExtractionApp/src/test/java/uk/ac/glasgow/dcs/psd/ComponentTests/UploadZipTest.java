package uk.ac.glasgow.dcs.psd.ComponentTests;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import uk.ac.glasgow.dcs.psd.Models.UploadZip;

import java.lang.reflect.Field;


/**
 * This class uses reflection tests
 * to validate the API of UploadZip
 * Ensuring that everything is correct before
 * sending it through Springs Jackson ObjectMapper.
 */
public class UploadZipTest {

    UploadZip uZip = null;

    @Before
    public void setUp() {
        uZip = new UploadZip(0, null, null, null);
    }

    @After
    public void tearDown() {
        uZip = null;
    }

    /**
     * Test that changing status works
     * correctly
     */
    @Test
    public void setStatusTest() {
        uZip.setStatus(200);

        try {
            Field f = uZip.getClass().getDeclaredField("status");
            f.setAccessible(true);
            int retrievedStatus = (int) f.get(uZip);
            assertTrue(retrievedStatus == 200);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * Test that setting filenames
     * works correctly
     */
    @Test
    public void setFilenamesTest() {
        uZip.setFilename("file");

        try {
            Field f = uZip.getClass().getDeclaredField("filename");
            f.setAccessible(true);
            String retrievedFilename = (String) f.get(uZip);
            assertTrue(retrievedFilename.equals("file"));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * Test that setting href link
     * works correctly
     */
    @Test
    public void setHrefTest() {
        uZip.setHref("/link/");

        try {
            Field f = uZip.getClass().getDeclaredField("href");
            f.setAccessible(true);
            String retrievedHref = (String) f.get(uZip);
            assertTrue(retrievedHref.equals("/link/"));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * Test that setting messages
     * works correctly
     */
    @Test
    public void setMessageTest() {
        uZip.setMessage("message");

        try {
            Field f = uZip.getClass().getDeclaredField("message");
            f.setAccessible(true);
            String retrievedMessage = (String) f.get(uZip);
            assertTrue(retrievedMessage.equals("message"));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * Test that getting status
     * works correctly
     */
    @Test
    public void getStatusTest() {
        uZip.setStatus(200);
        assertTrue(uZip.getStatus() == 200);
    }

    /**
     * Test that getting filenames
     * works correctly
     */
    @Test
    public void getFilenamesTest() {
        uZip.setFilename("file");
        assertTrue(uZip.getFilename().equals("file"));
    }

    /**
     * Test that getting href link
     * works correctly
     */
    @Test
    public void getHrefTest() {
        uZip.setHref("/link/");
        assertTrue(uZip.getHref().equals("/link/"));
    }

    /**
     * Test that getting message
     * works correctly
     */
    @Test
    public void getMessageTest() {
        uZip.setMessage("message");
        assertTrue(uZip.getMessage().equals("message"));
    }

}
