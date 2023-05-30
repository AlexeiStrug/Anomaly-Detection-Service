package com.example.demo.anomaly;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.testcontainers.shaded.org.awaitility.Awaitility.await;

@SpringJUnitConfig
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class AnomalyControllerIT {

    public static final String COMMON_PATH = "/v1/anomalies";
    @Container
    public static KafkaContainer kafkaContainer = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:latest"));

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private MockMvc mockMvc;

    @DynamicPropertySource
    static void registerKafkaProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.consumer.bootstrap-servers", kafkaContainer::getBootstrapServers);
        registry.add("spring.kafka.producer.bootstrap-servers", kafkaContainer::getBootstrapServers);
    }

    @AfterAll
    static void cleanup() {
        if (kafkaContainer != null && kafkaContainer.isRunning()) {
            kafkaContainer.stop();
        }
    }

    @Test
    public void testGetAnomaliesByThermometerId_WithExistingId_ReturnsNonEmptyResponse() throws InterruptedException, JsonProcessingException {
        // Wait for Kafka to be ready
        waitForKafkaToBeReady();

        // Wait for the measurement to be processed and anomaly to be detected
        Thread.sleep(2000);

        // Execute the API call
        ResponseEntity<String> response = executeCall(COMMON_PATH + "/thermometer/Thermometer1");

        // Parse the response body as JSON
        JsonNode responseBody = getResponseBody(response);

        // Assert what the response body is properly returned
        assertNotNull(response.getBody());
        assertFalse(responseBody.get("content").isEmpty());
        assertTrue(responseBody.get("totalElements").asInt() > 0);
    }

    @Test
    public void testGetAnomaliesByThermometerId_WithDifferentExistingIds_ReturnsNonEmptyResponse() throws InterruptedException, JsonProcessingException {
        // Wait for Kafka to be ready
        waitForKafkaToBeReady();

        // Wait for the measurement to be processed and anomaly to be detected
        Thread.sleep(2000);

        // Execute the API call for different existing IDs
        for (int i = 1; i <= 10; i++) {
            ResponseEntity<String> response = executeCall(COMMON_PATH + "/thermometer/Thermometer" + i);

            // Parse the response body as JSON
            JsonNode responseBody = getResponseBody(response);

            // Assert what the response body is properly returned
            assertNotNull(response.getBody());
            assertFalse(responseBody.get("content").isEmpty());
            assertTrue(responseBody.get("totalElements").asInt() > 0);
        }
    }


    @Test
    public void testGetAnomaliesByThermometerId_WithNotExistingId_ReturnsEmptyResponse() throws InterruptedException, JsonProcessingException {
        // Wait for Kafka to be ready
        waitForKafkaToBeReady();

        // Wait for the measurement to be processed and anomaly to be detected
        Thread.sleep(2000);

        // Execute the API call
        ResponseEntity<String> response = executeCall(COMMON_PATH + "/thermometer/Thermometer" + "notexistingId");

        // Parse the response body as JSON
        JsonNode responseBody = getResponseBody(response);

        // Assert what the response body is properly returned
        assertNotNull(response.getBody());
        assertTrue(responseBody.get("content").isEmpty());
        assertEquals(0, responseBody.get("totalElements").asInt());
    }

    @Test
    public void testGetAnomaliesByRoomId_WithExistingId_ReturnsNonEmptyResponse() throws InterruptedException, JsonProcessingException {
        // Wait for Kafka to be ready
        waitForKafkaToBeReady();

        // Wait for the measurement to be processed and anomaly to be detected
        Thread.sleep(2000);

        // Execute the API call
        ResponseEntity<String> response = executeCall(COMMON_PATH + "/room/Room1");

        // Parse the response body as JSON
        JsonNode responseBody = getResponseBody(response);

        // Assert what the response body is properly returned
        assertNotNull(response.getBody());
        assertFalse(responseBody.get("content").isEmpty());
        assertTrue(responseBody.get("totalElements").asInt() > 0);
    }

    @Test
    public void testGetAnomaliesByRoomId_WithDifferentExistingIds_ReturnsNonEmptyResponse() throws InterruptedException, JsonProcessingException {
        // Wait for Kafka to be ready
        waitForKafkaToBeReady();

        // Wait for the measurement to be processed and anomaly to be detected
        Thread.sleep(2000);

        // Execute the API call for different existing IDs
        for (int i = 1; i <= 10; i++) {
            ResponseEntity<String> response = executeCall(COMMON_PATH + "/room/Room" + i);

            // Parse the response body as JSON
            JsonNode responseBody = getResponseBody(response);

            // Assert what the response body is properly returned
            assertNotNull(response.getBody());
            assertFalse(responseBody.get("content").isEmpty());
            assertTrue(responseBody.get("totalElements").asInt() > 0);
        }
    }

    @Test
    public void testGetAnomaliesByRoomId_WithNotExistingId_ReturnsEmptyResponse() throws InterruptedException, JsonProcessingException {
        // Wait for Kafka to be ready
        waitForKafkaToBeReady();

        // Wait for the measurement to be processed and anomaly to be detected
        Thread.sleep(2000);

        // Execute the API call
        ResponseEntity<String> response = executeCall(COMMON_PATH + "/room/Room" + "notexistingId");

        // Parse the response body as JSON
        JsonNode responseBody = getResponseBody(response);

        // Assert what the response body is properly returned
        assertNotNull(response.getBody());
        assertTrue(responseBody.get("content").isEmpty());
        assertEquals(0, responseBody.get("totalElements").asInt());
    }

    @Test
    public void testGetThermometersWithHighAnomalyCount_WithThreshold_1_ReturnsNonEmptyResponse() throws InterruptedException, JsonProcessingException {
        // Wait for Kafka to be ready
        waitForKafkaToBeReady();

        // Wait for the measurement to be processed and anomaly to be detected
        Thread.sleep(2000);

        // Execute the API call
        ResponseEntity<String> response = executeCall(COMMON_PATH + "/high-threshold/1");

        // Parse the response body as JSON
        JsonNode responseBody = getResponseBody(response);

        // Assert that the response body is not empty and has a non-zero total elements
        assertNotNull(response.getBody());
        assertFalse(responseBody.get("content").isEmpty());
        assertTrue(responseBody.get("totalElements").asInt() > 0);
    }

    @Test
    public void testGetThermometersWithHighAnomalyCount_WithThreshold_1000000_ReturnsEmptyResponse() throws InterruptedException, JsonProcessingException {
        // Wait for Kafka to be ready
        waitForKafkaToBeReady();

        // Wait for the measurement to be processed and anomaly to be detected
        Thread.sleep(2000);

        // Execute the API call
        ResponseEntity<String> response = executeCall(COMMON_PATH + "/high-threshold/1000000");

        // Parse the response body as JSON
        JsonNode responseBody = getResponseBody(response);

        // Assert that the response body is not empty and has a non-zero total elements
        assertNotNull(response.getBody());
        assertTrue(responseBody.get("content").isEmpty());
        assertEquals(0, responseBody.get("totalElements").asInt());
    }


    @Test
    public void testInvalidHttpMethod_ReturnsMethodNotAllowed() {
        // Send a PUT request to the API endpoint
        ResponseEntity<String> putResponse = restTemplate.exchange(COMMON_PATH + "/thermometer/Thermometer1", HttpMethod.PUT, null, String.class);

        // Assert that the response status is 405 Method Not Allowed
        assertEquals(HttpStatus.METHOD_NOT_ALLOWED, putResponse.getStatusCode());

        // Send a DELETE request to the API endpoint
        ResponseEntity<String> deleteResponse = restTemplate.exchange(COMMON_PATH + "/thermometer/Thermometer1", HttpMethod.DELETE, null, String.class);

        // Assert that the response status is 405 Method Not Allowed
        assertEquals(HttpStatus.METHOD_NOT_ALLOWED, deleteResponse.getStatusCode());
    }

    @Test
    public void testStreamSse_ReturnsSseEmitter() throws Exception {
        // Wait for Kafka to be ready
        waitForKafkaToBeReady();

        // Wait for the measurement to be processed and anomaly to be detected
        Thread.sleep(2000);

        // Execute the API call and start SSE stream
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(COMMON_PATH + "/stream")
                        .accept(MediaType.TEXT_EVENT_STREAM))
                .andExpect(request().asyncStarted())
                .andReturn();

        // Use Awaitility to wait for at least one SSE event to be sent
        await().atMost(10, TimeUnit.SECONDS).until(() -> {
            // Check the response content incrementally
            String content = mvcResult.getResponse().getContentAsString();
            return content != null && !content.isEmpty();
        });

        // Get the response content
        String content = mvcResult.getResponse().getContentAsString();

        // Assert that the response content is not empty
        assertNotNull(content);
        assertFalse(content.isEmpty());
    }

    private ResponseEntity<String> executeCall(String path) {
        return restTemplate.getForEntity(path, String.class);
    }

    private JsonNode getResponseBody(ResponseEntity<String> response) throws JsonProcessingException {
        return new ObjectMapper().readTree(response.getBody());
    }

    private void waitForKafkaToBeReady() throws InterruptedException {
        while (!kafkaContainer.isRunning()) {
            Thread.sleep(1000);
        }
    }
}

