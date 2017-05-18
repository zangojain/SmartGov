package com.oracle.fintech.smartgov.model.beans;

import com.oracle.fintech.smartgov.model.lov.ExecutionTransactionType;

import java.util.List;

public class ExecutionEvent
{
  //Block chain record for financial execution
  private String eventTxnId;
  private String eventDate;
  private String orderExecutionId; //ref to exec order
  private String smartContractId; //ref smart contract
  private String salesOrderId; //ref from ERP
  private String transactionType = ExecutionTransactionType.SHIPMENT.toString();
  private String eventDocId; //

  private String item;
  private String qty;
  private String uom;
  private String reportingParty;

  private List<SmartGovLedger> transactions;

  public void setEventTxnId(String eventTxnId)
  {
    this.eventTxnId = eventTxnId;
  }

  public String getEventTxnId()
  {
    return eventTxnId;
  }

  public void setEventDate(String eventDate)
  {
    this.eventDate = eventDate;
  }

  public String getEventDate()
  {
    return eventDate;
  }

  public void setOrderExecutionId(String orderExecutionId)
  {
    this.orderExecutionId = orderExecutionId;
  }

  public String getOrderExecutionId()
  {
    return orderExecutionId;
  }

  public void setSmartContractId(String smartContractId)
  {
    this.smartContractId = smartContractId;
  }

  public String getSmartContractId()
  {
    return smartContractId;
  }

  public void setSalesOrderId(String salesOrderId)
  {
    this.salesOrderId = salesOrderId;
  }

  public String getSalesOrderId()
  {
    return salesOrderId;
  }

  public void setTransactionType(String transactionType)
  {
    this.transactionType = transactionType;
  }

  public String getTransactionType()
  {
    return transactionType;
  }

  public void setEventDocId(String eventDocId)
  {
    this.eventDocId = eventDocId;
  }

  public String getEventDocId()
  {
    return eventDocId;
  }

  public void setItem(String item)
  {
    this.item = item;
  }

  public String getItem()
  {
    return item;
  }

  public void setQty(String qty)
  {
    this.qty = qty;
  }

  public String getQty()
  {
    return qty;
  }

  public void setUom(String uom)
  {
    this.uom = uom;
  }

  public String getUom()
  {
    return uom;
  }

  public void setReportingParty(String reportingParty)
  {
    this.reportingParty = reportingParty;
  }

  public String getReportingParty()
  {
    return reportingParty;
  }
}
