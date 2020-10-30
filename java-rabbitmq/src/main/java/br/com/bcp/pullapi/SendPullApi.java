package br.com.bcp.pullapi;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class SendPullApi {

    private final static String CUSTOMER_A = "customer_a";
    private final static String CUSTOMER_B = "customer_b";

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(CUSTOMER_A, false, false, false, null);
        channel.queueDeclare(CUSTOMER_B, false, false, false, null);

        String value = "";
        Scanner scanner = new Scanner(System.in);
        do {
            System.out.print("How many messages? (0 to exit): ");
            value = scanner.nextLine();
            postMessage(channel, Integer.valueOf(value));
        } while (!"0".equalsIgnoreCase(value));
        scanner.close();
        connection.close();
        System.exit(0);
    }

    private static void postMessage(Channel channel, int count) throws Exception {
        // send messages for customer A (2x more than B)
        for (int i = 1; i <= count * 2; i++) {
            String message = "A - " + i + " - " + System.currentTimeMillis();
            Thread.sleep(Math.round(Math.random()*1000));
            channel.basicPublish("", CUSTOMER_A, null, message.getBytes());
            System.out.println(" [x] Sent '" + message + "'");
        }
        for (int i = 1; i <= count; i++) {
            String message = "B - " + i + " - " + System.currentTimeMillis();
            Thread.sleep(Math.round(Math.random()*1000));
            channel.basicPublish("", CUSTOMER_B, null, message.getBytes());
            System.out.println(" [x] Sent '" + message + "'");
        }
    }
}
