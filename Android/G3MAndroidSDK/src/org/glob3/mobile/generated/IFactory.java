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
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IImage;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class INetwork;

public abstract class IFactory
{
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual ITimer* createTimer() const = 0;
  public abstract ITimer createTimer();

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual void deleteTimer(const ITimer* timer) const = 0;
  public abstract void deleteTimer(ITimer timer);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual IImage* createImageFromFileName(const String filename) const = 0;
  public abstract IImage createImageFromFileName(String filename);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual IImage* createImageFromData(const ByteBuffer& bb) const = 0;
  public abstract IImage createImageFromData(ByteBuffer bb);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual void deleteImage(const IImage* image) const = 0;
  public abstract void deleteImage(IImage image);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual INetwork* createNetwork() const = 0;
  public abstract INetwork createNetwork();

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual void deletenetwork(const INetwork* image) const = 0;
  public abstract void deletenetwork(INetwork image);

  // a virtual destructor is needed for conversion to Java
  public void dispose()
  {
  }
}