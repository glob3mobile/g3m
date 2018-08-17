package org.glob3.mobile.generated;//
//  ITimer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 12/06/12.
//



public abstract class ITimer
{
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual TimeInterval now() const = 0;
  public abstract TimeInterval now();

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual long nowInMilliseconds() const = 0;
  public abstract long nowInMilliseconds();

  public abstract void start();

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual TimeInterval elapsedTime() const = 0;
  public abstract TimeInterval elapsedTime();

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual long elapsedTimeInMilliseconds() const = 0;
  public abstract long elapsedTimeInMilliseconds();

  public void dispose()
  {
  }

}
