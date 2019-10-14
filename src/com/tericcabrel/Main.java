package com.tericcabrel;

import javax.smartcardio.*;
import javax.xml.bind.DatatypeConverter;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
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
        }
    }
}

/*
* try {
			// show the list of available terminals
			TerminalFactory factory = TerminalFactory.getDefault();
			List<CardTerminal> terminals = factory.terminals().list();
			// get the first terminal
			if (terminals.isEmpty()) {
				System.out.println("No terminals found!");
			} else {
				System.out.println("Terminals: " + terminals);
				CardTerminal terminal = terminals.get(0);
				// establish a connection with the card
				// Card card = terminal.connect("T=1");
				Card card = terminal.connect("DIRECT");
				System.out.println("card: " + card);
				byte[] ccidResp = card.transmitControlCommand(
						CM_IOCTL_GET_FEATURE_REQUEST, new byte[] {});
				System.out.println(HexString.bufferToHex(ccidResp));
				CardChannel channel = card.getBasicChannel();
//				channel.transmit(new CommandAPDU(new byte[]{0,1,2,3,4,5,6}));
				// disconnect
				card.disconnect(false);
			}
		} catch (Exception e) {
			System.err.println(e.getLocalizedMessage());
		}
* */