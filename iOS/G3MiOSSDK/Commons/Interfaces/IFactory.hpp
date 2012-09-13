//
//  IFactory.h
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 31/05/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_IFactory_h
#define G3MiOSSDK_IFactory_h

#include "ILogger.hpp"

#ifdef C_CODE
#define GFactory (*IFactory::instance())
#else
#define GFactory IFactory.instance() //FOR JAVA CONVERTER
#endif

class ITimer;
class IImage;
class IFloatBuffer;
class IIntBuffer;
class IByteBuffer;
class ILogger;

class IFactory {
  static IFactory* _instance;
  
public:
  static void setInstance(IFactory* factory) {
    if (_instance != NULL) {
      printf("Warning, ILooger instance set two times\n");
    }
    _instance = factory;
  }
  
  static IFactory* instance() {
    return _instance;
  }

  virtual ~IFactory() {
    
  }
  
  virtual ITimer* createTimer() const = 0;
  
  virtual void deleteTimer(const ITimer* timer) const = 0;
  
  virtual IImage* createImageFromFileName(const std::string filename) const = 0;
  
  virtual IImage* createImageFromBuffer(const IByteBuffer* buffer) const = 0;
  
  virtual IImage* createImageFromSize(int width, int height) const = 0;

  virtual void deleteImage(const IImage* image) const = 0;
  
  virtual IFloatBuffer* createFloatBuffer(int size) const = 0;
  
  virtual IIntBuffer* createIntBuffer(int size) const = 0;
  
  virtual IByteBuffer* createByteBuffer(int length) const = 0;
  
  virtual IByteBuffer* createByteBuffer(unsigned char data[], int length) const = 0;
  
};

#endif
