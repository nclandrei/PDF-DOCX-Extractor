package uk.ac.glasgow.dcs.psd.Controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uk.ac.glasgow.dcs.psd.Components.*;
import uk.ac.glasgow.dcs.psd.Models.UploadZip;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

/**
 * Controller responsible to manage uploads and downloads from post/get
 * requests.
 */
@RestController
public class UploadDownloadController {

    /**
     * Property to indicate whether or not use checksum method.
     */
    @Value("${doChecksum}")
    private boolean doChecksum;

    /**
     * Property to indicate whether or not upload files to dropbox.
     */
    @Value("${uploadToDropbox}")
    private boolean uploadToDropbox;

    /**
     * Property to indicate whether or not upload files to dropbox.
     */
    @Value("${uploadToDropbox}")
    private boolean downloadFromDropbox;

    /**
     * <h1>Upload file</h1> Allows to upload files for process.
     *
     * @param file file to upload
     * @return path to created zip or error
     */
    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    @ResponseBody
    public UploadZip handleFileUpload(
            @RequestParam("file") final MultipartFile file) {
        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                String fileName = file.getOriginalFilename();
                BufferedOutputStream stream =
                        new BufferedOutputStream(
                                new FileOutputStream(new File(HelperComponent
                                        .getFileLocation(fileName))));

                stream.write(bytes);
                stream.close();

                String extension = fileName.substring(fileName.lastIndexOf("."),
                        fileName.length());
                String fileWithoutExtension = HelperComponent.getFileLocation(
                        fileName.substring(0, fileName.lastIndexOf(".")));

                File originalFile =
                        new File(HelperComponent.getFileLocation(fileName));

                if (doChecksum) {
                    UploadZip existingFile = ChecksumComponent
                            .getChecksum(fileName, originalFile,
                                    uploadToDropbox, downloadFromDropbox);
                    if (existingFile != null) {
                        return existingFile;
                    }
                }

                if (extension.compareTo(".docx") == 0) {
                    ExtractDocxComponent.extractTablesAndImages(
                            HelperComponent.getFileLocation(fileName),
                            fileWithoutExtension);
                }

                if (extension.compareTo(".pdf") == 0) {
                    ExtractPdfComponent.process(fileWithoutExtension);
                }

                //delete the original uploaded file
                HelperComponent.delete(originalFile);

                String href = "/file/" + fileName
                        .substring(0, fileName.lastIndexOf("."));
                return new UploadZip(1, href, fileName,
                        "Upload and Conversion was successful");
            } catch (Exception e) {
                return new UploadZip(0, null, null,
                        "Failed to convert the file. " +
                                "If you see this more than once, " +
                                "please submit a bug report.");
            }
        } else {
            return new UploadZip(0, null, null,
                    "Failed to upload the file. " +
                            "If you see this more than once, " +
                            "please submit a bug report.");
        }
    }

    /**
     * <h1>Upload file using Dropbox</h1> Allows to upload files for process
     * using Dropbox integration.
     *
     * @param file file to upload
     * @param fileName the name of the file
     * @return path to created zip or error
     * @throws IOException throws an IOException if the file is not found
     */
    @RequestMapping(value = "/uploadFileDropbox", method = RequestMethod.POST)
    @ResponseBody
    public UploadZip handleFileUploadDropbox(@RequestParam("file") final String file,
                                             @RequestParam("fileName")
                                             final String fileName)
            throws IOException {
        try {
            if(file == null || fileName == null) {
                return new UploadZip(1, null, null,
                        "Failed to get Dropbox file.");
            }
            URL website = new URL(file);
            ReadableByteChannel rbc = Channels.newChannel(website.openStream());
            String inputFileName = HelperComponent.getFileLocation(fileName);
            FileOutputStream fos = new FileOutputStream(inputFileName);
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            fos.close();

            String extension = fileName.substring(fileName.lastIndexOf("."),
                    fileName.length());
            String fileWithoutExtension = HelperComponent.getFileLocation(
                    fileName.substring(0, fileName.lastIndexOf(".")));

            File originalFile = new File(inputFileName);
            if (doChecksum) {
                UploadZip existingFile = ChecksumComponent
                        .getChecksum(fileName, originalFile, uploadToDropbox,
                                downloadFromDropbox);
                if (existingFile != null) {
                    return existingFile;
                }
            }

            if (extension.compareTo(".docx") == 0) {
                ExtractDocxComponent.extractTablesAndImages(
                        HelperComponent.getFileLocation(fileName),
                        fileWithoutExtension);
            }

            if (extension.compareTo(".pdf") == 0) {
                ExtractPdfComponent.process(fileWithoutExtension);
            }

            originalFile.delete();
            String href =
                    "/file/" + fileName.substring(0, fileName.lastIndexOf("."));

            return new UploadZip(1, href, fileName,
                    "Upload and Conversion was successful");
        } catch (Exception e) {
            return new UploadZip(0, null, null,
                    "Failed to convert the file. " +
                            "If you see this more than once, " +
                            "please submit a bug report.");
        }
    }

    /**
     * <h1>Download file</h1> Allows to download processed files.
     *
     * @param fileName filename to download
     * @param response the response
     * @throws IOException throws error if file cannot be found
     */
    @RequestMapping(value = "/file/{fileID}", method = RequestMethod.GET)
    public void getFile(
            @PathVariable("fileID") final String fileName,
            final HttpServletResponse response) throws IOException {

        String src = HelperComponent.getFileLocation(fileName + ".zip");

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

        if (!doChecksum)
        {
            file.delete();
        }
    }

}
