package uk.ac.glasgow.dcs.psd.ControllerTests;

        import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.PropertySource;
        import org.springframework.http.MediaType;
        import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import uk.ac.glasgow.dcs.psd.ApplicationConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

        import static org.hamcrest.Matchers.containsInAnyOrder;
        import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test case for HomeController
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ApplicationConfiguration.class)
@PropertySource("classpath:application.properties")
@WebAppConfiguration
public class UploadDownloadControllerTest {
    @Autowired
    WebApplicationContext wac;

    private MockMvc mockMvc;

    String publicResourcesTestZip;
    String publicResourcesSecondTestZip;

    @Before
    public void setup() {
        // for getFile()
        String directoryTest = System.getProperty("user.dir");
        String directoryMain = directoryTest;
        directoryTest += "/src/test/java/uk/ac/glasgow/dcs/psd";
        directoryMain += "/src/main/";
        String resourcesTestZip = directoryTest + "/Resources/testGetFile.zip";
        publicResourcesTestZip = directoryMain + "resources/static/uploads/test.zip";
        publicResourcesSecondTestZip =
                directoryMain + "resources/static/uploads/Zfbj7AHekx.zip";
        try {
            Files.copy(new File(resourcesTestZip).toPath(),
                    new File(publicResourcesTestZip).toPath(),
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @After
    public void tearDown(){
        boolean deleteTestZip = new File(publicResourcesTestZip).delete();
        boolean deleteSecondTestZip = new File(publicResourcesSecondTestZip).delete();
        if(!deleteTestZip || !deleteSecondTestZip) {
            System.out.println("Failed to delete test zips");
        }
    }

    /**
     * Try to get test.zip file
     * with HTML code 200.
     * @throws Exception if an error occurs
     */
    @Test
    public void getFile() throws Exception {
        this.mockMvc.perform(get("/file/test")
                .accept("application/zip"))
                .andExpect(status().isOk());
    }

    /**
     * Try to get test.zip file
     * with HTML code 404.
     * @throws Exception if an error occurs
     */
    @Test
    public void getFileNotFound() throws Exception {
        this.mockMvc.perform(get("/tests")
                .accept("application/zip"))
                .andExpect(status().is4xxClientError());
    }

    /**
     * Test working link to a file
     */
    @Test
    public void handleFileUploadDropbox() {
        try {
            this.mockMvc.perform(post("/uploadFileDropbox")
                    .param("file","https://cldup.com/Zfbj7AHekx.docx")
                    .param("fileName","Zfbj7AHekx.docx"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.status").value(1));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Test not working link to a file
     */
    @Test
    public void handleFileUploadDropboxNotWorking() {
        try {
            this.mockMvc.perform(post("/uploadFileDropbox")
                    .param("file","Zfbj7AHekx.docx")
                    .param("fileName","Zfbj7AHekx.docx"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                    .andExpect(jsonPath("$.status").value(0));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
