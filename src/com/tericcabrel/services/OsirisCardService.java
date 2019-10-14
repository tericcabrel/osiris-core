package com.tericcabrel.services;

import com.tericcabrel.services.interfaces.IOsirisCardService;
import com.tericcabrel.utils.Helpers;

import javax.smartcardio.*;
import java.util.HashMap;

public class OsirisCardService implements IOsirisCardService {
    /* Constants */
    private static byte[] APPLET_AID = { (byte) 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x00, 0x00 };
    public static final byte CLA_OSIRIS = (byte) 0x3A;

    private static final byte INS_GET_DATA = 0x00;
    private static final byte INS_SET_DATA = 0x01;
    private static final byte INS_SET_NAME = 0x02;
    private static final byte INS_SET_BIRTH_DATE = 0x03;
    private static final byte INS_RESET_DATA = 0x04;
    private final static byte INS_PIN_AUTH = (byte) 0x05;
    private final static byte INS_PIN_UNBLOCK = (byte) 0x06;

    private static final char DATA_DELIMITER = '|';

    // Signal that there is no error
    private final static String SW_SUCCESS_RESPONSE = "9000";

    // Signal that an unknown error occurred
    private final static String SW_INTERNAL_ERROR = "15000";
    // Signal that the PIN verification failed
    private final static String SW_VERIFICATION_FAILED = "25344";
    // Signal the PIN validation is required for an action
    private final static String SW_PIN_VERIFICATION_REQUIRED = "25345";

    private final HashMap<String, String> ERROR_MAPS = new HashMap<String, String>();

    private Card card;
    private int pinRemaining;
    private CardChannel channel;
    private boolean appletSelected;
    private boolean isAuthenticated;

    OsirisCardService(Card card) {
        ERROR_MAPS.put(SW_VERIFICATION_FAILED, "Authentication failed");
        ERROR_MAPS.put(SW_PIN_VERIFICATION_REQUIRED, "Authentication is required");

        this.card = card;
        pinRemaining = 3;
        appletSelected = false;
        isAuthenticated = false;

        this.channel = card.getBasicChannel();
    }

    @Override
    public String selectApplet() {
        // Send Select Applet command
        try {
            ResponseAPDU response = this.channel.transmit(new CommandAPDU(0x00, 0xA4, 0x04, 0x00, APPLET_AID));
            String responseString = Helpers.byteArrayToString(response.getData());

            if (responseString.equals(SW_SUCCESS_RESPONSE)) {
                this.appletSelected = true;
            }

            return responseString;
        } catch (CardException e) {
            e.printStackTrace();
        }

        return SW_INTERNAL_ERROR;
    }

    @Override
    public String authenticate(String pinCode) {
        try {
            byte[] data = Helpers.numberStringToByteArray(pinCode);

            ResponseAPDU response = this.channel.transmit(new CommandAPDU(0x00, INS_PIN_AUTH, 0x00, 0x00, data));
            String responseString = Helpers.byteArrayToString(response.getData());

            if (responseString.equals(SW_SUCCESS_RESPONSE)) {
                this.isAuthenticated = true;
                this.pinRemaining = 3;
            }

            return responseString;
        } catch (CardException e) {
            e.printStackTrace();
        }

        return SW_INTERNAL_ERROR;
    }

    @Override
    public String getData() {
        try {
            ResponseAPDU response = this.channel.transmit(new CommandAPDU(0x00, INS_GET_DATA, 0x00, 0x00));
            String responseString = Helpers.byteArrayToString(response.getData());

            return responseString;
        } catch (CardException e) {
            e.printStackTrace();
        }

        return SW_INTERNAL_ERROR;
    }

    @Override
    public String setData(String data) {
        try {
            byte[] params = Helpers.numberStringToByteArray(data);

            ResponseAPDU response = this.channel.transmit(new CommandAPDU(0x00, INS_SET_DATA, 0x00, 0x00, params));

            return Helpers.byteArrayToString(response.getData());
        } catch (CardException e) {
            e.printStackTrace();
        }

        return SW_INTERNAL_ERROR;
    }

    @Override
    public String setName(String data) {
        try {
            byte[] params = Helpers.numberStringToByteArray(data);

            ResponseAPDU response = this.channel.transmit(new CommandAPDU(0x00, INS_SET_NAME, 0x00, 0x00, params));

            return Helpers.byteArrayToString(response.getData());
        } catch (CardException e) {
            e.printStackTrace();
        }

        return SW_INTERNAL_ERROR;
    }

    @Override
    public String setBirthDate(String data) {
        try {
            byte[] params = Helpers.numberStringToByteArray(data);

            ResponseAPDU response = this.channel.transmit(new CommandAPDU(0x00, INS_SET_BIRTH_DATE, 0x00, 0x00, params));

            return Helpers.byteArrayToString(response.getData());
        } catch (CardException e) {
            e.printStackTrace();
        }

        return SW_INTERNAL_ERROR;
    }

    @Override
    public String resetData() {
        try {
            ResponseAPDU response = this.channel.transmit(new CommandAPDU(0x00, INS_RESET_DATA, 0x00, 0x00));

            return Helpers.byteArrayToString(response.getData());
        } catch (CardException e) {
            e.printStackTrace();
        }

        return SW_INTERNAL_ERROR;
    }

    @Override
    public String unblock() {
        try {
            ResponseAPDU response = this.channel.transmit(new CommandAPDU(0x00, INS_PIN_UNBLOCK, 0x00, 0x00));

            return Helpers.byteArrayToString(response.getData());
        } catch (CardException e) {
            e.printStackTrace();
        }

        return SW_INTERNAL_ERROR;
    }

    public void disconnect() {
        if (card != null) {
            try {
                card.disconnect(true);
            } catch (CardException e) {
                e.printStackTrace();
            }
        }
    }

    public Card getCard() {
        return card;
    }

    public OsirisCardService setCard(Card card) {
        this.card = card;
        return this;
    }

    public int getPinRemaining() {
        return pinRemaining;
    }

    public OsirisCardService setPinRemaining(int pinRemaining) {
        this.pinRemaining = pinRemaining;
        return this;
    }

    public CardChannel getChannel() {
        return channel;
    }

    public OsirisCardService setChannel(CardChannel channel) {
        this.channel = channel;
        return this;
    }

    public boolean isAppletSelected() {
        return appletSelected;
    }

    public OsirisCardService setAppletSelected(boolean appletSelected) {
        this.appletSelected = appletSelected;
        return this;
    }

    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    public OsirisCardService setAuthenticated(boolean authenticated) {
        isAuthenticated = authenticated;
        return this;
    }
}
