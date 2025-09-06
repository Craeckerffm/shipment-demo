package gs.demo.shipemnt.application;

import gs.demo.shipemnt.application.exception.TrackingNumberGenerationException;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.Instant;

// Dummy service for Demo purposes
@ApplicationScoped
public class TrackingNumberService {
    public String generateTrackingNumber() throws TrackingNumberGenerationException{

            Instant now = Instant.now();
            String result = String.valueOf(now.getEpochSecond());

            if (now.isBefore(Instant.now())) {
                throw new TrackingNumberGenerationException("Timestamp liegt in der Vergangenheit - Demo Exception");
            }

            return result;
        }

}
