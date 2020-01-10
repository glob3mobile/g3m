package org.glob3.mobile.generated;
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



//class IDeviceInfo;
//class ITimer;
//class IFloatBuffer;
//class IIntBuffer;
//class IShortBuffer;
//class IByteBuffer;
//class ICanvas;
//class IWebSocket;
//class URL;
//class IWebSocketListener;


public abstract class IFactory
{
  private static IFactory _instance = null;

  private IDeviceInfo _deviceInfo;

  protected abstract IDeviceInfo createDeviceInfo();

  protected IFactory()
  {
     _deviceInfo = null;
  
  }

  public static void setInstance(IFactory factory)
  {
    if (_instance != null)
    {
  //    ILogger::instance()->logWarning("IFactory instance already set!");
      if (_instance != null)
         _instance.dispose();
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

  public abstract ITimer createTimer();

  public abstract IFloatBuffer createFloatBuffer(int size);

  /* special factory method for creating floatbuffers from matrix */
  public abstract IFloatBuffer createFloatBuffer(float f0, float f1, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, float f12, float f13, float f14, float f15);

  public abstract IIntBuffer createIntBuffer(int size);

  public abstract IShortBuffer createShortBuffer(int size);

  public abstract IByteBuffer createByteBuffer(int length);

  public abstract ICanvas createCanvas(boolean retina);

  public abstract IWebSocket createWebSocket(URL url, IWebSocketListener listener, boolean autodeleteListener, boolean autodeleteWebSocket);

  public final IDeviceInfo getDeviceInfo()
  {
    if (_deviceInfo == null)
    {
      _deviceInfo = createDeviceInfo();
    }
    return _deviceInfo;
  }


  public abstract IShortBuffer createShortBuffer(final short[] array, final int length);
  public abstract IFloatBuffer createFloatBuffer(final float[] array, final int length);

}
