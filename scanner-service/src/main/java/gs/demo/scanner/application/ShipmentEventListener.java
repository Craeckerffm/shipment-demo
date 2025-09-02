package gs.demo.scanner.application;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.reactive.messaging.Incoming;

import static io.quarkus.arc.ComponentsProvider.LOG;

@ApplicationScoped
public class ShipmentEventListener {

    @Incoming("shipment-created")
    @Transactional
    public void onShipmentCreated(String eventPayload) {

        LOG.infof("ddd");
    }
}
