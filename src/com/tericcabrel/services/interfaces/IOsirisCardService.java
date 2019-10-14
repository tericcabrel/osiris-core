package com.tericcabrel.services.interfaces;

public interface IOsirisCardService {
    String selectApplet();
    String authenticate(String pinCode);
    String getData();
    String setData(String data);
    String setName(String data);
    String setBirthDate(String data);
    String resetData();
    String unblock();
    void disconnect();
}
