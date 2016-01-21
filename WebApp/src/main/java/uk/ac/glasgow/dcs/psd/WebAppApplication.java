package uk.ac.glasgow.dcs.psd;


import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import com.google.common.io.Files;
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
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Date;

@Configuration
@ComponentScan
@EnableAutoConfiguration
@Controller
public class WebAppApplication {

    @RequestMapping("/")
    String home() {
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

//        file.delete();
    }

    @RequestMapping(value="/uploadFile", method=RequestMethod.POST)
    @ResponseBody
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

                String extension = filename.substring(filename.lastIndexOf("."), filename.length());
                String fileWithoutExtension = getFileLocation(filename.substring(0, filename.lastIndexOf(".")));

                File originalFile = new File(getFileLocation(filename));

                String sCurrentLine = getChecksum(filename, originalFile);

                if (sCurrentLine != null) return sCurrentLine;

                if(extension.compareTo(".docx") == 0) {
                    ExtractDocx.extractTables(getFileLocation(filename), fileWithoutExtension);
                }

                if(extension.compareTo(".pdf") == 0){
                    PDFTableExtraction.process(fileWithoutExtension);
                }

                //delete the original uploaded file
                originalFile.delete();


                return "/file/" + filename.substring(0,filename.lastIndexOf("."));
            } catch (Exception e) {
                return "You failed to upload" + e.getMessage();
            }
        } else {
            return "You failed to upload because the file was empty.";
        }
    }

    private String getChecksum(String filename, File originalFile) throws IOException {
        HashCode hc = Files.hash(originalFile, Hashing.sha1());

        return checkChecksum(filename, hc);
    }

    private String checkChecksum(String filename, HashCode hc) {
        try (BufferedReader br = new BufferedReader(new FileReader("checksums.txt")))
        {
            String sCurrentLine;
            while ((sCurrentLine = br.readLine()) != null) {
                if (sCurrentLine.contains(hc.toString())) {
                    return "/file/" + sCurrentLine.substring(sCurrentLine.indexOf("FileName:")+9,
                            sCurrentLine.indexOf(":FileName")).substring(0, filename.lastIndexOf("."));
                }
            }
            addChecksumToFile(filename, hc);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void addChecksumToFile(String filename, HashCode hc) {
        try(PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("checksums.txt", true)))) {
            out.println(hc + " FileName:" + filename + ":FileName " + new Date());
        }catch (IOException e) {
        }
    }

    @RequestMapping(value="/uploadFileDropbox", method=RequestMethod.POST)
    @ResponseBody
    public String handleFileUploadDropbox(@RequestParam("file") String file,
                                          @RequestParam("fileName") String fileName) throws IOException {
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
        String sCurrentLine = getChecksum(fileName, originalFile);

        if (sCurrentLine != null) return sCurrentLine;

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