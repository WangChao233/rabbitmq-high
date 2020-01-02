package com.imooc.customize;

import com.rabbitmq.client.*;

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

        String exchangeName = "test_consumer_exchange";
        String routingKey = "consumer.#";
        String queueName = "test_consumer_queue";

        channel.exchangeDeclare(exchangeName,"topic",true);
        //创建一个队列
        channel.queueDeclare(queueName,true,false,false,null);
        channel.queueBind(queueName,exchangeName,routingKey);
        //创建消费者
        MyConsumer myConsumer = new MyConsumer(channel);
        //设置channel
        channel.basicConsume(queueName,true,myConsumer);
    }
}
