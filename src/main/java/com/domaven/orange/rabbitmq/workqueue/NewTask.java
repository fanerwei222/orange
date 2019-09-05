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
            /**
             * 是否开启持久化操作
             */
            boolean durable = true;
            /**
             * 声明任务队列
             */
            channel.queueDeclare(TASK_NAME, durable, false, false ,null);
            String message = "hello... world... this... is .....a ..... new ...task....";
            /**
             * 发布消息,消息发布进行持久化操作,便于意外中断时恢复.
             */
            channel.basicPublish("", TASK_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
            System.out.println(" [x] Sent '" + message + "'");
        }
    }
}
