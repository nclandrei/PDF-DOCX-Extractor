package uk.ac.glasgow.dcs.psd;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Controller
@EnableAutoConfiguration
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

    @RequestMapping(value="/uploadFile", method= RequestMethod.GET)
    String provideUploadInfo() {
        return "/index.html";
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

    }

    @RequestMapping(value="/uploadFile", method=RequestMethod.POST)
    public String handleFileUpload(@RequestParam("file") MultipartFile file){
        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                String filename = file.getOriginalFilename();
                BufferedOutputStream stream =
                        new BufferedOutputStream(
                                new FileOutputStream(new File(getFileLocation(filename))));
                stream.write(bytes);
                stream.close();
                String outputFileName = getFileLocation(filename.substring(0,filename.lastIndexOf(".")));
                ExtractDocx.extractTables(getFileLocation(filename), outputFileName);

                return "redirect:/file/"+filename.substring(0,filename.lastIndexOf("."));
            } catch (Exception e) {
                return "You failed to upload" + e.getMessage();
            }
        } else {
            return "You failed to upload because the file was empty.";
        }
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
