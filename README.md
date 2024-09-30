# RoomBooker API

## Overview

RoomBooker is a Spring Boot application designed to facilitate hotel room bookings. It offers a RESTful API that enables users to manage hotels, rooms, bookings, and statistics efficiently. The application incorporates security features and uses Kafka for messaging.

## Table of Contents

- [Features](#features)
- [Technologies](#technologies)
- [API Documentation](#api-documentation)
- [Installation](#installation)


## Features

- **User Management:** Create, update, and delete users with roles (ADMIN, USER).
- **Hotel Management:** Add, update, and delete hotels and their ratings.
- **Room Management:** Manage rooms including filtering options based on various criteria.
- **Booking Management:** Book rooms and view all bookings.
- **Statistics Export:** Export statistics to CSV format.
- **Swagger UI:** Interactive API documentation for easy testing.

## Technologies

- **Java 17**
- **Spring Boot 3.3.3**
- **Spring Data JPA**
- **Spring Security**
- **Spring Kafka**
- **MongoDB**
- **MySQL**
- **OpenAPI (Swagger)**

## API Documentation

The API documentation is available via Swagger UI. You can access it at:
[Swagger UI](http://localhost:8080/swagger-ui/index.html)



## Installation

1. **Clone the repository:**
   ```bash
   git clone <https://github.com/Benhap1/RoomBooker>
   cd roomBooker

2. If you want to use Docker, the necessary files docker-compose.yaml and DockerFile are available in the root of the project.

3. Update application.properties with your database configurations:

    spring.datasource.url=jdbc:mysql://localhost:3306/hotels

    spring.datasource.username=root

    spring.datasource.password=admin

    spring.data.mongodb.uri=mongodb://localhost:27017/statistics_db

4. Kafka configuration:

#### Command to start Zookeeper:
    
zookeeper-server-start.bat /path/to/kafka/config/zookeeper.properties
    
#### Command to start Kafka broker:
    
kafka-server-start.bat /path/to/kafka/config/server.properties
    
#### Start consuming Messages from the Topics:
    
kafka-console-consumer.sh --topic user-registration --bootstrap-server localhost:9092 --from-beginning --group statistic-group
    
kafka-console-consumer.sh --topic room-booking --bootstrap-server localhost:9092 --from-beginning --group statistic-group



### User Initialization with Default Roles

The application includes a component, `RoleInitializer`, that automatically sets up two user roles (`ADMIN`, `USER`) and creates an admin user when the application starts. This initialization occurs via a class annotated with `@Component`, ensuring it is loaded as a Spring bean.

#### `RoleInitializer` Class

The `RoleInitializer` class implements the `CommandLineRunner` interface, which means that its `run()` method is executed after the application context is fully loaded. This class is responsible for ensuring that the default roles and an admin user are created if they do not already exist in the database.

##### Key Features:

- **User Roles**:  
  Two roles are initialized:
    - `ADMIN`: Has higher-level access and privileges.
    - `USER`: Has basic-level access.

- **Default Admin User**:  
  If the application is run for the first time or the admin user does not exist, an admin user is created with the following credentials:
    - Username: `ADMIN`
    - Password: `ADMIN`

    




