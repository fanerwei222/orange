package com.domaven.orange.rabbitmq.routing;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

/**
 * TODO
 * 路由器模式(直接模式)消息接收者
 * @author fw
 * @date 2019/9/5 9:45
 */
public class ReceiveLogsDirect {
    private static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, "direct");

        String queueName = channel.queueDeclare().getQueue();
        if (args.length < 1) {
            System.err.println("Usage: ReceiveLogsDirect [info] [warning] [error]");
            System.exit(1);
        }

        /**
         * 对不同的消息分类分配不同的消息队列
         * 例如:此处的args传入的是info, warning, error
         * 那么该客户端就会处理这三种类型的消息
         */
        for (String severity : args) {
            /**
             * 只接受该分类的消息,其他类别的消息不予理睬;
             * 例如客户端设置消息分类传入为 error;
             * 那么对info和warning类型的消息将不理睬;
             * 如果客户端设置消息分类传入为 info, warning, error;
             * 那么对这三种消息都做处理;
             * 再新建一个客户端用来专门处理error类型消息;
             */
            channel.queueBind(queueName, EXCHANGE_NAME, severity);
        }
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        /**
         * 消息处理
         */
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [x] Received '" +
                    delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
        };
        /**
         * 消费消息
         */
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> { });
    }
}
