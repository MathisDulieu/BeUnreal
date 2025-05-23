# BeUnreal

BeUnreal is a Java 21 microservices-based social platform that allows users to share authentic moments through messages and media. Built with Spring Boot and Kafka, it features real-time messaging and location-based discovery for a responsive mobile experience.

## Project Structure

BeUnreal consists of the following microservices:

- **beunreal-api-gateway**: Centralized API gateway that routes requests to appropriate microservices
- **beunreal-auth-service**: Handles user authentication and security
- **beunreal-user-service**: Manages user profiles and friendship relationships
- **beunreal-message-service**: Processes instant messaging for individuals and groups
- **beunreal-media-service**: Manages storage and sharing of photos and short videos
- **beunreal-discovery-service**: Provides location-based content discovery
- **beunreal-notification-service**: Delivers real-time notifications

## Prerequisites

- Docker and Docker Compose
- JDK 21
- Maven or Gradle

## Getting Started

### Starting the Project

1. Clone the repository:
   ```bash
   git clone https://github.com/MathisDulieu/BeUnreal.git
   cd beunreal
   ```

2. Launch the entire stack with Docker Compose:
   ```bash
   docker-compose up -d
   ```

3. Wait for all services to initialize (this may take a few minutes on first startup).

### Stopping the Project

To stop all services:
```bash
docker-compose down
```

To stop a specific service:
```bash
docker-compose stop [service-name]
```

### Restarting the Project

To restart all services:
```bash
docker-compose restart
```

To restart a specific service:
```bash
docker-compose restart [service-name]
```

## Management Interfaces

### MongoDB Admin Interface (Mongo Express)

- **URL**: http://localhost:8081
- **Username**: admin
- **Password**: pass

### Kafka Management (AKHQ)

- **URL**: http://localhost:8082
- **Username**: admin
- **Password**: admin

## API Documentation

REST API documentation is available once the services are running:

- API Gateway Swagger UI: http://localhost/api/swagger-ui/index.html

## Service URLs

| Service | URL |
|---------|-----|
| API Gateway | http://localhost:80/api |
| Auth Service | http://localhost:8091 |
| User Service | http://localhost:8092 |
| Message Service | http://localhost:8093 |
| Media Service | http://localhost:8094 |

## Application Features

- User management with CRUD operations
- Friend discovery and connection
- Direct messaging with media sharing
- Group messaging
- Location-based content discovery

## Troubleshooting

### Common Issues

1. **Services fail to start**
    - Check Docker logs: `docker-compose logs [service-name]`
    - Ensure MongoDB and Kafka are running properly

2. **Kafka connectivity issues**
    - Verify Kafka broker is running: `docker-compose logs kafka`
    - Check topics exist: Access AKHQ interface

3. **Database connection errors**
    - Verify MongoDB is running: `docker-compose logs mongodb`
    - Check credentials in configuration

### Rebuilding Services

If you need to rebuild a specific service:
```bash
docker-compose build [service-name]
docker-compose up -d [service-name]
```