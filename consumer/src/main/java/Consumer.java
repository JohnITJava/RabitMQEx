import com.rabbitmq.client.*;


import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeoutException;

public class Consumer {
    public static void main(String[] args) throws NoSuchAlgorithmException, KeyManagementException, URISyntaxException, IOException, TimeoutException {

        ExecutorService es = Executors.newFixedThreadPool(5);
        ConnectionFactory factory = new ConnectionFactory();

        boolean autoAck = false;
        factory.setUri("amqp://guest:guest@localhost");
        factory.setConnectionTimeout(300000);
        Connection connection = factory.newConnection(es);
        Channel channel = connection.createChannel();
        channel.queueDeclare("my-queue", true, false, false, null);

        addConsToChan(autoAck, channel, "my-consumer1");
        addConsToChan(autoAck, channel, "my-consumer2");

    }

    private static void addConsToChan(boolean autoAck, Channel channel, String tagName) throws IOException {
        channel.basicConsume("my-queue", autoAck, tagName,
                new DefaultConsumer(channel) {
                    @Override
                    public void handleDelivery(String consumerTag,
                                               Envelope envelope,
                                               AMQP.BasicProperties properties,
                                               byte[] body)
                            throws IOException {
                        String routingKey = envelope.getRoutingKey();
                        String contentType = properties.getContentType();
                        long deliveryTag = envelope.getDeliveryTag();
                        // (process the message components here ...)
                        channel.basicAck(deliveryTag, false);
                        String recMes = new String(body, "UTF-8");
                        System.out.println(recMes);
                    }
                });
    }
}
