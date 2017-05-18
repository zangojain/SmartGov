package com.oracle.fintech.smartgov.model.beans;

import java.util.List;


public class SmartGovAsset
{
  private SmartContract smartContract;
  private List<ExecutionOrder> execOrder;
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

//  public void setExecOrder(ExecutionOrder execOrder)
//  {
//    this.execOrder = execOrder;
//  }
//
//  public ExecutionOrder getExecOrder()
//  {
//    return execOrder;
//  }
}
