package com.oracle.fintech.smartgov.model.beans;

import com.oracle.fintech.smartgov.api.common.SmartGovBaseObjInterface;
import com.oracle.fintech.smartgov.model.lov.FlowType;

import java.util.List;

public class SmartContract implements SmartGovBaseObjInterface
{
  private String smartContractId; //ref smart contract
  private String sfoAgreementId;
  private String sellingPartyName;
  private String buyingPartyName;
  private String contractName;
  private String startDate;
  private String endDate;
  private String item;
  private String flowType = FlowType.DROPSHIP.toString();
  private List<TradePath> tradePaths;
  private List<ExecutionOrder> orders;

  public SmartContract()
  {
    super();
  }

  public void setSfoAgreementId(String sfoAgreementId)
  {
    this.sfoAgreementId = sfoAgreementId;
  }

  public String getSfoAgreementId()
  {
    return sfoAgreementId;
  }

  public void setSellingPartyName(String sellingPartyName)
  {
    this.sellingPartyName = sellingPartyName;
  }

  public String getSellingPartyName()
  {
    return sellingPartyName;
  }

  public void setBuyingPartyName(String buyingPartyName)
  {
    this.buyingPartyName = buyingPartyName;
  }

  public String getBuyingPartyName()
  {
    return buyingPartyName;
  }

  public void setContractName(String contractName)
  {
    this.contractName = contractName;
  }

  public String getContractName()
  {
    return contractName;
  }

  public void setStartDate(String startDate)
  {
    this.startDate = startDate;
  }

  public String getStartDate()
  {
    return startDate;
  }

  public void setEndDate(String endDate)
  {
    this.endDate = endDate;
  }

  public String getEndDate()
  {
    return endDate;
  }

  public void setItem(String item)
  {
    this.item = item;
  }

  public String getItem()
  {
    return item;
  }

  public void setTradePaths(List<TradePath> tradePaths)
  {
    this.tradePaths = tradePaths;
  }

  public List<TradePath> getTradePaths()
  {
    return tradePaths;
  }

  public void setFlowType(String flowType)
  {
    this.flowType = flowType;
  }

  public String getFlowType()
  {
    return flowType;
  }

  public void setOrders(List<ExecutionOrder> orders)
  {
    this.orders = orders;
  }

  public List<ExecutionOrder> getOrders()
  {
    return orders;
  }
}
