package com.tericcabrel;

import opencard.core.event.CTListener;
import opencard.core.event.CardTerminalEvent;
import opencard.core.service.CardServiceException;
import opencard.core.service.SmartCard;
import opencard.core.terminal.CardTerminalException;
import opencard.core.terminal.CardTerminalRegistry;
import opencard.core.util.OpenCardPropertyLoadingException;

public class CardService implements CTListener {
    SmartCard card = null;

    CardService() {
        try {
            SmartCard.start();
            CardTerminalRegistry.getRegistry().addCTListener(this);
            CardTerminalRegistry.getRegistry().createEventsForPresentCards(this);

            System.out.println("Card Service initialized !");
        } catch (OpenCardPropertyLoadingException | ClassNotFoundException | CardServiceException | CardTerminalException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    @Override
    public void cardInserted(CardTerminalEvent cardTerminalEvent) throws CardTerminalException {
        System.out.println("Card inserted!");
    }

    @Override
    public void cardRemoved(CardTerminalEvent cardTerminalEvent) throws CardTerminalException {
        System.out.println("Card removed");
    }
}