package org.glob3.mobile.generated;import java.util.*;

public class RenderState
{
  private static final RenderState READY = new RenderState(RenderState_Type.RENDER_READY);
  private static final RenderState BUSY = new RenderState(RenderState_Type.RENDER_BUSY);

  public static RenderState ready()
  {
	return READY;
  }
  public static RenderState busy()
  {
	return BUSY;
  }
  public static RenderState error(java.util.ArrayList<String> errors)
  {
	return new RenderState(errors);
  }
  public static RenderState error(String error)
  {
	java.util.ArrayList<String> errors = new java.util.ArrayList<String>();
	errors.add(error);
	return new RenderState(errors);
  }

  public final RenderState_Type _type;

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const java.util.ArrayList<String> getErrors() const
  public final java.util.ArrayList<String> getErrors()
  {
	return _errors;
  }

  public RenderState(RenderState that)
  {
	  _type = that._type;
	  _errors = that._errors;
  }

//  RenderState& operator= (const RenderState& that) {
//    if (this != &that) {
//      _type = that._type;
//      _errors = that._errors;
//    }
//    return *this;
//  }

  public void dispose()
  {

  }

//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
  private final java.util.ArrayList<String> _errors = new java.util.ArrayList<String>();
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
  public final java.util.ArrayList<String> _errors = new internal();
//#endif

  private RenderState(RenderState_Type type)
  {
	  _type = type;
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	_errors = null;
//#endif
  }

  private RenderState(java.util.ArrayList<String> errors)
  {
	  _type = RenderState_Type.RENDER_ERROR;
	  _errors = errors;
  }



}
