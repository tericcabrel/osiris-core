package com.tericcabrel;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import com.tericcabrel.services.OsirisCardService;
import com.tericcabrel.utils.CardHelper;
import com.tericcabrel.utils.Messaging;

import javax.smartcardio.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeoutException;

public class Main {
    private static Channel channel;

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

            channel = connection.createChannel();
        }catch (TimeoutException | IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        new Timer().scheduleAtFixedRate(new TimerTask(){
            @Override
            public void run() {
                Card newCard = CardHelper.getCard();

                if (OsirisCardService.getCard() == null && newCard != null) {
                    System.out.println("Card inserted");

                    OsirisCardService.setCard(newCard);

                    String message = OsirisCardService.selectApplet();

                    Messaging.sendToQueue(channel, Messaging.Q_APPLET_SELECTED_RESPONSE, message);
                } else if (OsirisCardService.getCard() != null && newCard == null) {
                    System.out.println("Card removed");

                    OsirisCardService.setCard(null);

                    Messaging.sendToQueue(channel, Messaging.Q_CARD_REMOVED_RESPONSE, OsirisCardService.SW_CARD_REMOVED);
                }
            }
        },0,1000);

        try {
            channel.queueDeclare(Messaging.Q_AUTHENTICATE_REQUEST, false, false, false, null);
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                System.out.println(" [x] Received '" + message + "'");
                String response = OsirisCardService.authenticate(message);
                Messaging.sendToQueue(channel, Messaging.Q_AUTHENTICATE_RESPONSE, response);
            };
            channel.basicConsume(Messaging.Q_AUTHENTICATE_REQUEST, true, deliverCallback, consumerTag -> { });

            channel.queueDeclare(Messaging.Q_GET_DATA_REQUEST, false, false, false, null);
            DeliverCallback deliverCallback1 = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                String response = OsirisCardService.getData();
                Messaging.sendToQueue(channel, Messaging.Q_GET_DATA_RESPONSE, response);
            };
            channel.basicConsume(Messaging.Q_GET_DATA_REQUEST, true, deliverCallback1, consumerTag -> { });

            channel.queueDeclare(Messaging.Q_SET_DATA_REQUEST, false, false, false, null);
            DeliverCallback deliverCallback2 = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                System.out.println(" [x] Received '" + message + "'");
                String response = OsirisCardService.setData(message);
                Messaging.sendToQueue(channel, Messaging.Q_SET_DATA_RESPONSE, response);
            };
            channel.basicConsume(Messaging.Q_SET_DATA_REQUEST, true, deliverCallback2, consumerTag -> { });

            channel.queueDeclare(Messaging.Q_SET_NAME_REQUEST, false, false, false, null);
            DeliverCallback deliverCallback3 = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                System.out.println(" [x] Received '" + message + "'");
                String response = OsirisCardService.setName(message);
                Messaging.sendToQueue(channel, Messaging.Q_SET_NAME_RESPONSE, response);
            };
            channel.basicConsume(Messaging.Q_SET_NAME_REQUEST, true, deliverCallback3, consumerTag -> { });

            channel.queueDeclare(Messaging.Q_SET_BIRTH_DATE_REQUEST, false, false, false, null);
            DeliverCallback deliverCallback4 = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                System.out.println(" [x] Received '" + message + "'");
                String response = OsirisCardService.setBirthDate(message);
                Messaging.sendToQueue(channel, Messaging.Q_SET_BIRTH_DATE_RESPONSE, response);
            };
            channel.basicConsume(Messaging.Q_SET_BIRTH_DATE_REQUEST, true, deliverCallback4, consumerTag -> { });

            channel.queueDeclare(Messaging.Q_RESET_REQUEST, false, false, false, null);
            DeliverCallback deliverCallback5 = (consumerTag, delivery) -> {
                String response = OsirisCardService.resetData();
                Messaging.sendToQueue(channel, Messaging.Q_RESET_RESPONSE, response);
            };
            channel.basicConsume(Messaging.Q_RESET_REQUEST, true, deliverCallback5, consumerTag -> { });

            channel.queueDeclare(Messaging.Q_UNBLOCK_REQUEST, false, false, false, null);
            DeliverCallback deliverCallback6 = (consumerTag, delivery) -> {
                String response = OsirisCardService.unblock();
                Messaging.sendToQueue(channel, Messaging.Q_UNBLOCK_RESPONSE, response);
            };
            channel.basicConsume(Messaging.Q_UNBLOCK_REQUEST, true, deliverCallback6, consumerTag -> { });

            channel.queueDeclare(Messaging.Q_DISCONNECT_REQUEST, false, false, false, null);
            DeliverCallback deliverCallback7 = (consumerTag, delivery) -> {
                OsirisCardService.disconnect();
                Messaging.sendToQueue(channel, Messaging.Q_DISCONNECT_RESPONSE, "OK");
            };
            channel.basicConsume(Messaging.Q_DISCONNECT_REQUEST, true, deliverCallback7, consumerTag -> { });

            channel.queueDeclare(Messaging.Q_ENROLL_REQUEST, false, false, false, null);
            DeliverCallback deliverCallback8 = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                System.out.println(" [x] Received '" + message + "'"); // contains user's uid
                Messaging.sendToQueue(channel, Messaging.Q_GET_FINGERPRINT_REQUEST, message);
            };
            channel.basicConsume(Messaging.Q_ENROLL_REQUEST, true, deliverCallback8, consumerTag -> { });

            channel.queueDeclare(Messaging.Q_GET_FINGERPRINT_RESPONSE, false, false, false, null);
            DeliverCallback deliverCallback9 = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                System.out.println(" [x] Received '" + message + "'"); // contains value indicate if enrollment succeeded
                Messaging.sendToQueue(channel, Messaging.Q_ENROLL_RESPONSE, message);
            };
            channel.basicConsume(Messaging.Q_GET_FINGERPRINT_RESPONSE, true, deliverCallback9, consumerTag -> { });

            channel.queueDeclare(Messaging.Q_VERIFY_USER_REQUEST, false, false, false, null);
            DeliverCallback deliverCallback10 = (consumerTag, delivery) -> {
                String info = OsirisCardService.getData();
                String[] array = info.split(OsirisCardService.DATA_DELIMITER);
                Messaging.sendToQueue(channel, Messaging.Q_MATCH_MATCH_FINGERPRINT_REQUEST, array.length > 0 ? array[0] : "");
            };
            channel.basicConsume(Messaging.Q_VERIFY_USER_REQUEST, true, deliverCallback10, consumerTag -> { });

            channel.queueDeclare(Messaging.Q_MATCH_FINGERPRINT_RESPONSE, false, false, false, null);
            DeliverCallback deliverCallback11 = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                System.out.println(" [x] Received '" + message + "'"); // contains value indicate if verification succeeded
                Messaging.sendToQueue(channel, Messaging.Q_VERIFY_USER_RESPONSE, message);
            };
            channel.basicConsume(Messaging.Q_MATCH_FINGERPRINT_RESPONSE, true, deliverCallback11, consumerTag -> { });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}