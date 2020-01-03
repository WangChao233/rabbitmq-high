package com.imooc.dlx;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * 死信队列
 */
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

        String exchangeName = "test_dlx_exchange";
        String routingKey = "dlx.#";
        String queueName = "test_dlx_queue";

        //声明死信队列
        channel.exchangeDeclare("dlx.exchange","topic",true,false,null);
        channel.queueDeclare("dlx.queue",true,false,false,null);
        channel.queueBind("dlx.queue","dlx.exchange","#");

        Map<String,Object> arguments = new HashMap<>(16);
        arguments.put("x-dead-letter-exchang","dlx.exchange");
        channel.exchangeDeclare(exchangeName,"topic",true,false,null);
        //创建一个队列
        channel.queueDeclare(queueName,true,false,false,arguments);
        channel.queueBind(queueName,exchangeName,routingKey);


        //创建消费者
        MyConsumer myConsumer = new MyConsumer(channel);
        //设置channel
        channel.basicConsume(queueName,false,myConsumer);
    }
}
