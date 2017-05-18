package com.oracle.fintech.smartgov.model.dc;

import com.google.gson.Gson;

import com.oracle.fintech.smartgov.api.common.LedgerJSONRequest;
import com.oracle.fintech.smartgov.model.beans.SmartContract;
import com.oracle.fintech.smartgov.model.util.SmartGovHttpClientUtil;
import com.oracle.fintech.smartgov.multichain.MultichainException;
import com.oracle.fintech.smartgov.multichain.MultichainQueryBuilder;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;


public class IssueCommand
{
  public IssueCommand()
  {
    super();
  }

  public String callIssueCommand(String payload)
  {
    String assetId = null;
    HttpPost httpPost = SmartGovHttpClientUtil.getDefaultPostRequest();
    LedgerJSONRequest instance = LedgerJSONRequest.getInstance("issue");

    StringEntity requestParam = null;
    CloseableHttpResponse response = null;
    try
    {
      requestParam = new StringEntity(instance.getJSONRequest());
      httpPost.setEntity(requestParam);
      response = (CloseableHttpResponse) SmartGovHttpClientUtil.getHttpClient().execute(httpPost);
      StatusLine statusLine = response.getStatusLine();
      if (statusLine.getStatusCode() == 200)
      {
        //System.out.println(response.getStatusLine());
        HttpEntity entity = response.getEntity();
        String json = EntityUtils.toString(entity, "UTF-8");

        JSONObject jsonResult = new JSONObject(json);
        System.out.println(jsonResult);
        jsonResult = jsonResult.getJSONObject("result");
        System.out.println(jsonResult);
        EntityUtils.consume(entity);
      }
    }
    catch (JSONException e)
    {

    }
    catch (UnsupportedEncodingException e)
    {

    }
    catch (IOException e)
    {
    }
    finally
    {
      if (response != null)
      {
        //response.close();
      }
      //httpclient.getConnectionManager().shutdown();
    }


    return assetId;
  }

  public static String issue(String assetName, int qty, SmartContract asset)
  {
    String result = null;
    String address = "19e9HGFVZG5eFjjKZmit7EXZfuXzETcX91CDga";
    boolean open = true;
    int quantity = qty;
    float unit = 2;
    Gson gson = new Gson();
    String details = gson.toJson(asset);
    //String details = "{\"contract:"
    try
    {
      result = MultichainQueryBuilder.executeIssue(address, assetName, open, quantity, unit, details);
    }
    catch (MultichainException e)
    {
      e.printStackTrace();
    }
    return result;
  }
}
