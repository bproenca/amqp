package br.com.bcp.topic;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class SendLogsTopic {

    // private final static String QUEUE_NAME = "simple-javaa";
    private static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection(); Channel channel = connection.createChannel()) {
            // channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            channel.exchangeDeclare(EXCHANGE_NAME, "topic");
            postMessage(channel);
        }
        System.exit(0);
    }

    private static void postMessage(Channel channel) throws UnsupportedEncodingException, IOException {
        String routingKey, message = "";
        Scanner scanner = new Scanner(System.in);
        do {
            // Get the value from the user.
            System.out.print("Enter routingKey (aa|bb): ");
            routingKey = scanner.nextLine();
            System.out.print("Enter message (or exit): ");
            message = scanner.nextLine();
            
            channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes("UTF-8"));
            System.out.println(" [x] Sent '" + routingKey + "':'" + message + "'");
        } while (!"exit".equalsIgnoreCase(message));
        scanner.close();
    }
}