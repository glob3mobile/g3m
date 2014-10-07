package org.glob3.mobile.generated; 
public abstract class CartoCSSToken
{

  protected CartoCSSToken(CartoCSSTokenType type, int position)
  {
     _type = type;
     _position = position;
  }

  public final CartoCSSTokenType _type;
  public final int _position;

  public void dispose()
  {
  }

  public abstract String description();

  @Override
  public String toString() {
    return description();
  }
}