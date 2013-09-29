package org.glob3.mobile.generated; 
public class RenderState
{

  public static RenderState ready()
  {
    return new RenderState(RenderState_Type.READY);
  }
  public static RenderState busy()
  {
    return new RenderState(RenderState_Type.BUSY);
  }
  public static RenderState error(java.util.ArrayList<String> errors)
  {
    return new RenderState(errors);
  }

  public final RenderState_Type _type;

  public final java.util.ArrayList<String> getErrors()
  {
    return _errors;
  }

  private final java.util.ArrayList<String> _errors = new java.util.ArrayList<String>();

  private RenderState(RenderState_Type type)
  {
     _type = type;
  }

  private RenderState(java.util.ArrayList<String> errors)
  {
     _type = CartoCSSTokenType.ERROR;
     _errors = errors;
  }

}