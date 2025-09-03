package gs.demo.scanner.domain.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

import java.time.Instant;

@Entity
public class InboxEvent extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(nullable = false, length = 100, unique = true)
    public String eventId;

    @Column(nullable = false, length = 100)
    public String aggregateId;

    @Column(nullable = false, length = 20)
    public String eventType;

    @Column(nullable = false, length = 20)
    public String aggregateType;

    @Column(nullable = false, updatable = false)
    public Instant receivedAt;

}
