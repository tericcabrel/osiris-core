package com.tericcabrel.services;

import com.tericcabrel.utils.Helpers;

import javax.smartcardio.*;
import java.util.HashMap;

public class OsirisCardService {
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

    public static final String DATA_DELIMITER = "|";

    // Signal that there is no error
    public final static String SW_SUCCESS_RESPONSE = "36864";

    // Signal that the applet selected successfully
    public final static String SW_APPLET_SELECTED = "36865";

    // Signal that the card was removed
    public final static String SW_CARD_REMOVED = "14000";

    // Signal that an unknown error occurred
    private final static String SW_INTERNAL_ERROR = "15000";
    // Signal that the PIN verification failed
    private final static String SW_VERIFICATION_FAILED = "25344";
    // Signal the PIN validation is required for an action
    private final static String SW_PIN_VERIFICATION_REQUIRED = "25345";

    private static Card card;
    private static int pinRemaining = 3;
    private static boolean appletSelected = false;
    private static boolean isAuthenticated = false;

    public static String selectApplet() {
        // Send Select Applet command
        try {
            ResponseAPDU response = card.getBasicChannel().transmit(new CommandAPDU(0x00, 0xA4, 0x04, 0x00, APPLET_AID));

            if (String.valueOf(response.getSW()).equals(SW_SUCCESS_RESPONSE)) {
                appletSelected = true;
                return SW_APPLET_SELECTED;
            }

            return String.valueOf(response.getSW());
        } catch (CardException e) {
            e.printStackTrace();
        }

        return SW_INTERNAL_ERROR;
    }

    public static String authenticate(String pinCode) {
        try {
            byte[] data = Helpers.numberStringToByteArray(pinCode);

            ResponseAPDU response = card.getBasicChannel().transmit(new CommandAPDU(CLA_OSIRIS, INS_PIN_AUTH, 0x00, 0x00, data));

            if (String.valueOf(response.getSW()).equals(SW_SUCCESS_RESPONSE)) {
                isAuthenticated = true;
                pinRemaining = 3;
            }

            return String.valueOf(response.getSW());
        } catch (CardException e) {
            e.printStackTrace();
        }

        return SW_INTERNAL_ERROR;
    }

    public static String getData() {
        try {
            ResponseAPDU response = card.getBasicChannel().transmit(new CommandAPDU(CLA_OSIRIS, INS_GET_DATA, 0x00, 0x00));
            if (String.valueOf(response.getSW()).equals(SW_SUCCESS_RESPONSE)) {
                return Helpers.byteArrayToString(response.getData());
            }

            return String.valueOf(response.getSW());
        } catch (CardException e) {
            e.printStackTrace();
        }

        return SW_INTERNAL_ERROR;
    }

    public static String setData(String data) {
        try {
            byte[] params = data.getBytes();

            ResponseAPDU response = card.getBasicChannel().transmit(new CommandAPDU(CLA_OSIRIS, INS_SET_DATA, 0x00, 0x00, params));

            return String.valueOf(response.getSW());
        } catch (CardException e) {
            e.printStackTrace();
        }

        return SW_INTERNAL_ERROR;
    }

    public static String setName(String data) {
        try {
            byte[] params = data.getBytes();

            ResponseAPDU response = card.getBasicChannel().transmit(new CommandAPDU(CLA_OSIRIS, INS_SET_NAME, 0x00, 0x00, params));

            return String.valueOf(response.getSW());
        } catch (CardException e) {
            e.printStackTrace();
        }

        return SW_INTERNAL_ERROR;
    }

    public static String setBirthDate(String data) {
        try {
            byte[] params = data.getBytes();

            ResponseAPDU response = card.getBasicChannel().transmit(new CommandAPDU(CLA_OSIRIS, INS_SET_BIRTH_DATE, 0x00, 0x00, params));

            return String.valueOf(response.getSW());
        } catch (CardException e) {
            e.printStackTrace();
        }

        return SW_INTERNAL_ERROR;
    }

    public static String resetData() {
        try {
            ResponseAPDU response = card.getBasicChannel().transmit(new CommandAPDU(CLA_OSIRIS, INS_RESET_DATA, 0x00, 0x00));

            return String.valueOf(response.getSW());
        } catch (CardException e) {
            e.printStackTrace();
        }

        return SW_INTERNAL_ERROR;
    }

    public static String unblock() {
        try {
            ResponseAPDU response = card.getBasicChannel().transmit(new CommandAPDU(CLA_OSIRIS, INS_PIN_UNBLOCK, 0x00, 0x00));

            return String.valueOf(response.getSW());
        } catch (CardException e) {
            e.printStackTrace();
        }

        return SW_INTERNAL_ERROR;
    }

    public static void disconnect() {
        if (card != null) {
            try {
                card.disconnect(true);
            } catch (CardException e) {
                e.printStackTrace();
            }
        }
    }

    public static Card getCard() {
        return card;
    }

    public static void setCard(Card newCard) {
        card = newCard;
    }

    public int getPinRemaining() {
        return pinRemaining;
    }

    public void setPinRemaining(int pinRemaining) {
        OsirisCardService.pinRemaining = pinRemaining;
    }

    public boolean isAppletSelected() {
        return appletSelected;
    }

    public void setAppletSelected(boolean appletSelected) {
        OsirisCardService.appletSelected = appletSelected;
    }

    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        isAuthenticated = authenticated;
    }
}
