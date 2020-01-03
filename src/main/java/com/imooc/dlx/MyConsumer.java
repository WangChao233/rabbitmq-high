package com.imooc.dlx;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import java.io.IOException;

public class MyConsumer extends DefaultConsumer {
    private Channel channel;
    /**
     * Constructs a new instance and records its association to the passed-in channel.
     *
     * @param channel the channel to which this consumer is attached
     */
    public MyConsumer(Channel channel) {
        super(channel);
        this.channel = channel;
    }

    @Override
    public void handleDelivery(String consumerTag,
                               Envelope envelope,
                               AMQP.BasicProperties properties,
                               byte[] body)
            throws IOException
    {
        /*System.out.println("-----consume message-----");
        System.out.println("consumerTag："+consumerTag);
        System.out.println("envelope："+envelope);
        System.out.println("properties："+properties);*/
        System.out.println("body："+new String(body));
        channel.basicReject(envelope.getDeliveryTag(), false);
    }
}
