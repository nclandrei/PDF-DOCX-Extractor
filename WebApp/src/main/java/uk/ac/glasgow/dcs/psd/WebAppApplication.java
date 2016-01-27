package uk.ac.glasgow.dcs.psd;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

@Configuration
@ComponentScan
@EnableAutoConfiguration
@Controller
public class WebAppApplication {

    @Value("${doChecksum}")
    private boolean doChecksum;

    @Value("${uploadToDropbox}")
    private boolean uploadToDropbox;

    @RequestMapping("/")
    String home() { return "/index.html"; }

    @RequestMapping(value = "/file/{fileID}", method = RequestMethod.GET)
    public void getFile(
            @PathVariable("fileID") String fileName,
            HttpServletResponse response) throws IOException {

        String src = getFileLocation(fileName+".csv");

        File file = new File(src);
        InputStream is = new FileInputStream(file);

        // MIME type of the file
        response.setContentType("application/octet-stream");
        // Response header
        response.setHeader("Content-Disposition", "attachment; filename=\""
                + file.getName() + "\"");
        // Read from the file and write into the response
        OutputStream os = response.getOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while ((len = is.read(buffer)) != -1) {
            os.write(buffer, 0, len);
        }
        os.flush();
        os.close();
        is.close();

        if(!doChecksum) //noinspection ResultOfMethodCallIgnored
            file.delete();
    }

    @RequestMapping(value="/uploadFile", method=RequestMethod.POST)
    @ResponseBody
    public String handleFileUpload(@RequestParam("file") MultipartFile file){
        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                String fileName = file.getOriginalFilename();
                BufferedOutputStream stream =
                        new BufferedOutputStream(
                                new FileOutputStream(new File(getFileLocation(fileName))));

                stream.write(bytes);
                stream.close();

                String extension = fileName.substring(fileName.lastIndexOf("."), fileName.length());
                String fileWithoutExtension = getFileLocation(fileName.substring(0, fileName.lastIndexOf(".")));

                File originalFile = new File(getFileLocation(fileName));

                if (doChecksum) {
                    String existingFile = ChecksumController.getChecksum(fileName,originalFile,uploadToDropbox);
                    if (existingFile != null)
                        return existingFile;
                }

                if(extension.compareTo(".docx") == 0) {
                    ExtractDocx.extractTables(getFileLocation(fileName), fileWithoutExtension);
                }

                if(extension.compareTo(".pdf") == 0){
                    PDFTableExtraction.process(fileWithoutExtension);
                }

                //delete the original uploaded file
                //noinspection ResultOfMethodCallIgnored
                originalFile.delete();

                return "/file/" + fileName.substring(0,fileName.lastIndexOf("."));
            } catch (Exception e) {
                return "You failed to upload" + e.getMessage();
            }
        } else {
            return "You failed to upload because the file was empty.";
        }
    }

    @RequestMapping(value="/uploadFileDropbox", method=RequestMethod.POST)
    @ResponseBody
    public String handleFileUploadDropbox(@RequestParam("file") String file,
                                          @RequestParam("fileName") String fileName) throws IOException {
        URL website = new URL(file);
        ReadableByteChannel rbc = Channels.newChannel(website.openStream());
        String inputFileName = getFileLocation(fileName);
        FileOutputStream fos = new FileOutputStream(inputFileName);
        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        fos.close();

        String extension = fileName.substring(fileName.lastIndexOf("."), fileName.length());
        String fileWithoutExtension = getFileLocation(fileName.substring(0, fileName.lastIndexOf(".")));

        File originalFile = new File(inputFileName);
        if (doChecksum) {
            String existingFile = ChecksumController.getChecksum(fileName,originalFile,uploadToDropbox);
            if (existingFile != null)
                return existingFile;
        }

        if(extension.compareTo(".docx") == 0) {
            ExtractDocx.extractTables(getFileLocation(fileName), fileWithoutExtension);
        }

        if(extension.compareTo(".pdf") == 0){
            PDFTableExtraction.process(fileWithoutExtension);
        }

        //noinspection ResultOfMethodCallIgnored
        originalFile.delete();

        return "/file/" + fileName.substring(0, fileName.lastIndexOf("."));
    }

    private String getFileLocation(String fileName) {
        String separator = System.getProperty("file.separator");
        return (System.getProperty("user.dir") + separator) +
                String.format("src%smain%sresources%sstatic%suploads%s%s",
                        separator, separator, separator, separator, separator,fileName);
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(WebAppApplication.class, args);
    }

}