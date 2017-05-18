package com.oracle.fintech.smartgov.model.beans;


public class SmartGovAsset
{
  private String address;
  private int qty;
  private float amount;
  private SmartContract smartContract;
  private ExecutionOrder execOrder;

  public SmartGovAsset()
  {
    super();
  }

  public void setSmartContract(SmartContract smartContract)
  {
    this.smartContract = smartContract;
  }

  public SmartContract getSmartContract()
  {
    return smartContract;
  }

  public void setExecOrder(ExecutionOrder execOrder)
  {
    this.execOrder = execOrder;
  }

  public ExecutionOrder getExecOrder()
  {
    return execOrder;
  }
}
