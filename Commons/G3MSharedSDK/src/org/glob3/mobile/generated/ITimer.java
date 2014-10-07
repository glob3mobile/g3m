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
  public abstract TimeInterval now();

  public abstract long nowInMilliseconds();

  public abstract void start();

  public abstract TimeInterval elapsedTime();

  public abstract long elapsedTimeInMilliseconds();

  public void dispose()
  {
  }

}