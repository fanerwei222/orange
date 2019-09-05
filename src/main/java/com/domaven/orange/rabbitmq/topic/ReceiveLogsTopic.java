package com.domaven.orange.rabbitmq.topic;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

/**
 * TODO
 * 路由器模式top类型消息接收者
 * @author fw
 * @date 2019/9/5 10:14
 */
public class ReceiveLogsTopic {
    private static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        /**
         * topic路由器模式栓塞制
         */
        channel.exchangeDeclare(EXCHANGE_NAME, "topic");
        /**
         * 随机获取队列名称
         */
        String queueName = channel.queueDeclare().getQueue();

        if (argv.length < 1) {
            System.err.println("Usage: ReceiveLogsTopic [binding_key]...");
            System.exit(1);
        }

        /**
         * 将对应的分类例如 lazy.black.dog
         * 设置为该队列的分类,那么该队列只处理lazy.black.dog 类型的消息;
         * 如果是 *.black.* 那么该队列处理 任何性格(lazy)的黑色(black)的动物(dog);
         */
        for (String bindingKey : argv) {
            channel.queueBind(queueName, EXCHANGE_NAME, bindingKey);
        }

        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [x] Received '" +
                    delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
        };
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> { });
    }
}
