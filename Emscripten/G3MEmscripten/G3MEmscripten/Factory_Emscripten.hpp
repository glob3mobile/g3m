
#ifndef Factory_Emscripten_hpp
#define Factory_Emscripten_hpp

#include "G3M/IFactory.hpp"


class Factory_Emscripten : public IFactory {
protected:
  IDeviceInfo* createDeviceInfo() const;

public:

  ITimer* createTimer() const;

  IByteBuffer* createByteBuffer(size_t size) const;

  IFloatBuffer* createFloatBuffer(size_t size) const;

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
                                  float f15) const;

  IIntBuffer* createIntBuffer(size_t size) const;

  IShortBuffer* createShortBuffer(size_t size) const;

  ICanvas* createCanvas(bool retina) const;

  ICanvas* createCanvas(bool retina,
                        const int maxSize) const;

  IWebSocket* createWebSocket(const URL& url,
                              IWebSocketListener* listener,
                              bool autodeleteListener,
                              bool autodeleteWebSocket) const;

};

#endif
