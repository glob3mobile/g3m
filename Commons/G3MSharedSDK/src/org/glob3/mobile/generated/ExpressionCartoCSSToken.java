package org.glob3.mobile.generated; 
public class ExpressionCartoCSSToken extends CartoCSSToken
{
  public final String _source;

  public ExpressionCartoCSSToken(String source, int position)
  {
     super(CartoCSSTokenType.EXPRESION, position);
     _source = source;
  }

  public final String description()
  {
    IStringBuilder isb = IStringBuilder.newStringBuilder();
    isb.addString("[Expression source=\"");
    isb.addString(_source);
    isb.addString("\"]");
    final String s = isb.getString();
    if (isb != null)
       isb.dispose();
    return s;
  }
}