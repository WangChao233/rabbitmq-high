package com.imooc.limit;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class LimitConsumer {
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

        String exchangeName = "test_qo_exchange";
        String routingKey = "qo.save";
        String queueName = "test_qo_queue";

        channel.exchangeDeclare(exchangeName,"topic",true);
        //创建一个队列
        channel.queueDeclare(queueName,true,false,false,null);
        channel.queueBind(queueName,exchangeName,routingKey);
        //限流
        //1.autoAck设置为false(global false表示应用到本消费端而不是channel)
        channel.basicQos(0,1,false);

        //创建消费者
        MyConsumer myConsumer = new MyConsumer(channel);
        //设置channel,自动确认关闭
        channel.basicConsume(queueName,false,myConsumer);
    }
}
