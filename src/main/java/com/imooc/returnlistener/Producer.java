package com.imooc.returnlistener;

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

        //添加ReturnListener
        channel.addReturnListener((replyCode,replyText,exchange,routingKey,properties,body)->{
                System.out.println("-----handle return-----");
                System.out.println("replyCode"+replyCode);
                System.out.println("replyText"+replyText);
                System.out.println("exchange"+exchange);
                System.out.println("routingKey"+routingKey);
                System.out.println("properties"+properties);
                System.out.println("body"+new String(body));
        });
        String exchangeName = "test_return_exchange";
        String routingKey = "return.save";
        String routingKeyError = "abc.save";
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
            String msg = "Hello RabbitMQ Send return message!";
            channel.basicPublish(exchangeName, routingKeyError, true,properties, msg.getBytes());
        }
    }
}
