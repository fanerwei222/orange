package com.domaven.orange.rabbitmq.helloworld;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;

/**
 * TODO
 * 发布者
 * @author fw
 * @date 2019/9/4 14:14
 */
public class Send {
    private final static String QUEUE_NAME = "hello";
    public static void main(String[] args) throws Exception{
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try(Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
        ){
            /**
             * 声明队列
             */
            channel.queueDeclare(QUEUE_NAME, false, false, false ,null);
            String message = "Hello World!";
            /**
             * 发布消息
             */
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
            System.out.println(" [x] Sent '" + message + "'");
        }
    }
}
