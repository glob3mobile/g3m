//
//  IFactory.hpp
//  G3MiOSSDK
//
//  Created by Agustin Trujillo Pino on 31/05/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_IFactory
#define G3MiOSSDK_IFactory

#include "ILogger.hpp"

class ITimer;
class IImage;
class IFloatBuffer;
class IIntBuffer;
class IShortBuffer;
class IByteBuffer;
class ILogger;
class IImageListener;
class ICanvas;
class IWebSocket;
class IWebSocketListener;
class URL;
class IDeviceInfo;

class IFactory {
private:
  static IFactory*     _instance;

  mutable IDeviceInfo* _deviceInfo;

protected:
  virtual IDeviceInfo* createDeviceInfo() const = 0;

public:
  static void setInstance(IFactory* factory) {
    if (_instance != NULL) {
      ILogger::instance()->logWarning("IFactory instance already set!");
      delete _instance;
    }
    _instance = factory;
  }

  static IFactory* instance() {
    return _instance;
  }

  IFactory() :
  _deviceInfo(NULL)
  {

  }

  virtual ~IFactory() {
  }

  virtual void deleteImage(const IImage* image) const = 0;

  virtual ITimer* createTimer() const = 0;

  virtual void deleteTimer(const ITimer* timer) const = 0;

  virtual IFloatBuffer* createFloatBuffer(int size) const = 0;

  /* special factory method for creating floatbuffers from matrix */
  virtual IFloatBuffer* createFloatBuffer(float f0,
                                          float f1,
                                          float f2,
                                          float f3,
                                          float f4,
                                          float f5,
                                          float f6,
                                          float f7,
                                          float f8,
                                          float f9,
                                          float f10,
                                          float f11,
                                          float f12,
                                          float f13,
                                          float f14,
                                          float f15) const = 0;

  virtual IIntBuffer* createIntBuffer(int size) const = 0;

  virtual IShortBuffer* createShortBuffer(int size) const = 0;

  virtual IByteBuffer* createByteBuffer(int length) const = 0;

  virtual IByteBuffer* createByteBuffer(unsigned char data[], int length) const = 0;

  virtual ICanvas* createCanvas() const = 0;

  virtual IWebSocket* createWebSocket(const URL& url,
                                      IWebSocketListener* listener,
                                      bool autodeleteListener,
                                      bool autodeleteWebSocket) const = 0;

  const IDeviceInfo* getDeviceInfo() const;


#ifdef JAVA_CODE

  public abstract IShortBuffer createShortBuffer(final short[] array, final int length);
  public abstract IFloatBuffer createFloatBuffer(final float[] array, final int length);

//  public abstract float[] getThreadLocalFloatArray();
//  public abstract void    setThreadLocalFloatArray(final float[] array);
#endif
  
};

#endif
