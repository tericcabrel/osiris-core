package com.tericcabrel.services;

public class MyCardService {
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
