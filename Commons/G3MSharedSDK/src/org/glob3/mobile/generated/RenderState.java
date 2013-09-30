package org.glob3.mobile.generated; 
public class RenderState
{

  public static RenderState ready()
  {
    return new RenderState(RenderState_Type.RENDER_READY);
  }
  public static RenderState busy()
  {
    return new RenderState(RenderState_Type.RENDER_BUSY);
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

  public RenderState(RenderState that)
  {
     _type = that._type;
     _errors = that._errors;

  }

  public void dispose()
  {

  }

  private final java.util.ArrayList<String> _errors;

  private RenderState(RenderState_Type type)
  {
     _type = type;
    _errors = null;
  }

  private RenderState(java.util.ArrayList<String> errors)
  {
     _type = RenderState_Type.RENDER_ERROR;
     _errors = errors;
  }



}