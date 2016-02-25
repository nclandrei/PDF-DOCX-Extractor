package uk.ac.glasgow.dcs.psd.ComponentTests;

import junit.framework.TestCase;
import org.junit.*;

import static org.junit.Assert.*;
import uk.ac.glasgow.dcs.psd.Components.ChecksumComponent;
import uk.ac.glasgow.dcs.psd.Components.HelperComponent;

import java.io.*;
import java.util.Date;

public class ChecksumComponentTest {

    String directory;
    String checksumsFileName;
    String testFile;
    String test1Check = "da39a3ee5e6b4b0d3255bfef95601890afd80709";
    String test2Check = "109f4b3c50d7b0df729d299bc6f8e9ef9066971f";
    String test3Check = "3ebfa301dc59196f18593c45e519287a23297589";


    @BeforeClass
    public static void beforeClass(){
        String checksumsFileName = "checksums.txt";
        File oldFile = new File(checksumsFileName);
        File newFile = new File(checksumsFileName+"-saved");
        System.out.printf("\nMoving checksums.txt file away\n");

        oldFile.renameTo(newFile);
    }

    @AfterClass
    public static void afterClass(){
        System.out.printf("\nMoving checksums.txt file back\n");
        String checksumsFileName = "checksums.txt";
        File oldFile = new File(checksumsFileName);
        File newFile = new File(checksumsFileName+"-saved");

        newFile.renameTo(oldFile);
    }

    @Before
    public void setUp(){
        directory = System.getProperty("user.dir");
        checksumsFileName = "checksums.txt";
        testFile = directory + "/src/test/java/uk/ac/glasgow/dcs/psd/Resources/testChecksum";
        File checksumsFile = new File(checksumsFileName);
        if(!checksumsFile.exists()){
            try {
                checksumsFile.createNewFile();
            }catch (IOException e){
                e.printStackTrace();
                System.out.printf("ERROR: Could not create file %s\n", checksumsFileName);
            }
        }
    }


    @After
    public void tearDown(){
        directory = System.getProperty("user.dir");
        checksumsFileName = "checksums.txt";
        File file = new File(checksumsFileName);

        if(file.exists())
            file.delete();

    }


    @Test
    public void getChecksumTest(){
        try {
            ChecksumComponent.getChecksum(testFile, new File(testFile+".txt"), false, false);
        }catch(IOException e){
            e.printStackTrace();
            System.out.printf("ERROR: File %s not found\n", testFile);
        }

        File checksumsFile = new File(checksumsFileName);
        assertTrue(checksumsFile.exists());

        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File(checksumsFileName)));
            String line = reader.readLine();
            assertTrue(line.contains("da39a3ee5e6b4b0d3255bfef95601890afd80709"));
            assertTrue(line.contains(testFile));

        }catch(FileNotFoundException e){
            e.printStackTrace();
            System.out.printf("ERROR: File %s not found\n", checksumsFileName);
            assert(false);
        }catch (IOException e){
            e.printStackTrace();
            System.out.printf("ERROR: Could not read line from %s\n", checksumsFileName);
        }

    }


    @Test
    public void multipleAddChecksumTest(){
        try {
            ChecksumComponent.getChecksum(testFile+".txt", new File(testFile+".txt"), false, false);
            ChecksumComponent.getChecksum(testFile+"2.txt", new File(testFile+"2.txt"), false, false);
            ChecksumComponent.getChecksum(testFile+"3.txt", new File(testFile+"3.txt"), false, false);
        }catch(IOException e){
            e.printStackTrace();
            System.out.printf("ERROR: File %s not found\n", testFile);
        }

        File checksumsFile = new File(checksumsFileName);
        assertTrue(checksumsFile.exists());

        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File(checksumsFileName)));
            String line = reader.readLine();
            while(line!=null) {
                assertTrue(line.contains(test1Check) || line.contains(test2Check) || line.contains(test3Check));
                assertTrue(line.contains(testFile + ".txt") || line.contains(testFile + "2.txt") ||
                        line.contains(testFile + "3.txt"));
                line = reader.readLine();
            }

        }catch(FileNotFoundException e){
            e.printStackTrace();
            System.out.printf("ERROR: File %s not found\n", checksumsFileName);
            assert(false);
        }catch (IOException e){
            e.printStackTrace();
            System.out.printf("ERROR: Could not read line from %s\n", checksumsFileName);
        }

    }
}
