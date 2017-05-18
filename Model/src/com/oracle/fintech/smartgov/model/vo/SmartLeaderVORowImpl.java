package com.oracle.fintech.smartgov.model.vo;

import java.util.HashMap;

import oracle.jbo.server.ProgrammaticViewRowImpl;
import oracle.jbo.server.ViewAttributeDefImpl;
// ---------------------------------------------------------------------
// ---    File generated by Oracle ADF Business Components Design Time.
// ---    Wed May 10 21:15:14 PDT 2017
// ---    Custom code may be added to this class.
// ---    Warning: Do not modify method signatures of generated methods.
// ---------------------------------------------------------------------
public class SmartLeaderVORowImpl
  extends ProgrammaticViewRowImpl
{
  /**
   * createRowData - for custom java data source support.
   * Overridden to initialize the dataProvier for newly created row.
   * Used for updateable View Objects.
   */
  public Object createRowData(HashMap attrNameValueMap)
  {
    Object value = super.createRowData(attrNameValueMap);
    return value;
  }

  /**
   * convertToSourceType - for custom java data source support.
   * Overridden to provide custom implementation for conversions of a value
   * from attribute java type to datasource column/field type.
   * Not required in most cases.
   */
  public Object convertToSourceType(ViewAttributeDefImpl attrDef, String sourceType, Object val)
  {
    Object value = super.convertToSourceType(attrDef, sourceType, val);
    return value;
  }

  /**
   * convertToAttributeType - for custom java data source support.
   * Overridden to provide custom implementation for conversions of a value
   *  from datasource/column field type to attribute java type.
   * Not required in most cases.
   */
  public Object convertToAttributeType(ViewAttributeDefImpl attrDef, Class javaTypeClass, Object val)
  {
    Object value = super.convertToAttributeType(attrDef, javaTypeClass, val);
    return value;
  }

  /**
   * AttributesEnum: generated enum for identifying attributes and accessors. DO NOT MODIFY.
   */
  protected enum AttributesEnum
  {
    sellerId,
    sellarName,
    buyerId,
    buyerName,
    id,
    contractAmount;
    private static AttributesEnum[] vals = null;
    private static final int firstIndex = 0;

    protected int index()
    {
      return AttributesEnum.firstIndex() + ordinal();
    }

    protected static final int firstIndex()
    {
      return firstIndex;
    }

    protected static int count()
    {
      return AttributesEnum.firstIndex() + AttributesEnum.staticValues().length;
    }

    protected static final AttributesEnum[] staticValues()
    {
      if (vals == null)
      {
        vals = AttributesEnum.values();
      }
      return vals;
    }
  }

  public static final int SELLERID = AttributesEnum.sellerId.index();
  public static final int SELLARNAME = AttributesEnum.sellarName.index();
  public static final int BUYERID = AttributesEnum.buyerId.index();
  public static final int BUYERNAME = AttributesEnum.buyerName.index();
  public static final int ID = AttributesEnum.id.index();
  public static final int CONTRACTAMOUNT = AttributesEnum.contractAmount.index();

  /**
   * This is the default constructor (do not remove).
   */
  public SmartLeaderVORowImpl()
  {
  }
}
