package com.tericcabrel;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.tericcabrel.utils.CardHelper;

import javax.smartcardio.*;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeoutException;

public class Main {
    private static Card card= null;

    public static void main(String[] args) {
        // Connection to RabbitMQ
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setPort(5672);
        factory.setUsername("guest");
        factory.setPassword("guest");

        try {
            Connection connection = factory.newConnection();
            System.out.println(" Connected successfully to RabbitMQ Server");

            Channel channel = connection.createChannel();

            /*channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            String message = "Hello World!";
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes("UTF-8"));
            System.out.println(" [x] Sent '" + message + "'");*/
        }catch (TimeoutException | IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        new Timer().scheduleAtFixedRate(new TimerTask(){
            @Override
            public void run(){
                Card newCard = CardHelper.getCard();

                if (card == null && newCard != null) {
                    card = newCard;
                    System.out.println("Card inserted");
                } else if (card != null && newCard == null){
                    card = null;
                    System.out.println("Card removed");
                }
            }
        },0,1000);
    }
}