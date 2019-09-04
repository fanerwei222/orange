package com.domaven.orange.rabbitmq.workqueue;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

/**
 * TODO
 * 新任务发布者
 * @author fw
 * @date 2019/9/4 14:45
 */
public class NewTask {
    private final static String TASK_NAME = "task_queue";
    public static void main(String[] args) throws Exception{
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try(Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
        ){
            boolean durable = true;
            channel.queueDeclare(TASK_NAME, durable, false, false ,null);
            String message = "hello... world... this... is .....a ..... new ...task....";
            channel.basicPublish("", TASK_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
            System.out.println(" [x] Sent '" + message + "'");
        }
    }
}
