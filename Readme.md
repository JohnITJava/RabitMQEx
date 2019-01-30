Application demonstrates an exchange and queue messaging 
between sender and receiver with helping RabbitMQ framework.

For demonstration:
1. brew install rabbitmq
2. install docker hub
3. docker run -d --hostname my-rabbit --name some-rabbit -p 8080:15672 -p 5672:5672 rabbitmq:3-management