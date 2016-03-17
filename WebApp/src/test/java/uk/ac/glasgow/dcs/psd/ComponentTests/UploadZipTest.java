package uk.ac.glasgow.dcs.psd.ComponentTests;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import uk.ac.glasgow.dcs.psd.Models.UploadZip;

import java.lang.reflect.Field;

public class UploadZipTest {

    UploadZip uZip = null;

    @Before
    public void setUp() {
        uZip = new UploadZip(0, null, null, 0, null);
    }

    @After
    public void tearDown() {
        uZip = null;
    }

    @Test
    public void setStatusTest() {
        uZip.setStatus(200);

        try {
            Field f = uZip.getClass().getDeclaredField("status");
            f.setAccessible(true);
            int retrievedStatus = (int) f.get(uZip);
            assertTrue(retrievedStatus == 200);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void setFilenamesTest() {
        uZip.setFilename("file");

        try {
            Field f = uZip.getClass().getDeclaredField("filename");
            f.setAccessible(true);
            String retrievedFilename = (String) f.get(uZip);
            assertTrue(retrievedFilename.equals("file"));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void setHrefTest() {
        uZip.setHref("/link/");

        try {
            Field f = uZip.getClass().getDeclaredField("href");
            f.setAccessible(true);
            String retrievedHref = (String) f.get(uZip);
            assertTrue(retrievedHref.equals("/link/"));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void setMessageTest() {
        uZip.setMessage("message");

        try {
            Field f = uZip.getClass().getDeclaredField("message");
            f.setAccessible(true);
            String retrievedMessage = (String) f.get(uZip);
            assertTrue(retrievedMessage.equals("message"));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void setFileSizeTest() {
        uZip.setFileSize(10);

        try {
            Field f = uZip.getClass().getDeclaredField("fileSize");
            f.setAccessible(true);
            int retrievedFileSize = (int) f.get(uZip);
            assertTrue(retrievedFileSize == 10);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getStatusTest() {
        uZip.setStatus(200);
        assertTrue(uZip.getStatus() == 200);
    }


    @Test
    public void getFilenamesTest() {
        uZip.setFilename("file");
        assertTrue(uZip.getFilename().equals("file"));
    }

    @Test
    public void getHrefTest() {
        uZip.setHref("/link/");
        assertTrue(uZip.getHref().equals("/link/"));
    }

    @Test
    public void getMessageTest() {
        uZip.setMessage("message");
        assertTrue(uZip.getMessage().equals("message"));
    }

    @Test
    public void getFileSizeTest() {
        uZip.setFileSize(10);
        assertTrue(uZip.getFileSize() == 10);
    }

}
