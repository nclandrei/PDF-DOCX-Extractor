package cloudconvert;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.media.multipart.MultiPartFeature;

import javax.ws.rs.client.*;

public class ClientUtil {
    
    public static Client createClient() {
        return ClientBuilder.newBuilder()
                            .register(JacksonFeature.class)
                            .register(new CloudConvertMapperProvider("https"))
                            .register(MultiPartFeature.class)
                            //.register(LoggingFilter.class) // For debugging
                            .build();
    }
    
}
