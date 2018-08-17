package org.glob3.mobile.generated;import java.util.*;

//
//  IFactory.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 06/09/12.
//
//

//
//  IFactory.hpp
//  G3MiOSSDK
//
//  Created by Agustin Trujillo Pino on 31/05/12.
//



//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class ITimer;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IImage;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IFloatBuffer;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IIntBuffer;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IShortBuffer;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IByteBuffer;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class ILogger;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IImageListener;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class ICanvas;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IWebSocket;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IWebSocketListener;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class URL;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IDeviceInfo;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IXMLNode;

public abstract class IFactory
{
  private static IFactory _instance = null;

  private IDeviceInfo _deviceInfo;

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual IDeviceInfo* createDeviceInfo() const = 0;
  protected abstract IDeviceInfo createDeviceInfo();

  public static void setInstance(IFactory factory)
  {
	if (_instance != null)
	{
	  ILogger.instance().logWarning("IFactory instance already set!");
	  if (_instance != null)
		  _instance.dispose();
	}
	_instance = factory;
  }

  public static IFactory instance()
  {
	return _instance;
  }

  public IFactory()
  {
	  _deviceInfo = null;

  }

  public void dispose()
  {
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual ITimer* createTimer() const = 0;
  public abstract ITimer createTimer();

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
//ORIGINAL LINE: virtual IShortBuffer* createShortBuffer(int size) const = 0;
  public abstract IShortBuffer createShortBuffer(int size);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual IByteBuffer* createByteBuffer(int length) const = 0;
  public abstract IByteBuffer createByteBuffer(int length);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual IByteBuffer* createByteBuffer(byte data[], int length) const = 0;
  public abstract IByteBuffer createByteBuffer(byte[] data, int length);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual ICanvas* createCanvas(boolean retina) const = 0;
  public abstract ICanvas createCanvas(boolean retina);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual IWebSocket* createWebSocket(const URL& url, IWebSocketListener* listener, boolean autodeleteListener, boolean autodeleteWebSocket) const = 0;
  public abstract IWebSocket createWebSocket(URL url, IWebSocketListener listener, boolean autodeleteListener, boolean autodeleteWebSocket);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const IDeviceInfo* getDeviceInfo() const
  public final IDeviceInfo getDeviceInfo()
  {
	if (_deviceInfo == null)
	{
	  _deviceInfo = createDeviceInfo();
	}
	return _deviceInfo;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual IXMLNode* createXMLNodeFromXML(const String& xmlText) const = 0;
  public abstract IXMLNode createXMLNodeFromXML(String xmlText);


//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
  public abstract public  IShortBuffer createShortBuffer(final short[] array, final int length);
  public abstract public  IFloatBuffer createFloatBuffer(final float[] array, final int length);
//#endif

}
