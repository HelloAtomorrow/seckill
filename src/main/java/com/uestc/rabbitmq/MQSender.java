package com.uestc.rabbitmq;

import com.rabbitmq.client.MessageProperties;
import com.uestc.redis.RedisService;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MQSender {

    @Autowired
    AmqpTemplate amqpTemplate;

    @Autowired
    RedisService redisService;

//    public void send(Object message) {
//        String msg = RedisService.beanToString(message);
//        System.out.println("send message: " + msg);
//        amqpTemplate.convertAndSend(MQConfig.DIRECT_QUEUE, msg);
//    }
//
//    public void sendTopic(Object message) {
//        String msg = RedisService.beanToString(message);
//        System.out.println("send topic message: " + msg);
//        amqpTemplate.convertAndSend(MQConfig.TOPIC_EXCHANGE, "topic.key1", msg + "1");
//        amqpTemplate.convertAndSend(MQConfig.TOPIC_EXCHANGE, "topic.key2", msg + "2");
//    }
//
//    public void sendFanout(Object message) {
//        String msg = RedisService.beanToString(message);
//        System.out.println("send topic message: " + msg);
//        amqpTemplate.convertAndSend(MQConfig.FANOUT_EXCHANGE, "", msg + "1");
//    }
//
//    public void sendHeader(Object message) {
//        String msg = RedisService.beanToString(message);
//        System.out.println("send fanout message:" + msg);
//        org.springframework.amqp.core.MessageProperties properties = new org.springframework.amqp.core.MessageProperties();
//        properties.setHeader("header1", "value1");
//        properties.setHeader("header2", "value2");
//        Message obj = new Message(msg.getBytes(), properties);
//        amqpTemplate.convertAndSend(MQConfig.HEADERS_EXCHANGE, "", obj);
//    }

    public void sendSeckillMessage(SeckillMessage seckillMessage) {
        String msg = redisService.beanToString(seckillMessage);
        amqpTemplate.convertAndSend(MQConfig.SECKILL_QUEUE, msg);
    }
}
