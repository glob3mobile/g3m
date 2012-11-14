package org.glob3.mobile.generated; 
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


///#include <string>

public class RCObject
{
  private int _referenceCounter;

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void _suicide() const
  private void _suicide()
  {
  }

  protected RCObject() // the object starts retained
  {
	  _referenceCounter = 1;

  }

  public void dispose()
  {

  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void _retain() const
  public final void _retain()
  {
	_referenceCounter++;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void _release() const
  public final void _release()
  {
	if (--_referenceCounter == 0)
	{
	  _suicide();
	}
  }

//  virtual const std::string description() const = 0;

}