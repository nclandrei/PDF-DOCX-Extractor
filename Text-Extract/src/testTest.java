/**
 * Created by richy734 on 05/12/15.
 */

import cloudconvert.*;

import java.io.File;

public class testTest {

    public static void main(String args[]) {
        try {
            // Create service object
            CloudConvertService service = new CloudConvertService("mOtkuwKxMcGnJdpRzxRdwVSbulQhz-Yz8aHquK9_9qhB1RP6hYum1Sn2P_qnezV6c2v85X38vJJLpsQ6KFNc6w");

// Create conversion process
            ConvertProcess process = service.startProcess("pdf", "docx");

// Perform conversion
            process.startConversion(new File("res/sample.pdf"));

// Wait for result
            ProcessStatus status;
            waitLoop:
            while (true) {
                status = process.getStatus();

                switch (status.step) {
                    case FINISHED:
                        break waitLoop;
                    case ERROR:
                        throw new RuntimeException(status.message);
                }

                // Be gentle
                Thread.sleep(200);
            }

// Download result
            service.download(status.output.url, new File("output.docx"));

// Clean up
            process.delete();
        } catch (Exception e) {}

    }
}
