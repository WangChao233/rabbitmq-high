package com.imooc.message;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

import java.util.Map;

public class Consumer {
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

        //创建一个队列
        channel.queueDeclare("test001",true,false,false,null);

        //创建消费者
        QueueingConsumer queueingConsumer = new QueueingConsumer(channel);

        //设置channel
        channel.basicConsume("test001",true,queueingConsumer);
        //获取消息
        while(true){
            QueueingConsumer.Delivery delivery = queueingConsumer.nextDelivery();
            String msg = new String(delivery.getBody());
            System.out.println("消费端："+msg);
            System.out.println(delivery.getProperties().getHeaders());

            System.out.println(delivery.getProperties().getExpiration());
            //Envelope envelope = delivery.getEnvelope();
        }
    }
}
