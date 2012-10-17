package org.glob3.mobile.generated; 
//
//  IFactory.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 06/09/12.
//
//

//
//  IFactory.h
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 31/05/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//



//C++ TO JAVA CONVERTER NOTE: The following #define macro was replaced in-line:
//#define GFactory IFactory.instance()

//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class ITimer;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IImage;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IFloatBuffer;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IIntBuffer;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IByteBuffer;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class ILogger;

public abstract class IFactory
{
  private static IFactory _instance = null;

  public static void setInstance(IFactory factory)
  {
	if (_instance != null)
	{
	  ILogger.instance().logWarning("Warning, ILooger instance set two times\n");
	}
	_instance = factory;
  }

  public static IFactory instance()
  {
	return _instance;
  }

  public void dispose()
  {

  }

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
//ORIGINAL LINE: virtual IImage* createImageFromBuffer(const IByteBuffer* buffer) const = 0;
  public abstract IImage createImageFromBuffer(IByteBuffer buffer);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual IImage* createImageFromSize(int width, int height) const = 0;
  public abstract IImage createImageFromSize(int width, int height);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual void deleteImage(const IImage* image) const = 0;
  public abstract void deleteImage(IImage image);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual IFloatBuffer* createFloatBuffer(int size) const = 0;
  public abstract IFloatBuffer createFloatBuffer(int size);

  /* special factory method for creating floatbuffers from matrix */
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual IFloatBuffer* createFloatBuffer(float f0, float f1, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, float f12, float f13, float f14, float f15) const = 0;
  public abstract IFloatBuffer createFloatBuffer(float f0, float f1, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, float f12, float f13, float f14, float f15);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual IIntBuffer* createIntBuffer(int size) const = 0;
  public abstract IIntBuffer createIntBuffer(int size);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual IByteBuffer* createByteBuffer(int length) const = 0;
  public abstract IByteBuffer createByteBuffer(int length);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual IByteBuffer* createByteBuffer(byte data[], int length) const = 0;
  public abstract IByteBuffer createByteBuffer(byte[] data, int length);

}