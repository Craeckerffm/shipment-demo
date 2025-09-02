package gs.demo.shipment.domain.entity;

import gs.demo.shipment.domain.enums.ShipmentStatus;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

@Entity
public class Shipment extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(nullable = false, length = 50, unique = true)
    public String parcelId;

    @Column(nullable = false, length = 50)
    public String senderId;

    @Column(nullable = false, length = 50)
    public String recipientId;

    @Column(nullable = false, length = 50)
    public String trackingNumber;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    public ShipmentStatus status;
}