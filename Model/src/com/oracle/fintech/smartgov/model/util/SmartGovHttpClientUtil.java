package com.oracle.fintech.smartgov.model.util;

import com.oracle.fintech.smartgov.api.common.SmartGovConstants;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class SmartGovHttpClientUtil
{
  public SmartGovHttpClientUtil()
  {
    super();
  }

  public static HttpClient getHttpClient()
  {
    CredentialsProvider credsProvider = new BasicCredentialsProvider();
    credsProvider.setCredentials(new AuthScope(SmartGovConstants.LEDGER_REST_HOST, SmartGovConstants.LEDGER_REST_PORT),
                                 new UsernamePasswordCredentials(SmartGovConstants.LEDGER_USER,
                                                                 SmartGovConstants.LEDGER_PASS));
    CloseableHttpClient httpclient = HttpClients.custom()
                                                .setDefaultCredentialsProvider(credsProvider)
                                                .build();
    return httpclient;
  }

  public static HttpPost getDefaultPostRequest()
  {
    HttpPost httpPost = new HttpPost(SmartGovConstants.LEDGER_REST_URL);
    httpPost.addHeader("content-type", "application/json");
    httpPost.addHeader("accept", "application/json");
    return httpPost;
  }
}
