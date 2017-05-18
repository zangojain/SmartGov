package com.oracle.fintech.smartgov.model.dc;

import com.oracle.fintech.smartgov.api.common.LedgerJSONRequest;
import com.oracle.fintech.smartgov.model.util.SmartGovHttpClientUtil;

import java.io.IOException;

import java.util.Collections;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class OraLedgerServiceDC
{
  public OraLedgerServiceDC()
  {
    super();
  }

  public void callLedger(String methodName, List<String> methodParams)
    throws IOException, ClientProtocolException
  {
    HttpPost httpPost = SmartGovHttpClientUtil.getDefaultPostRequest();

    //    List<NameValuePair> nvps = new ArrayList<NameValuePair>();
    //    nvps.add(new BasicNameValuePair("username", SmartGovConstants.LEDGER_USER));
    //    nvps.add(new BasicNameValuePair("password", SmartGovConstants.LEDGER_PASS));

    //    httpPost.setEntity(new UrlEncodedFormEntity(nvps));


    LedgerJSONRequest instance = LedgerJSONRequest.getInstance("getblockchainparams");

    StringEntity requestParam = null;

    try
    {
      requestParam = new StringEntity(instance.getJSONRequest());
    }
    catch (JSONException e)
    {

    }

    httpPost.setEntity(requestParam);
    CloseableHttpResponse response = (CloseableHttpResponse) SmartGovHttpClientUtil.getHttpClient().execute(httpPost);

    try
    {
      StatusLine statusLine = response.getStatusLine();
      if (statusLine.getStatusCode() == 200)
      {
        //System.out.println(response.getStatusLine());
        HttpEntity entity = response.getEntity();
        String json = EntityUtils.toString(entity, "UTF-8");

        try
        {
          JSONObject jsonResult = new JSONObject(json);
          System.out.println(jsonResult);
          jsonResult = jsonResult.getJSONObject("result");
          System.out.println(jsonResult.getString("id"));
        }
        catch (JSONException e)
        {
          e.printStackTrace();
        }
        finally
        {
          EntityUtils.consume(entity);
        }
      }
      else
      {
        System.out.printf("Request failed with : %s", statusLine.getReasonPhrase());
      }
    }
    finally
    {
      response.close();
      //httpclient.getConnectionManager().shutdown();
    }
  }

  public static void main(String[] args)
  {
    OraLedgerServiceDC dc = new OraLedgerServiceDC();
    try
    {
      dc.callLedger("getinfo", Collections.emptyList());
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }

  public String getRequest()
  {
    return null;
  }
}
