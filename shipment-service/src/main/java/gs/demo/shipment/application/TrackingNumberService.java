package gs.demo.shipment.application;

import gs.demo.shipment.application.exception.TrackingNumberGenerationException;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.UUID;

// Dummy service for Demo purposes
@ApplicationScoped
public class TrackingNumberService {
    public String generateTrackingNumber() throws TrackingNumberGenerationException{
        String result = UUID.randomUUID().toString();
        if(result == null) {
            throw new TrackingNumberGenerationException("Could not generate tracking number");
        }
        return result;
    }
}
