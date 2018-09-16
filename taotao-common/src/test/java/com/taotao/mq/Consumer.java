/*package com.taotao.mq;

import com.taotao.common.pojo.SearchResult;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.Test;

import javax.jms.*;

public class Consumer {

    @Test
    public void testQueueConsumer() throws JMSException, InterruptedException {
        //1.建立连接
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.1.124:61616");
        //2.获取连接
        Connection connection = connectionFactory.createConnection();
        //3.开启连接
        connection.start();
        //4.根据连接对象创建session
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //5.根据session创建Destination(目的地，Queue,Topic)
        Queue queue = session.createQueue("queue-test");
        //6.创建消费者
        MessageConsumer consumer = session.createConsumer(queue);
        //7.接收消息
        System.out.println("start");
        consumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                if(message instanceof TextMessage) {
                    TextMessage message1 = (TextMessage) message;
                    try{
                        String text = message1.getText();
                        System.out.println("获取的消息为："+text);
                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        System.out.println("end");
        Thread.sleep(10000);
        //8.关闭资源
        consumer.close();
        session.close();
        connection.close();
    }

    @Test
    public void testTopicConsumer() throws JMSException, InterruptedException {
        //1.建立连接
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.1.124:61616");
        //2.获取连接
        Connection connection = connectionFactory.createConnection();
        //3.开启连接
        connection.start();
        //4.根据连接对象创建session
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //5.根据session创建Destination(目的地，Queue,Topic)
        Topic topic = session.createTopic("topic-test");
        //6.创建消费者
        MessageConsumer consumer = session.createConsumer(topic);
        //7.接收消息
        //另外一种接收消息方式
        while(true) {
            //接收消息，单位为毫秒
            Message message = consumer.receive(100000);
            if(null == message) {
                break;
            }
            if(message instanceof TextMessage) {
                TextMessage message1 = (TextMessage) message;
                String text = message1.getText();
                System.out.println("获取的消息为:" + text);
            }
        }
        /*System.out.println("start");
        consumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                if(message instanceof TextMessage) {
                    TextMessage message1 = (TextMessage) message;
                    try{
                        String text = message1.getText();
                        System.out.println("获取的消息为："+text);
                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        System.out.println("end");
        Thread.sleep(10000);
        //8.关闭资源
        consumer.close();
        session.close();
        connection.close();
    }
}
*/