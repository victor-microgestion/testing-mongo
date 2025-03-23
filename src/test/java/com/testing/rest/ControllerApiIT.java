package com.testing.rest;

import java.nio.file.Paths;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.images.builder.ImageFromDockerfile;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.org.awaitility.Awaitility;
import org.testcontainers.utility.MountableFile;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Testcontainers
public class ControllerApiIT {

    private static final Network network = Network.newNetwork();
    private static final int HTTP_PORT = 9080;

    private static ImageFromDockerfile invImage = new ImageFromDockerfile("liberty-mongodb:1.0-SNAPSHOT")
            .withDockerfile(Paths.get("./Dockerfile"));

    @Container
    private static final MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:6.0")
            .withNetwork(network)
            .withNetworkAliases("mongodb");

    @Container
    private static final GenericContainer<?> libertyContainer = new GenericContainer<>(invImage)
            .withNetwork(network)
            .withExposedPorts(HTTP_PORT)
            .withEnv("MONGO_HOST", "mongodb")
            .withEnv("MONGO_PORT", "27017");

    private static Client client;
    private static String baseUrl;

    @BeforeAll
    public static void setup() {
        client = (ResteasyClient) ResteasyClientBuilder.newClient();
        baseUrl = "http://" + libertyContainer.getHost() + ":" + libertyContainer.getMappedPort(HTTP_PORT)
                + "/api/mock";
    }

    @AfterAll
    public static void tearDown() {
        client.close();
        libertyContainer.stop();
        mongoDBContainer.stop();
    }

    @Test
    public void testGetUsers() {
        Response response = client.target(baseUrl).request().get();
        Assertions.assertEquals(200, response.getStatus());
    }

    @Test
    public void testHealthCheck() {
        Response response = client.target(baseUrl + "/health")
                .request(MediaType.TEXT_PLAIN)
                .get();
        Assertions.assertEquals(200, response.getStatus());
    }

    @Test
    public void testUpdateUser() {
        String userJson = "{\"name\":\"John\", \"provider\":\"Google\", \"role\":\"admin\"}";
        // Intenta hasta 3 veces con un delay de 5 segundos
        Response response = client.target(baseUrl + "/john@example.com")
                .request()
                .put(Entity.entity(userJson, MediaType.APPLICATION_JSON));
        Assertions.assertEquals(200, response.getStatus());
    }

    /*
     * @Test
     * public void testGetUsers() {
     * // Client client = ClientBuilder.newClient();
     * // Response response = client.target("localhost").request().get();
     * Assertions.assertEquals(200, 200); // Espera 404, pero el endpoint retorna
     * 200
     * }
     */
}