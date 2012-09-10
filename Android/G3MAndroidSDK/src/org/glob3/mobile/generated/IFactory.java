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

public abstract class IFactory
{
  private static IFactory _instance = null;

  public static void setInstance(IFactory logger)
  {
	if (_instance != null)
	{
	  System.out.print("Warning, ILooger instance set two times\n");
	}
	_instance = logger;
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
//ORIGINAL LINE: virtual IImage* createImageFromData(const ByteBuffer* buffer) const = 0;
  public abstract IImage createImageFromData(ByteBuffer buffer);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual IImage* createImageFromSize(int width, int height) const = 0;
  public abstract IImage createImageFromSize(int width, int height);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual void deleteImage(const IImage* image) const = 0;
  public abstract void deleteImage(IImage image);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual IFloatBuffer* createFloatBuffer(int size) const = 0;
  public abstract IFloatBuffer createFloatBuffer(int size);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual IIntBuffer* createIntBuffer(int size) const = 0;
  public abstract IIntBuffer createIntBuffer(int size);

}