# Chat Application using Redis

A Spring Boot-based RESTful chat application that uses Redis as the primary data store for room management, message persistence, and real-time message broadcasting.

## Tech Stack

- **Java 17**
- **Spring Boot**
- **Redis** (Docker)
- **JUnit 5**


## Running the Application

### - Start Redis via Docker

```bash
docker compose up -d
```

### - Run the Application

```bash
mvn spring-boot:run
```

### - Test via Postman

**Base URL:**
```
http://localhost:8080
```


## Architecture Overview

The application follows a layered architecture:

```
Controller → Service → Repository → Redis
```

### Responsibilities:
- **Controller** → Handles REST requests
- **Service** → Business logic & validation
- **Repository** → Redis interaction
- **Redis** → Data storage & Pub/Sub

##  Redis Data Modeling

| Feature | Redis Type | Key Pattern |
|---------|-----------|-------------|
| Room metadata | Hash | `chatroom:{roomId}` |
| Participants | Set | `chatroom:{roomId}:participants` |
| Messages | List | `chatroom:{roomId}:messages` |
| Room registry | Set | `chatrooms` |
| Pub/Sub channel | Channel | `chatroom:{roomId}:channel` |

### Design Decisions

- **Set** (`chatrooms`) ensures atomic room uniqueness using `SADD`
- **Set** (`participants`) prevents duplicate users automatically
- **List** (`messages`) stores ordered chat history
- `LRANGE -N -1` efficiently retrieves latest messages
- **Pub/Sub** channels isolate messages per room

## Real-Time Messaging

When a message is sent:

1. Message is stored in Redis List
2. Message is published to `chatroom:{roomId}:channel`
3. Backend subscribes using a Redis pattern topic:
   ```
   chatroom:*:channel
   ```
4. Messages are logged to console for demonstration

> **Note:** In production, clients (via WebSockets) would subscribe only to rooms they joined.

## API Endpoints

### Create Room
```http
POST /api/chatapp/chatrooms
```
**Body:**
```json
{
  "roomName": "general"
}
```

### Replace "roomId" with "general" for testing

### Join Room 
```http
POST /api/chatapp/chatrooms/{roomId}/join
```

**Body:**
```json
{
   "participant": "anamya"
}
```

### Send Message
```http
POST /api/chatapp/chatrooms/{roomId}/messages
```

**Body:**
```json
{
   "participant": "anamya",
   "message": "Hello everyone!"
}
```

### Get Chat History
```http
GET /api/chatapp/chatrooms/{roomId}/messages?limit=10
```

### Delete Room
```http
DELETE /api/chatapp/chatrooms/{roomId}
```

## Validation Rules

- Room names cannot be empty
- Duplicate rooms return **409 Conflict**
- Sending message without joining returns **400**
- Non-existent rooms return **404**

##  Testing


Tests verify:
- Room creation & uniqueness
- Message ordering
- Membership enforcement
- Room deletion lifecycle

Redis is flushed before each test to ensure isolation.

**Run tests:**
```bash
mvn test
```

## Redis Persistence

Redis runs with Append Only File (AOF) enabled:

```yaml
--appendonly yes
```

This ensures message durability across container restarts.

## Scalability Considerations

For large-scale systems:

- Redis stores **recent messages only**
- Full history stored in a **durable database**
- **WebSockets** used for bidirectional real-time messaging
- Redis **Pub/Sub** enables horizontal scaling across multiple instances
- **Redis Cluster** can mitigate hot key issues


