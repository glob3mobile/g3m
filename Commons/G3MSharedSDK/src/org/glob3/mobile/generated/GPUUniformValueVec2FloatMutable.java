package org.glob3.mobile.generated;import java.util.*;

public class GPUUniformValueVec2FloatMutable extends GPUUniformValueVec2Float
{
  public void dispose()
  {
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	super.dispose();
//#endif
  }


  public GPUUniformValueVec2FloatMutable(float x, float y)
  {
	  super(x,y);
  }

  public final void changeValue(float x, float y)
  {
	_x = x;
	_y = y;
  }
}
