package com.domaven.orange.rabbitmq.pubsub;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

/**
 * TODO
 * 消息接收者副本
 * @author fw
 * @date 2019/9/4 17:17
 */
public class ReceiveLogsCopy {
    private static final String EXCHANGE_NAME = "logs";
    public static void main(String[] args) throws Exception{
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        /**
         * 声明交换器
         */
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");

        /**
         * 随机获取一个队列名称
         */
        String queueName = channel.queueDeclare().getQueue();
        /**
         * 绑定指定名称的队列
         */
        channel.queueBind(queueName, EXCHANGE_NAME, "");

        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        /**
         * 消息处理
         */
        DeliverCallback deliverCallback = (cosumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "utf-8");
            System.out.println(" [x] Received '" + message + "'");
        };

        /**
         * 消费消息
         */
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {});
    }
}
