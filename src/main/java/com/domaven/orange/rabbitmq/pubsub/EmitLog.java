package com.domaven.orange.rabbitmq.pubsub;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * TODO
 * 消息发布者
 * @author fw
 * @date 2019/9/4 17:06
 */
public class EmitLog {
    private static final String EXCHANGE_NAME = "logs";
    public static void main(String[] args) throws Exception{
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel();
        ){
            /**
             * 设置交换器名称和模式
             */
            channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
            String message = args.length < 1 ? "info : hello world" : String.join(" ", args);
            /**
             * 发布消息
             */
            channel.basicPublish(EXCHANGE_NAME, "",null, message.getBytes("utf-8"));
            System.out.println(" [x] Sent '" + message + "'");
        }
    }
}
