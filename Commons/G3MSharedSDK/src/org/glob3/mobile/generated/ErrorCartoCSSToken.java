package org.glob3.mobile.generated; 
public class ErrorCartoCSSToken extends CartoCSSToken
{
  public final String _description;

  public ErrorCartoCSSToken(String description, int position)
  {
     super(CartoCSSTokenType.ERROR, position);
     _description = description;
  }

  public final String description()
  {
    IStringBuilder isb = IStringBuilder.newStringBuilder();
    isb.addString("[Error description=\"");
    isb.addString(_description);
    isb.addString("\" at ");
    isb.addInt(_position);
    isb.addString("]");
    final String s = isb.getString();
    if (isb != null)
       isb.dispose();
    return s;
  }
}