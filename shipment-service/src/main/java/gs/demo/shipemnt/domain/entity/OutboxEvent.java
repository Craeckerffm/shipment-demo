package gs.demo.shipemnt.domain.entity;


import gs.demo.shipemnt.domain.enums.EventStatus;
import gs.demo.shipemnt.domain.enums.ShipmentStatus;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

import java.time.Instant;

@Entity
public class OutboxEvent extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(nullable = false, length = 100)
    public String aggregateType;

    @Column(nullable = false, length = 100)
    public String aggregateId;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    public ShipmentStatus eventType;

    @Column(nullable = false, columnDefinition = "TEXT")
    public String payload;

    @Column(nullable = false)
    public Instant occurredOn;

    @Column(nullable = false)
    public boolean processed = false;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    public EventStatus status = EventStatus.PENDING;

    @Column(nullable = false, length = 100)
    public String eventId;

}
