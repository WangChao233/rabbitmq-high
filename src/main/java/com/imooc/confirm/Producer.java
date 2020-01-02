package com.imooc.confirm;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Producer {
    public static void main(String[] args) throws Exception {
        //创建一个ConnectionFactory,并进行配置
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("192.168.1.34");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("/");

        //通过连接工厂创建连接
        Connection connection = connectionFactory.newConnection();
        //通过connection创建一个Channel
        Channel channel = connection.createChannel();
        //指定消息投递模式：消息确认模式
        channel.confirmSelect();
        String exchangeName = "test_confirm_exchange";
        String routingKey = "confirm.save";
        Map<String, Object> headers = new HashMap<>(16);
        headers.put("1", 111);
        headers.put("2", 222);
        AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder()
                .deliveryMode(2)
                .contentEncoding("UTF-8")
                .headers(headers)
                .build();
        //通过channle发送数据
        for (int i = 0; i < 10; i++) {
            String msg = "Hello RabbitMQ Send confirm message!";
            channel.basicPublish(exchangeName, routingKey, properties, msg.getBytes());
            try {
                //  等待回复，如果回复true
                if (channel.waitForConfirms()) {
                    System.out.println("发送成功");
                }
                else {
                    System.out.println("发送失败");
                }
            }
            catch (InterruptedException e) {
                e.printStackTrace();
                System.out.println("发送失败");
            }
        }
        /*//添加确认监听(异步)
        channel.addConfirmListener(new ConfirmListener() {
            @Override
            public void handleAck(long deliveryTag, boolean multiple) throws IOException {
                System.out.println("---------ack--------"+deliveryTag+multiple);
            }

            @Override
            public void handleNack(long deliveryTag, boolean multiple) throws IOException {
                System.err.println("---------no ack--------"+deliveryTag+multiple);
            }
        });*/

    }
}
