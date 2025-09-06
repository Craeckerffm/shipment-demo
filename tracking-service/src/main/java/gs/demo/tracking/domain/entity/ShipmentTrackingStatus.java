package gs.demo.tracking.domain.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import gs.demo.tracking.domain.enums.ShipmentStatus;

import java.time.Instant;

@Entity
public class ShipmentTrackingStatus extends PanacheEntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(nullable = false, length = 100)
    public String trackingNumber;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    public ShipmentStatus shipmentStatus;

    @Column(nullable = false, length = 100, unique = true)
    public String eventId;

    @Column(nullable = false)
    public Instant occurredOn;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    public Instant createdAt;
}
