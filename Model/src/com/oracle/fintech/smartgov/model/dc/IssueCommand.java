package com.oracle.fintech.smartgov.model.dc;

import com.oracle.fintech.smartgov.api.common.LedgerJSONRequest;
import com.oracle.fintech.smartgov.model.beans.SmartGovAsset;
import com.oracle.fintech.smartgov.model.util.SmartGovHttpClientUtil;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;

import org.codehaus.jettison.json.JSONException;


public class IssueCommand
{
  public IssueCommand()
  {
    super();
  }

  public String callIssueCommand(String payload)
  {
    HttpPost httpPost = SmartGovHttpClientUtil.getDefaultPostRequest();
    LedgerJSONRequest instance = LedgerJSONRequest.getInstance("issue");

    StringEntity requestParam = null;

    try
    {
      requestParam = new StringEntity(instance.getJSONRequest());
      httpPost.setEntity(requestParam);
      CloseableHttpResponse response = (CloseableHttpResponse) SmartGovHttpClientUtil.getHttpClient().execute(httpPost);
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


  }

  public static String issue(String assetName, float qty, SmartGovAsset smartGovAsset)
  {
    if ()
  }
}
