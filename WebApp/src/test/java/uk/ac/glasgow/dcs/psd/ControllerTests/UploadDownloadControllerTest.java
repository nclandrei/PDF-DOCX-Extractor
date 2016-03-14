package uk.ac.glasgow.dcs.psd.ControllerTests;

        import org.junit.After;
        import org.junit.Before;
        import org.junit.Test;
        import org.junit.runner.RunWith;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.beans.factory.support.ReplaceOverride;
        import org.springframework.boot.test.SpringApplicationConfiguration;
        import org.springframework.context.annotation.PropertySource;
        import org.springframework.http.MediaType;
        import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
        import org.springframework.test.context.web.WebAppConfiguration;
        import org.springframework.test.web.servlet.MockMvc;
        import org.springframework.test.web.servlet.setup.MockMvcBuilders;
        import org.springframework.web.context.WebApplicationContext;
        import uk.ac.glasgow.dcs.psd.ApplicationConfiguration;
        import uk.ac.glasgow.dcs.psd.Components.HelperComponent;

        import java.io.File;
        import java.io.IOException;
        import java.nio.file.Files;
        import java.nio.file.StandardCopyOption;

        import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
        import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

    String publicResourcesTestZip = "";

    @Before
    public void setup() {
        // for getFile()
        String directoryTest = System.getProperty("user.dir");
        String directoryMain = directoryTest;
        directoryTest += "/src/test/java/uk/ac/glasgow/dcs/psd";
        directoryMain += "/src/main/";
        String resourcesTestZip = directoryTest + "/Resources/testGetFile.zip";
        publicResourcesTestZip += directoryMain + "resources/static/uploads/test.zip";
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
        // remove test.zip
        new File(publicResourcesTestZip).delete();
    }

    /**
     * Try to get test.zip file
     * with HTML code 200.
     * @throws Exception if an error occurs
     */
    @Test
    public void getFile() throws Exception {
        this.mockMvc.perform(get("/test")
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
}
