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

  protected RCObject() // the object starts retained
  {
     _referenceCounter = 1;

  }

  public void dispose()
  {

  }

  public final void _retain()
  {
    _referenceCounter++;
  }

  public final void _release()
  {
    if (--_referenceCounter == 0)
    {
      _suicide();
    }
  }

}