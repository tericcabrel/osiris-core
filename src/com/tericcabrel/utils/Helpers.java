package com.tericcabrel.utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.File;
import java.io.IOException;

/**
 *
 * @author ZEGEEK
 */
public class Helpers {
    public static String byteArrayToString(byte[] byteArray) {
        StringBuffer hexStringBuffer = new StringBuffer();
        for (int i = 0; i < byteArray.length; i++) {
            hexStringBuffer.append((char) byteArray[i]);
        }
        return hexStringBuffer.toString();
    }
    
    public static byte[] numberStringToByteArray(String str) {
        if (str == null) {
            return new byte[]{ };
        }
        
        int strLength = str.length();
        byte[] bytes = new byte[strLength];
        
        for(int i = 0; i < strLength; i++) {
            bytes[i] = Integer.valueOf(str.charAt(i) + "", 10).byteValue();
        }
        
        return bytes;
    }

    public static String uploadFingerprint(String filePath) {
        CloseableHttpClient httpClient = HttpClients.createDefault();

        HttpPost post = new HttpPost("http://localhost:7000/api/fingerprints");
        File file = new File(filePath);
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        builder.addBinaryBody("file", file, ContentType.DEFAULT_BINARY, filePath);
        // builder.addTextBody("text", "message", ContentType.DEFAULT_BINARY);
//
        HttpEntity entity = builder.build();
        post.setEntity(entity);
        try {
            HttpResponse response = httpClient.execute(post);

            return "RES200";
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "RES500";
    }
}
