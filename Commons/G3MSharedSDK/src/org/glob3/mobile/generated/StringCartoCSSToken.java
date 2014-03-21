package org.glob3.mobile.generated; 
public class StringCartoCSSToken extends CartoCSSToken
{
  private String _str;


  public StringCartoCSSToken(String str, int position)
  {
     super(CartoCSSTokenType.STRING, position);
     _str = str;
  }

  public final void appendString(String str)
  {
    _str = _str + str;
  }

  public final String str()
  {
    return _str;
  }

  public final String description()
  {
    IStringBuilder isb = IStringBuilder.newStringBuilder();
    isb.addString("[String \"");
    isb.addString(_str);
    isb.addString("\"]");
    final String s = isb.getString();
    if (isb != null)
       isb.dispose();
    return s;
  }
}