package com.tericcabrel;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import opencard.core.service.CardServiceException;
import opencard.core.terminal.CardTerminalException;
import opencard.core.util.OpenCardPropertyLoadingException;

import javax.smartcardio.*;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class Main {
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

        // Initialize card Service
        new CardService();

        /*try {
            TerminalFactory terminalFactory = TerminalFactory.getDefault();
            List<CardTerminal> cardTerminals = terminalFactory.terminals().list();
            if (cardTerminals.isEmpty()) {
                System.out.println("Skipping the test: no card terminals available");
                return;
            }

            System.out.println("Terminals: " + cardTerminals);

            CardTerminal cardTerminal = cardTerminals.get(0);
            Card card = null;

            boolean present = cardTerminal.isCardPresent();

            if (present) {
                card = cardTerminal.connect("T=1");

                System.out.println("card: " + card);
                CardChannel channel = card.getBasicChannel();

                // Send Select Applet command
                byte[] aid = { (byte) 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x00, 0x00 };
                ResponseAPDU answer = channel.transmit(new CommandAPDU(0x00, 0xA4, 0x04, 0x00, aid));
                System.out.println("answer: " + answer.toString());


                ResponseAPDU res = channel.transmit(new CommandAPDU(0x3A, 0x00, 0x00, 0x00));
                String hex = DatatypeConverter.printHexBinary(res.getBytes());
                System.out.println("RES SW: " + res.getSW1());
                System.out.println("Response: " + res.getData().length);
                System.out.println("Data: " + Utils.byteArrayToString(res.getData()));

                card.disconnect(true);
            } else {
                System.out.println("The card is not present!");
            }
        } catch (CardException e) {
            e.printStackTrace();
        }*/
    }
}