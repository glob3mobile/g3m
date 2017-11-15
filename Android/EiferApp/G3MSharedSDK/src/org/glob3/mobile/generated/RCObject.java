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



public class RCObject
{
  private int _referenceCounter;

  private void _suicide()
  {
    this.dispose();
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
      throw new RuntimeException("Deleted RCObject with unreleased references!");
    }
  }


  public final void _retain()
  {
    _referenceCounter++;
  }

  public final boolean _release()
  {
    if (--_referenceCounter == 0)
    {
      _suicide();
      return true;
    }
    return false;
  }

  public final int _getReferenceCounter()
  {
    return _referenceCounter;
  }

}