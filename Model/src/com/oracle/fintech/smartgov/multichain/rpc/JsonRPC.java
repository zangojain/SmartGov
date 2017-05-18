/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oracle.fintech.smartgov.multichain.rpc;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.ParseException;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 *
 * @author saugupta
 */
public class JsonRPC {

    //public static String HOST = "192.168.0.38"; // 10.242.91.209:7325
    public static String HOST = "10.242.91.209";
    public static Integer PORT = 7324;
    public static String USER_NAME = "rahul";
    public static String PASSWORD = "Welcome1";
    public static String REMOTE_HOST = "http://" + HOST + ":" + PORT;

    public static String doPost(String payloadJSON) throws UnsupportedEncodingException, IOException {
        String postResponse = "error";
        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(
                new AuthScope(HOST, PORT),
                new UsernamePasswordCredentials(USER_NAME, PASSWORD));

        CloseableHttpClient httpclient = HttpClients.custom()
                .setDefaultCredentialsProvider(credsProvider)
                .build();
        try {
            HttpPost postcall = new HttpPost(REMOTE_HOST);

            System.out.println("Executing request " + postcall.getRequestLine());
//            if (payloadJSON == null) {
//                payloadJSON = "{\"method\":\"getinfo\"}";
//            }
            StringEntity se = null;
            se = new StringEntity(payloadJSON);
            postcall.setEntity(se);

            CloseableHttpResponse response = null;
            response = httpclient.execute(postcall);
            System.out.println("----------------------------------------");
            System.out.println(response.getStatusLine());
            //System.out.println(EntityUtils.toString(response.getEntity()));
            postResponse = EntityUtils.toString(response.getEntity());
        } catch (Exception e) {
            System.out.println(e);
        } finally {

        }
        return postResponse;
    }
}
// JsonRPC.doPost(CHAIN + " " + command.toString().toLowerCase() + " " + params);

