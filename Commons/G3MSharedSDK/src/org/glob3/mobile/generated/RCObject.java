package org.glob3.mobile.generated;import java.util.*;

//
//  RCObject.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 17/08/12.
//
//

//
//  RCObject.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 17/08/12.
//
//



public class RCObject
{
  private int _referenceCounter;

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void _suicide() const
  private void _suicide()
  {
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
	this = null;
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	this.dispose();
//#endif
  }

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  RCObject(RCObject that);
//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  RCObject operator =(RCObject that);

  protected RCObject() // the object starts retained
  {
	  _referenceCounter = 1;
  }


  ///#include "ILogger.hpp"
  
  public void dispose()
  {
	if (_referenceCounter != 0)
	{
  //    ILogger::instance()->logError("DELETING RCOBJECT WITH UNRELEASED REFERENCES!");
	  THROW_EXCEPTION("Deleted RCObject with unreleased references!");
	}
  }


//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void _retain() const
  public final void _retain()
  {
	_referenceCounter++;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean _release() const
  public final boolean _release()
  {
	if (--_referenceCounter == 0)
	{
	  _suicide();
	  return true;
	}
	return false;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: int _getReferenceCounter() const
  public final int _getReferenceCounter()
  {
	return _referenceCounter;
  }

}
