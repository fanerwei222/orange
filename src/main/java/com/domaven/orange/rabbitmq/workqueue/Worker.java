package com.domaven.orange.rabbitmq.workqueue;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

/**
 * TODO
 * 完成任务的小时工
 * @author fw
 * @date 2019/9/4 14:52
 */
public class Worker {
    private final static String TASK_QUEUE_NAME = "task_queue";
    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        /**
         * 声明任务队列
         */
        channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        /**
         * 一次分发一个任务
         */
        channel.basicQos(1);

        /**
         * 任务分发处理反馈
         */
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "utf-8");
            System.out.println(" [x] Received '" + message + "'");
            try {
                doWork(message);
            } finally {
                System.out.println(" [x] Done");
                /**
                 * 服务器会拒绝未确认的消息
                 */
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            }
        };
        /**
         * 是否开启自动检查
         */
        boolean autoAck = false; // acknowledgment is covered below
        channel.basicConsume(TASK_QUEUE_NAME, autoAck, deliverCallback, consumerTag -> {});
    }

    /**
     * 处理队列的任务
     * @param task
     */
    private static void doWork(String task) {
        for (char ch : task.toCharArray()){
            if (ch == '.') {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException  _ignored){
                    Thread.currentThread().interrupt();
                }

            }
        }
    }
}
