package uk.ac.glasgow.dcs.psd.Controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uk.ac.glasgow.dcs.psd.Components.*;
import uk.ac.glasgow.dcs.psd.Models.DownloadZip;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

/**
 * Controller responsible to manage
 * uploads and downloads from post/get
 * requests.
 */
@RestController
public class UploadDownloadController {

    /**
     * Property to indicate whether or not
     * use checksum method
     */
    @Value("${doChecksum}")
    private boolean doChecksum;

    /**
     * Property to indicate whether or not
     * upload files to dropbox
     */
    @Value("${uploadToDropbox}")
    private boolean uploadToDropbox;

    /**
     * Property to indicate whether or not
     * upload files to dropbox
     */
    @Value("${uploadToDropbox}")
    private boolean downloadFromDropbox;

    /**
     * <h1>Upload file</h1>
     * Allows to upload files for process
     *
     * @param file          file to upload
     * @return              path to created zip or error
     */
    @RequestMapping(value="/uploadFile", method= RequestMethod.POST)
    @ResponseBody
    public DownloadZip handleFileUpload(@RequestParam("file") MultipartFile file){
        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                String fileName = file.getOriginalFilename();
                BufferedOutputStream stream =
                        new BufferedOutputStream(
                                new FileOutputStream(new File(HelperComponent.getFileLocation(fileName))));

                stream.write(bytes);
                stream.close();

                String extension = fileName.substring(fileName.lastIndexOf("."), fileName.length());
                String fileWithoutExtension = HelperComponent.getFileLocation(fileName.substring(0, fileName.lastIndexOf(".")));

                File originalFile = new File(HelperComponent.getFileLocation(fileName));

                if (doChecksum) {
                    String existingFile = ChecksumComponent.getChecksum(fileName,originalFile,uploadToDropbox,downloadFromDropbox);
                    if (existingFile != null)
                        return new DownloadZip(1,null,null,0); // @todo change this
                }

                if(extension.compareTo(".docx") == 0) {
                    ExtractDocxComponent.extractTablesAndImages(HelperComponent.getFileLocation(fileName), fileWithoutExtension);
                }

                if(extension.compareTo(".pdf") == 0){
                    ExtractPdfComponent.process(fileWithoutExtension);
                }

                //delete the original uploaded file
                //noinspection ResultOfMethodCallIgnored
                originalFile.delete();

                return new DownloadZip(1, "/file/"+fileName.substring(0,fileName.lastIndexOf(".")),null,0);
//                return "/file/" + fileName.substring(0,fileName.lastIndexOf("."));
            } catch (Exception e) {
                return new DownloadZip(0,null,null,0); // @todo change this
            }
        } else {
            return new DownloadZip(0,null,null,0); // @todo change this
//            return "You failed to upload because the file was empty.";
        }
    }

    /**
     * <h1>Upload file using Dropbox</h1>
     * Allows to upload files for process
     * using Dropbox integration
     *
     * @param file          file to upload
     * @return              path to created zip or error
     */
    @RequestMapping(value="/uploadFileDropbox", method=RequestMethod.POST)
    @ResponseBody
    public String handleFileUploadDropbox(@RequestParam("file") String file,
                                          @RequestParam("fileName") String fileName) throws IOException {
        URL website = new URL(file);
        ReadableByteChannel rbc = Channels.newChannel(website.openStream());
        String inputFileName = HelperComponent.getFileLocation(fileName);
        FileOutputStream fos = new FileOutputStream(inputFileName);
        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        fos.close();

        String extension = fileName.substring(fileName.lastIndexOf("."), fileName.length());
        String fileWithoutExtension = HelperComponent.getFileLocation(fileName.substring(0, fileName.lastIndexOf(".")));

        File originalFile = new File(inputFileName);
        if (doChecksum) {
            String existingFile = ChecksumComponent.getChecksum(fileName,originalFile,uploadToDropbox,downloadFromDropbox);
            if (existingFile != null)
                return existingFile;
        }

        if(extension.compareTo(".docx") == 0) {
            ExtractDocxComponent.extractTablesAndImages(HelperComponent.getFileLocation(fileName), fileWithoutExtension);
        }

        if(extension.compareTo(".pdf") == 0){
            ExtractPdfComponent.process(fileWithoutExtension);
        }

        //noinspection ResultOfMethodCallIgnored
        originalFile.delete();

        return "/file/" + fileName.substring(0, fileName.lastIndexOf("."));
    }

    /**
     * <h1>Download file</h1>
     * Allows to download processed files
     *
     * @param fileName      filename to download
     * @return              path to file or error
     */
    @RequestMapping(value = "/file/{fileID}", method = RequestMethod.GET)
    public void getFile(
            @PathVariable("fileID") String fileName,
            HttpServletResponse response) throws IOException {

        String src = HelperComponent.getFileLocation(fileName+".zip");

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

}
