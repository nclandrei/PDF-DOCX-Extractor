package cloudconvert;

import org.glassfish.jersey.media.multipart.*;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition.FormDataContentDispositionBuilder;
import org.glassfish.jersey.media.multipart.file.*;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URI;
import java.text.ParseException;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

public class ConvertProcess {
    
    WebTarget root;
    ProcessArguments args;
    
    public ConvertProcess(URI root) {
        this(root, null);
    }
    
    
    public ConvertProcess(URI root, ProcessArguments args) {
        this.root = ClientUtil.createClient().target(root);
        this.args = args;
    }
    
    
    public ProcessStatus getStatus() {
        return root.request(APPLICATION_JSON)
                   .get(ProcessStatus.class);
    }
    
    
    public void startConversion(File file) throws ParseException, FileNotFoundException {
        if (!file.exists())
            throw new FileNotFoundException("File not found: " + file);
        startConversion(new FileDataBodyPart("file", file));
    }
    
    
    public void startConversion(InputStream input, String filename) throws ParseException {
        StreamDataBodyPart filePart = new StreamDataBodyPart("file", input);
        FormDataContentDispositionBuilder builder = FormDataContentDisposition.name("file")
                                                                              .fileName(filename);
        filePart.setFormDataContentDisposition(builder.build());
        startConversion(filePart);
    }
    
    
    private void startConversion(BodyPart bodyPart) {
        
        if (args == null)
            throw new IllegalStateException("No conversion arguments set.");
        
        MultiPart multipart = new FormDataMultiPart().field("input", "upload")
                                                     .field("outputformat", args.outputformat)
                                                     .bodyPart(bodyPart);
        
        root.request(MediaType.APPLICATION_JSON)
            .post(Entity.entity(multipart, multipart.getMediaType()));
    }
    
    
    public void delete() {
        root.path("delete").request().get();
    }
    
    
    public void cancel() {
        root.path("cancel").request().get();
    }
    
}
