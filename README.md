# Quarkus / Kafka Demo

Diese Demo zeigt das Zusammenspiel von Quarkus und Kafka.
Der Einfachheit halber sind alle Services in einem Gradle Multi-Projekt und greifen alle auf die gleiche DB zu (jeweils eigens Schema).
Untereinander kommunizieren die Services nur über Kafka.


## Installation
- docker-compose up -d (im Root-Projekt)
- services bauen & starten

## API
### Shipment erstellen
curl -X POST http://localhost:8081/create \
-H "Content-Type: application/json" \
-H "Accept: application/json" \
-d '{
"senderId": "1",
"recipientId": "2",
"parcelId": "994"
}'

### Shipment abrufen
curl http://localhost:8083/tracking/{tracking_number}
