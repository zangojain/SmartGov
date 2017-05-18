package com.oracle.fintech.smartgov.model.beans;

import java.util.List;


public class ExecutionOrder
{
  private String orderExecutionId; //ref to exec order
  private String smartContractId; //ref smart contract
  private String salesOrderId; //ref from ERP
  private String customer;
  private String customerLocation;
  private String sellingLegalEntity;
  private String sellingMgmtEntity;
  private String item;
  private String uom;
  private int qty;
  private float unitPrice;
  private String currency;
  private String shippingLocation;
  private String shippingLegalEntity;
  private String shippingMgmtEntity;

  private List<ExecutionEvent> execEvents;

  public ExecutionOrder()
  {
    super();
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

  public void setCustomer(String customer)
  {
    this.customer = customer;
  }

  public String getCustomer()
  {
    return customer;
  }

  public void setCustomerLocation(String customerLocation)
  {
    this.customerLocation = customerLocation;
  }

  public String getCustomerLocation()
  {
    return customerLocation;
  }

  public void setSellingLegalEntity(String sellingLegalEntity)
  {
    this.sellingLegalEntity = sellingLegalEntity;
  }

  public String getSellingLegalEntity()
  {
    return sellingLegalEntity;
  }

  public void setSellingMgmtEntity(String sellingMgmtEntity)
  {
    this.sellingMgmtEntity = sellingMgmtEntity;
  }

  public String getSellingMgmtEntity()
  {
    return sellingMgmtEntity;
  }

  public void setItem(String item)
  {
    this.item = item;
  }

  public String getItem()
  {
    return item;
  }

  public void setUom(String uom)
  {
    this.uom = uom;
  }

  public String getUom()
  {
    return uom;
  }

  public void setQty(int qty)
  {
    this.qty = qty;
  }

  public int getQty()
  {
    return qty;
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

  public void setShippingLocation(String shippingLocation)
  {
    this.shippingLocation = shippingLocation;
  }

  public String getShippingLocation()
  {
    return shippingLocation;
  }

  public void setShippingLegalEntity(String shippingLegalEntity)
  {
    this.shippingLegalEntity = shippingLegalEntity;
  }

  public String getShippingLegalEntity()
  {
    return shippingLegalEntity;
  }

  public void setShippingMgmtEntity(String shippingMgmtEntity)
  {
    this.shippingMgmtEntity = shippingMgmtEntity;
  }

  public String getShippingMgmtEntity()
  {
    return shippingMgmtEntity;
  }
}
