package org.glob3.mobile.generated; 
//
//  ITimer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 12/06/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//



public abstract class ITimer
{
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual TimeInterval now() const = 0;
  public abstract TimeInterval now();

  public abstract void start();

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual TimeInterval elapsedTime() const = 0;
  public abstract TimeInterval elapsedTime();

  public void dispose()
  {
  }

}