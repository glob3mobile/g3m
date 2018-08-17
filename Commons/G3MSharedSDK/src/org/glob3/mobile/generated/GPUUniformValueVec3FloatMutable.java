package org.glob3.mobile.generated;import java.util.*;

public class GPUUniformValueVec3FloatMutable extends GPUUniformValueVec3Float
{
  public void dispose()
  {
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	super.dispose();
//#endif
  }


  public GPUUniformValueVec3FloatMutable(float x, float y, float z)
  {
	  super(x,y,z);
  }

  public final void changeValue(float x, float y, float z)
  {
	_x = x;
	_y = y;
	_z = z;
  }
}
