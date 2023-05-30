# Always Right Temp Inc. - Anomaly Detection Service

Welcome to the recruitment task of 'Always Right Temp Inc', where we're seeking to build a temperature anomaly detection system using Kafka and a temperature measurement generator capable of handling 20k measurements per second.

## Tech Stack
The project uses a range of modern technologies including:
- Java 17
- Spring Boot 3
- Apache Maven
- Project Lombok
- H2 In-memory Database
- Apache Kafka
- JUnit, Mockito and Testcontainers for testing
- Docker and Docker Compose for containerization

## The Task

The system must receive temperature measurements and apply one of two algorithms to detect anomalies:

1. **Anomaly Detection Algorithm ONE**: This algorithm labels a temperature measurement as an anomaly if, within any 10 consecutive temperature measurements, there is one measurement that surpasses the average of the remaining 9 measurements by 5 degrees Celsius.

2. **Anomaly Detection Algorithm TWO**: This algorithm labels a temperature measurement as an anomaly if it exceeds the mean of all measurements by 5 degrees Celsius within a 10-second window based on the measurement timestamp.

The choice of algorithm is determined by a provided properties file.

All processed data should be stored and made available for visualization through implementation of all endpoints.

Unit and integration tests are required to ensure the quality and functionality of the code.

## How to Build and Execute

Ensure Docker and Docker Compose are installed on your machine. To build and execute the service, follow these steps:

1. Build the Docker images:
    ```bash
   SERVER_PORT=8080 \
   DB_URL=jdbc:h2:mem:test \
   DB_USERNAME=sa \
   DB_PASSWORD=password \
   KAFKA_BOOTSTRAP_SERVERS=kafka:9092 \
   KAFKA_GROUP_ID=group_id \
   ANOMALY_DETECTION_ALGORITHM=ONE \
   docker-compose build
    ```

2. Run the Docker Compose configuration (replace `ONE` with `TWO` if you want to use the second algorithm):
    ```bash
    SERVER_PORT=8080 \
    DB_URL=jdbc:h2:mem:test \
    DB_USERNAME=sa \
    DB_PASSWORD=password \
    KAFKA_BOOTSTRAP_SERVERS=kafka:9092 \
    KAFKA_GROUP_ID=group_id \
    ANOMALY_DETECTION_ALGORITHM=ONE \
    docker-compose up
    ```

## Disclaimer

This codebase is **NOT PRODUCTION-READY!!!**. It lacks important aspects of a production-level application, including but not limited to:

- Security measures: Proper authentication, authorization, and encryption have not been implemented.
- Documentation: The APIs are not documented with tools such as Swagger or OpenAPI.
- Error Handling: The service lacks a robust error handling mechanism.
- Logging: The service does not implement a complete logging mechanism for traceability.
- CI/CD: Continuous Integration and Continuous Deployment are not set up.
- DevOps Practices: This project does not follow some key DevOps practices.
- Test Structure: The tests lack a proper structure with reusable components.

This codebase is primarily for the recruitment process, and as such, it should not be used in a production environment without significant modifications. It serves as a basis for demonstrating potential software development skills.

## Postman Collection

For easier testing and interaction with the API, we've included a Postman collection in the `postman` folder. Postman is a popular API client that makes it easy for developers to create, share, test and document APIs.

In the `postman` folder, you will find two JSON files:

- `Anomaly Detection Service - API.postman_collection.json`: This file contains a collection of API requests that you can use to interact with the service.

- `Local Env.postman_environment.json`: This file represents an environment with a set of key-value pairs. It allows you to customize requests using variables, thus avoiding the need to hard-code values.

To use these files:

1. Download and install [Postman](https://www.postman.com/downloads/).
2. Open Postman.
3. Click on "Import" button at the top left corner.
4. Choose "Upload Files" and select both JSON files.
5. Select the environment you just imported from the dropdown at the top right corner.

You're now ready to send requests to the service!

Remember, before sending requests make sure that the service is up and running.
