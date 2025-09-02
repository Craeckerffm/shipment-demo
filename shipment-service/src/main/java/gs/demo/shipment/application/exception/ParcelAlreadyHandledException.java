package gs.demo.shipment.application.exception;

public class ParcelAlreadyHandledException extends Exception {

    public ParcelAlreadyHandledException(String message) {
        super(message);
    }
}
