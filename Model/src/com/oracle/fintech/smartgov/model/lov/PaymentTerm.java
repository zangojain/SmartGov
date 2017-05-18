package com.oracle.fintech.smartgov.model.lov;


public enum PaymentTerm
{
    NETTING("Netting"),
    NET_30("Net in 30 Days");

    private String name;
    PaymentTerm(String nm)
    {
      this.name = nm;
    }

    @Override
    public String toString()
    {
      return name;
    }
}
