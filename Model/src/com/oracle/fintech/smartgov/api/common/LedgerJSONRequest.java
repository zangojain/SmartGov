package com.oracle.fintech.smartgov.api.common;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;


public class LedgerJSONRequest
{
  //{"jsonrpc": "1.0", "id":"infot1", "method": "getinfo", "params": [] }

  private String jsonrpc = "1.0";
  private String id;
  private String method;
  private List<Object> params;

  public LedgerJSONRequest()
  {
    super();
  }

  private LedgerJSONRequest(String methodName)
  {
    super();
    method = methodName;
  }

  private LedgerJSONRequest(String methodName, Object param)
  {
    super();
    method = methodName;
    params = new ArrayList<>(1);
    params.add(param);
  }

  public static final LedgerJSONRequest getInstance(String methodName)
  {
    LedgerJSONRequest obj = new LedgerJSONRequest(methodName);
    return obj;
  }

  public static final LedgerJSONRequest getInstance(String methodName, String param)
  {
    LedgerJSONRequest obj = new LedgerJSONRequest(methodName);
    return obj;
  }


  public String getJsonrpc()
  {
    return jsonrpc;
  }

  public void setId(String id)
  {
    this.id = id;
  }

  public String getId()
  {
    if (id == null)
    {
      id = UUID.randomUUID().toString();
    }
    return id;
  }

  public void setMethod(String method)
  {
    this.method = method;
  }

  public String getMethod()
  {
    return method;
  }

  public void setParams(List<Object> params)
  {
    this.params = params;
  }

  public List<Object> getParams()
  {
    return params;
  }

  public void addParam(String param)
  {
    if (params == null || params.isEmpty())
    {
      params = new ArrayList<>();
    }
    params.add(param);
  }

  public String getJSONRequest()
    throws JSONException
  {
    JSONObject requestJSON = new JSONObject();
    requestJSON.put("jsonrpc", getJsonrpc());
    requestJSON.put("id", getId());
    requestJSON.put("method", getMethod());
    requestJSON.put("params", getParams());
    String json = requestJSON.toString();
    return json;
  }
}
