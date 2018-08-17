package org.glob3.mobile.generated;import java.util.*;

//
//  GPUUniform.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 16/05/13.
//
//

//
//  GPUUniform.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 05/04/13.
//
//





//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class GPUUniform;

public abstract class GPUUniformValue extends RCObject
{

  public void dispose()
  {
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	super.dispose();
//#endif
  }

  public final int _type;

  public GPUUniformValue(int type)
  {
	  _type = type;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual void setUniform(GL* gl, const IGLUniformID* id) const = 0;
  public abstract void setUniform(GL gl, IGLUniformID id);
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual boolean isEquals(const GPUUniformValue* v) const = 0;
  public abstract boolean isEquals(GPUUniformValue v);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual String description() const = 0;
  public abstract String description();
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
  public final Override public String toString()
  {
	return description();
  }
//#endif

}
