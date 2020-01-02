package com.imooc.message;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.util.HashMap;
import java.util.Map;

public class Producer {
    public static void main(String[] args) throws Exception{
        //创建一个ConnectionFactory,并进行配置
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("192.168.1.34");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("/");

        //通过连接工厂创建连接
        Connection connection = connectionFactory.newConnection();
        //通过connection创建一个Channel
        Channel channel = connection.createChannel();
        Map<String,Object> headers = new HashMap<>(16);
        headers.put("1",111);
        headers.put("2",222);
        AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder()
                .deliveryMode(2)
                .contentEncoding("UTF-8")
                .headers(headers)
                .build();
        //通过channle发送数据
        for (int i =0;i<10;i++) {
            String msg = "hello";
            channel.basicPublish("", "test001", properties, msg.getBytes());
        }
        //关闭连接
        channel.close();
        connection.close();
    }
}
