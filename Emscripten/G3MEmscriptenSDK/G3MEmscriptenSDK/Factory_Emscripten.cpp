

#include "Factory_Emscripten.hpp"

#include "DeviceInfo_Emscripten.hpp"
#include "Timer_Emscripten.hpp"
//#include "ByteBuffer_Emscripten.hpp"
//#include "FloatBuffer_Emscripten.hpp"
//#include "Image_Emscripten.hpp"
//#include "IntBuffer_Emscripten.hpp"
//#include "ShortBuffer_Emscripten.hpp"
//#include "IImageListener.hpp"
//#include "Canvas_Emscripten.hpp"
//#include "WebSocket_Emscripten.hpp"


IDeviceInfo* Factory_Emscripten::createDeviceInfo() const {
  return new DeviceInfo_Emscripten();
}

ITimer* Factory_Emscripten::createTimer() const {
  return new Timer_Emscripten();
}

IByteBuffer* Factory_Emscripten::createByteBuffer(unsigned char data[], size_t length) const {
  return new ByteBuffer_Emscripten(data, length);
}

IByteBuffer* Factory_Emscripten::createByteBuffer(size_t size) const {
  return new ByteBuffer_Emscripten(size);
}

IFloatBuffer* Factory_Emscripten::createFloatBuffer(size_t size) const {
  return new FloatBuffer_Emscripten(size);
}

IFloatBuffer* Factory_Emscripten::createFloatBuffer(float f0,
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
  return new FloatBuffer_Emscripten(f0,
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

IIntBuffer* Factory_Emscripten::createIntBuffer(size_t size) const {
  return new IntBuffer_Emscripten(size);
}


IShortBuffer* Factory_Emscripten::createShortBuffer(size_t size) const {
  return new ShortBuffer_Emscripten(size);
}

ICanvas* Factory_Emscripten::createCanvas(bool retina) const {
  return new Canvas_Emscripten(retina);
}

IWebSocket* Factory_Emscripten::createWebSocket(const URL& url,
                                                IWebSocketListener* listener,
                                                bool autodeleteListener,
                                                bool autodeleteWebSocket) const {
  return new WebSocket_Emscripten(url,
                                  listener,
                                  autodeleteListener,
                                  autodeleteWebSocket);
}
