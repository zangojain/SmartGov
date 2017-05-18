package com.oracle.fintech.smartgov.model.lov;


public enum ExecutionTransactionType
{
  SHIPMENT("Shipment"),
  RECEIPT("Receipt"),
  CUSTOM_CLEARANCE("Custom Clearance");

  private String name;

  ExecutionTransactionType(String aa)
  {
    name = aa;
  }

  public String toString()
  {
    return name;
  }

}
