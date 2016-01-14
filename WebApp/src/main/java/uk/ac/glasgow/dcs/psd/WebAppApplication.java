package uk.ac.glasgow.dcs.psd;


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
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Configuration
@ComponentScan
@EnableAutoConfiguration
@Controller
public class WebAppApplication {

    @RequestMapping("/")
    String home() {
        return "/index.html";
    }

    @RequestMapping("/response")
    @ResponseBody
    public Map<String,Object> randomResult() {
        Map<String,Object> model = new HashMap<>();
        model.put("id", UUID.randomUUID().toString());
        model.put("content", "Hello World!");
        return model;
    }

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

        file.delete();
    }

    @RequestMapping(value="/uploadFile", method=RequestMethod.POST)
    @ResponseBody
    public String handleFileUpload(@RequestParam("file") MultipartFile file){
        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                String filename = file.getOriginalFilename();
                filename = filename.replace(" ", "-");
                BufferedOutputStream stream =
                        new BufferedOutputStream(
                                new FileOutputStream(new File(getFileLocation(filename))));

                stream.write(bytes);
                stream.close();



                String extension = filename.substring(filename.lastIndexOf("."), filename.length());
                String fileWithoutExtension = getFileLocation(filename.substring(0, filename.lastIndexOf(".")));


                if(extension.compareTo(".docx") == 0) {
                    ExtractDocx.extractTables(getFileLocation(filename), fileWithoutExtension);
                }

                if(extension.compareTo(".pdf") == 0){
                    System.out.printf("FILE: %s\n", fileWithoutExtension);
                    PDFTableExtraction.process(fileWithoutExtension);
                }

                //delete the original uploaded file
                File originalFile = new File(getFileLocation(filename));
                originalFile.delete();


                return "/file/" + filename.substring(0,filename.lastIndexOf("."));
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
        fileName = fileName.replace(" ", "-");
        URL website = new URL(file);
        ReadableByteChannel rbc = Channels.newChannel(website.openStream());
        String inputFileName = getFileLocation(fileName);
        String outputFileName = inputFileName.substring(0,inputFileName.lastIndexOf("."));
        FileOutputStream fos = new FileOutputStream(inputFileName);
        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        fos.close();

        String extension = fileName.substring(fileName.lastIndexOf("."), fileName.length());
        String fileWithoutExtension = getFileLocation(fileName.substring(0, fileName.lastIndexOf(".")));

        if(extension.compareTo(".docx") == 0) {
            ExtractDocx.extractTables(getFileLocation(fileName), fileWithoutExtension);
        }

        if(extension.compareTo(".pdf") == 0){
            PDFTableExtraction.process(fileWithoutExtension);
        }

        File originalFile = new File(inputFileName);
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