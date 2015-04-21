package org.glob3.mobile.generated; 
public class CartoCSSResult
{
  private CartoCSSSymbolizer _symbolizer;
  private java.util.ArrayList<CartoCSSError> _errors = new java.util.ArrayList<CartoCSSError>();

  public CartoCSSResult(CartoCSSSymbolizer symbolizer)
  {
     _symbolizer = symbolizer;
  }

  public void dispose()
  {
  }

  public final CartoCSSSymbolizer getSymbolizer()
  {
    return _symbolizer;
  }

  public final void addError(CartoCSSError error)
  {
    _errors.add(error);
  }

  public final boolean hasError()
  {
    return !_errors.isEmpty();
  }

  public final java.util.ArrayList<CartoCSSError> getErrors()
  {
    return _errors;
  }

}