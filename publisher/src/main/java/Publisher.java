import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeoutException;

public class Publisher {

    public static void main(String[] args) throws NoSuchAlgorithmException, KeyManagementException, URISyntaxException, IOException, TimeoutException, InterruptedException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUri("amqp://guest:guest@localhost");
        /*factory.setUsername("guest");
        factory.setPassword("guest");
        factory.setVirtualHost("/");
        factory.setHost("localhost");
        factory.setPort(5672);*/
        factory.setConnectionTimeout(300000);

        Connection connection = factory.newConnection();
        Channel channel = ((Connection) connection).createChannel();

        channel.exchangeDeclare("my-exchange", "direct", true);
        channel.queueDeclare("my-queue", true, false, false, null);
        channel.queueBind("my-queue", "my-exchange", "my-rout");

        //channel.queueDeclare("my-queue", true, false, false, null);

        int count = 0;

        while (true){
            String message = "Message number " + count;

            channel.basicPublish("my-exchange", "my-rout", null, message.getBytes());
            count++;
            System.out.println("Published message: " + message);

            Thread.sleep(1000);
        }

    }
}
