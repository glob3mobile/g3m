//
//  Factory_iOS.hpp
//  G3MiOSSDK
//
//  Created by Agustin Trujillo Pino on 31/05/12.
//

#ifndef G3MiOSSDK_Factory_iOS
#define G3MiOSSDK_Factory_iOS

#include "G3M/IFactory.hpp"
#include "G3M/IImageListener.hpp"

#import "Timer_iOS.hpp"
#import "Image_iOS.hpp"
#import "ByteBuffer_iOS.hpp"
#import "FloatBuffer_iOS.hpp"
#import "IntBuffer_iOS.hpp"
#import "ShortBuffer_iOS.hpp"
#import "Canvas_iOS.hpp"
#import "WebSocket_iOS.hpp"
#import "DeviceInfo_iOS.hpp"

#import "NSString_CppAdditions.h"


class Factory_iOS: public IFactory {
protected:
  IDeviceInfo* createDeviceInfo() const {
    return new DeviceInfo_iOS();
  }

public:

  ITimer* createTimer() const {
    return new Timer_iOS();
  }

  IByteBuffer* createByteBuffer(size_t size) const {
    return new ByteBuffer_iOS(size);
  }

  IFloatBuffer* createFloatBuffer(size_t size) const {
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

  IIntBuffer* createIntBuffer(size_t size) const {
    return new IntBuffer_iOS(size);
  }


  IShortBuffer* createShortBuffer(size_t size) const {
    return new ShortBuffer_iOS(size);
  }

  ICanvas* createCanvas(bool retina) const {
    return new Canvas_iOS(retina, -1);
  }

  ICanvas* createCanvas(bool retina,
                        const int maxSize) const {
    return new Canvas_iOS(retina, maxSize);
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
