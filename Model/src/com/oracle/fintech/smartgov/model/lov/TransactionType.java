package com.oracle.fintech.smartgov.model.lov;


public enum TransactionType
{
  IN_TRANSIT("In transit receipt"),
  IN_TRANSIT_ISSUE("In transit issue"),
  PAYABLES_INVOICE("Payable Invoice"),
  RECEIVABLE_INVOICE("Receivable Invoice");

  private String name;

  TransactionType(String aa)
  {
    name = aa;
  }

  public String toString()
  {
    return name;
  }

}
