package com.oracle.fintech.smartgov.model.lov;


public enum IncoTerms
{
  FOB("Free on Board"),
  EXW("Ex Works"),
  DAT("Delivered at Terminal");

  private String name;
  IncoTerms(String nm)
  {
    this.name = nm;
  }

  @Override
  public String toString()
  {
    return name;
  }
}
