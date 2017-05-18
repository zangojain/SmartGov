package com.oracle.fintech.smartgov.model.beans;

import com.oracle.fintech.smartgov.model.lov.IncoTerms;
import com.oracle.fintech.smartgov.model.lov.PaymentTerm;
import com.oracle.fintech.smartgov.model.lov.TransactionType;


public class SmartGovLedger
{
  private String txnId;
  private String txnType = TransactionType.RECEIVABLE_INVOICE.toString();
  private String txnDate;
  private String txnLegalEntity;
  private String txnMgmtEntity;
  private String eventTxnId;
  private String orderExecutionId; //ref to exec order
  private String smartContractId; //ref smart contract
  private int qty;
  private String uom;
  private float unitPrice;
  private String currency;
  private float amt;
  private String paymentTerm = PaymentTerm.NET_30.toString();
  private String incoTerm = IncoTerms.FOB.toString();

  public SmartGovLedger()
  {
    super();
  }


  public void setTxnId(String txnId)
  {
    this.txnId = txnId;
  }

  public String getTxnId()
  {
    return txnId;
  }

  public void setTxnType(String txnType)
  {
    this.txnType = txnType;
  }

  public String getTxnType()
  {
    return txnType;
  }

  public void setTxnDate(String txnDate)
  {
    this.txnDate = txnDate;
  }

  public String getTxnDate()
  {
    return txnDate;
  }

  public void setTxnLegalEntity(String txnLegalEntity)
  {
    this.txnLegalEntity = txnLegalEntity;
  }

  public String getTxnLegalEntity()
  {
    return txnLegalEntity;
  }

  public void setTxnMgmtEntity(String txnMgmtEntity)
  {
    this.txnMgmtEntity = txnMgmtEntity;
  }

  public String getTxnMgmtEntity()
  {
    return txnMgmtEntity;
  }

  public void setEventTxnId(String eventTxnId)
  {
    this.eventTxnId = eventTxnId;
  }

  public String getEventTxnId()
  {
    return eventTxnId;
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

  public void setQty(int qty)
  {
    this.qty = qty;
  }

  public int getQty()
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

  public void setUnitPrice(float unitPrice)
  {
    this.unitPrice = unitPrice;
  }

  public float getUnitPrice()
  {
    return unitPrice;
  }

  public void setCurrency(String currency)
  {
    this.currency = currency;
  }

  public String getCurrency()
  {
    return currency;
  }

  public void setAmt(float amt)
  {
    this.amt = amt;
  }

  public float getAmt()
  {
    return amt;
  }

  public void setPaymentTerm(String paymentTerm)
  {
    this.paymentTerm = paymentTerm;
  }

  public String getPaymentTerm()
  {
    return paymentTerm;
  }

  public void setIncoTerm(String incoTerm)
  {
    this.incoTerm = incoTerm;
  }

  public String getIncoTerm()
  {
    return incoTerm;
  }
}
