package gs.demo.shipment.domain.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

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

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    public Instant createdAt;

}