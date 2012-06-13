package org.glob3.mobile.generated; 
//
//  IFactory.h
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 31/05/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//




//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class ITimer;


public abstract class IFactory
{
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual ITimer* createTimer() const = 0;
  public abstract ITimer createTimer();

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual void deleteTimer(ITimer* timer) const = 0;
  public abstract void deleteTimer(ITimer timer);

  // a virtual destructor is needed for conversion to Java
  public void dispose()
  {
  }
}