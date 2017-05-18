package com.oracle.fintech.smartgov.model.lov;


public enum FlowType
{
  DROPSHIP("Dropship"),
  PROCUREMENT("Procurement"),
  TRANSER("Transfer");

  private String name;
  FlowType(String nm)
  {
    this.name = nm;
  }

  @Override
  public String toString()
  {
    return name;
  }
}
