# Quarkus / Kafka Demo

Diese Demo zeigt das Zusammenspiel von Quarkus und Kafka.

Das System besteht aus 3 Services:
- Shipment-Service
- Scanner-Service
- Tracking-Service

Der Shipment-Service nimmt per POST ein Shipment entgegen und publisht in Kafka.
Der Scanner-Service hört auf das initiale Erstellen eines Shipments und publiziert weitere (simulierte) Update-Events.
Der Tracking-Service liest das initiale Event des Shipment-Services sowie alle weiteren Update-Events aus Kafka persistiert sie und stellt sie über einen Endpoint zur Verfügung


Der Einfachheit halber sind alle Services in einem Gradle Multi-Module-Projekt und greifen alle auf die gleiche DB zu (jeweils eigens Schema).
Untereinander teilen die Services keine Abhängigkeiten und kommunizieren lediglich über Kafka.


## Installation
- docker-compose up -d (im Root-Projekt)
- module bauen & starten

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
