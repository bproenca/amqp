package br.com.bcp.pullapi;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.GetResponse;

public class RecvPullApi {

    private final static String[] CUSTOMERS = {"customer_a", "customer_b"};

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(CUSTOMERS[0], false, false, false, null);
        channel.queueDeclare(CUSTOMERS[1], false, false, false, null);

        boolean autoAck = false;
        System.out.println("Press Ctrl+C to exit");
        while (true) {
            System.out.println(" [*] Fetching new messages: " + System.currentTimeMillis());
            for (String queueName : CUSTOMERS) {
                GetResponse response = channel.basicGet(queueName, autoAck);
                if (response == null) {
                    System.out.println(" [-] No message found in Queue: " + queueName);
                } else {
                    byte[] body = response.getBody();
                    long deliveryTag = response.getEnvelope().getDeliveryTag();
                    try {
                        process(new String(body) + " from: " + queueName, response.getEnvelope().isRedeliver());
                        channel.basicAck(deliveryTag, false); // acknowledge receipt of the message
                    } catch (Exception ex) {
                        System.out.println(ex.getMessage());
                        channel.basicNack(deliveryTag, false, true);
                    } 
                }                
            }
            Thread.sleep(7000);
        }
    }

    private static void process(String message, boolean redelivery) throws InterruptedException {
        if (!redelivery && (message.startsWith("A - 2") || message.startsWith("B - 2"))) {
            throw new RuntimeException("## Exception for message: " + message);
        } else {
            System.out.println(" [x] Received '" + message + "'");
            Thread.sleep(6000); // processing
        }
    }
}