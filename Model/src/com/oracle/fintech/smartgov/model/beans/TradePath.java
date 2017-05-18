package com.oracle.fintech.smartgov.model.beans;

import com.oracle.fintech.smartgov.model.lov.IncoTerms;
import com.oracle.fintech.smartgov.model.lov.PaymentTerm;


public class TradePath
{

  private String sfoAgreementId; //ID from SFO.
  private String tradeId;
  private String tradeSeller;
  private String tradeBuyer;
  private String incoTerm = IncoTerms.FOB.toString();
  private String payterms = PaymentTerm.NETTING.toString();
  private String pricing;

  public TradePath()
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

  public void setTradeId(String tradeId)
  {
    this.tradeId = tradeId;
  }

  public String getTradeId()
  {
    return tradeId;
  }

  public void setTradeSeller(String tradeSeller)
  {
    this.tradeSeller = tradeSeller;
  }

  public String getTradeSeller()
  {
    return tradeSeller;
  }

  public void setTradeBuyer(String tradeBuyer)
  {
    this.tradeBuyer = tradeBuyer;
  }

  public String getTradeBuyer()
  {
    return tradeBuyer;
  }

  public void setIncoTerm(String incoTerm)
  {
    this.incoTerm = incoTerm;
  }

  public String getIncoTerm()
  {
    return incoTerm;
  }

  public void setPayterms(String payterms)
  {
    this.payterms = payterms;
  }

  public String getPayterms()
  {
    return payterms;
  }

  public void setPricing(String pricing)
  {
    this.pricing = pricing;
  }

  public String getPricing()
  {
    return pricing;
  }
}
