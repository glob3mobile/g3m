//
//  Factory_iOS.hpp
//  G3MiOSSDK
//
//  Created by Agustin Trujillo Pino on 31/05/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_Factory_iOS
#define G3MiOSSDK_Factory_iOS

#include "IFactory.hpp"

#include "Timer_iOS.hpp"
#include "Image_iOS.hpp"
#include "ByteBuffer_iOS.hpp"
#include "FloatBuffer_iOS.hpp"
#include "IntBuffer_iOS.hpp"
#include "ShortBuffer_iOS.hpp"
#include "IImageListener.hpp"
#include "Canvas_iOS.hpp"
#include "WebSocket_iOS.hpp"
#import "NSString_CppAdditions.h"
#include "DeviceInfo_iOS.hpp"

class Factory_iOS: public IFactory {
protected:
  IDeviceInfo* createDeviceInfo() const {
    return new DeviceInfo_iOS();
  }

public:

  ITimer* createTimer() const {
    return new Timer_iOS();
  }

  void deleteTimer(const ITimer* timer) const {
    delete timer;
  }

  void deleteImage(const IImage* image) const {
    delete image;
  }

  IByteBuffer* createByteBuffer(unsigned char data[], int length) const{
    return new ByteBuffer_iOS(data, length);
  }

  IByteBuffer* createByteBuffer(int size) const{
    return new ByteBuffer_iOS(size);
  }

  IFloatBuffer* createFloatBuffer(int size) const {
    return new FloatBuffer_iOS(size);
  }

  IFloatBuffer* createFloatBuffer(float f0,
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
                                  float f15) const {
    return new FloatBuffer_iOS(f0,
                               f1,
                               f2,
                               f3,
                               f4,
                               f5,
                               f6,
                               f7,
                               f8,
                               f9,
                               f10,
                               f11,
                               f12,
                               f13,
                               f14,
                               f15);
  }

  IIntBuffer* createIntBuffer(int size) const {
    return new IntBuffer_iOS(size);
  }


  IShortBuffer* createShortBuffer(int size) const {
    return new ShortBuffer_iOS(size);
  }

  ICanvas* createCanvas() const {
    return new Canvas_iOS();
  }

  IWebSocket* createWebSocket(const URL& url,
                              IWebSocketListener* listener,
                              bool autodeleteListener,
                              bool autodeleteWebSocket) const {
    return new WebSocket_iOS(url,
                             listener,
                             autodeleteListener,
                             autodeleteWebSocket);
  }

};

#endif
