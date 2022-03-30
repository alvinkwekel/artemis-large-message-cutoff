package org.example;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.jms.JMSException;
import javax.jms.TextMessage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.example.Jms.connectionFactory;

class JmsTest {

    @BeforeEach
    void clean() throws JMSException {
        final var connectionFactory = connectionFactory();
        try (final var connection = connectionFactory.createConnection()) {
            connection.start();
            var message = Jms.receive(connection);
            while (message != null) {
                message = Jms.receive(connection);
            }
        }
    }

    @Test
    void test1024kibLargeMessageSizeAndCompressionWith1044kibTextMessage() throws IOException, JMSException {
        testTextMessage("in/1044kib.b64", 1048576, true);
    }

    @Test
    void test1024kibLargeMessageSizeAndCompressionWith1044kibBytesMessage() throws IOException, JMSException {
        testBytesMessage("in/1044kib.b64", 1048576, true);
    }

    @Test
    void test1024kibLargeMessageSizeWithoutCompressionWith1044kibBytesMessage() throws IOException, JMSException {
        testBytesMessage("in/1044kib.b64", 1048576, false);
    }

    @Test
    void test512kibLargeMessageSizeAndCompressionWith1044kibBytesMessage() throws IOException, JMSException {
        testBytesMessage("in/1044kib.b64", 524288, true);
    }

    @Test
    void test2048kibLargeMessageSizeAndCompressionWith1044kibBytesMessage() throws IOException, JMSException {
        testBytesMessage("in/1044kib.b64", 2097152, true);
    }

    @Test
    void test384kibLargeMessageSizeAndCompressionWith418kibBytesMessage() throws IOException, JMSException {
        testBytesMessage("in/418kib.b64", 393216, true);
    }

    void testBytesMessage(String filePath, int minimalLargeMessageSize, boolean compressLargeMessage) throws IOException, JMSException {
        final var inData = Files.readAllBytes(Paths.get(filePath));
        final var connectionFactory = connectionFactory(minimalLargeMessageSize, compressLargeMessage);
        try (final var connection = connectionFactory.createConnection()) {
            Jms.sendBytesMessage(connection, inData);
            connection.start();
            final var message = Jms.receive(connection);
            final var outData = message.getBody(byte[].class);
            Assertions.assertEquals(inData.length, outData.length);
        }
    }

    void testTextMessage(String filePath, int minimalLargeMessageSize, boolean compressLargeMessage) throws IOException, JMSException {
        final var inData = Files.readString(Paths.get(filePath));
        final var connectionFactory = connectionFactory(minimalLargeMessageSize, compressLargeMessage);
        try (final var connection = connectionFactory.createConnection()) {
            Jms.sendTextMessage(connection, inData);
            connection.start();
            final var message = (TextMessage) Jms.receive(connection);
            final var outData = message.getText();
            Assertions.assertEquals(inData.getBytes(UTF_8).length, outData.getBytes(UTF_8).length);
        }
    }
}
