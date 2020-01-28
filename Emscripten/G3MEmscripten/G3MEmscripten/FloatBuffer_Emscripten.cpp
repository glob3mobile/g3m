

#include "FloatBuffer_Emscripten.hpp"

#include <sstream>

#include "NativeGL_Emscripten.hpp"

using namespace emscripten;

long long FloatBuffer_Emscripten::_nextID = 0;


FloatBuffer_Emscripten::FloatBuffer_Emscripten(size_t size) :
_buffer( val::global("Float32Array").new_( val(size) ) ),
_timestamp(0),
_id(_nextID++),
_nativeGL(NULL),
_webGLBuffer( val::null() ),
_webGLBufferTimeStamp(-1)
{

}

FloatBuffer_Emscripten::FloatBuffer_Emscripten(float f0,
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
                                               float f15) :
_buffer( val::global("Float32Array").new_( val(16) ) ),
_timestamp(0),
_id(_nextID++),
_nativeGL(NULL),
_webGLBuffer( val::null() ),
_webGLBufferTimeStamp(-1)
{
  _buffer.set( 0, f0);
  _buffer.set( 1, f1);
  _buffer.set( 2, f2);
  _buffer.set( 3, f3);
  _buffer.set( 4, f4);
  _buffer.set( 5, f5);
  _buffer.set( 6, f6);
  _buffer.set( 7, f7);
  _buffer.set( 8, f8);
  _buffer.set( 9, f9);
  _buffer.set(10, f10);
  _buffer.set(11, f11);
  _buffer.set(12, f12);
  _buffer.set(13, f13);
  _buffer.set(14, f14);
  _buffer.set(15, f15);
}


long long FloatBuffer_Emscripten::getID() const {
  return _id;
}

const size_t FloatBuffer_Emscripten::size() const {
  return _buffer.as<bool>() ? _buffer["length"].as<size_t>() : 0;
}

int FloatBuffer_Emscripten::timestamp() const {
  return _timestamp;
}

const std::string FloatBuffer_Emscripten::description() const {
  std::ostringstream oss;

  oss << "FloatBuffer_Emscripten(";
  oss << "size=";
  oss << size();
  oss << ", timestamp=";
  oss << _timestamp;
  oss << ", values=(";
  oss << (_buffer.as<bool>() ? _buffer.call<std::string>("toString") : "NULL");
  oss << ")";

  oss << ")";

  return oss.str();
}

float FloatBuffer_Emscripten::get(const size_t i) const {
  return _buffer[i].as<float>();
}

void FloatBuffer_Emscripten::put(const size_t i,
                                 float value) {
  if (_buffer[i].as<float>() != value) {
    _buffer.set(i, value);
    _timestamp++;
  }
}

void FloatBuffer_Emscripten::rawPut(const size_t i,
                                    float value) {
  _buffer.set(i, value);
}

void FloatBuffer_Emscripten::rawAdd(const size_t i,
                                    float value) {
  _buffer.set(i, _buffer[i].as<float>() + value);
}

void FloatBuffer_Emscripten::rawPut(const size_t i,
                                    const IFloatBuffer* srcBuffer,
                                    const size_t srcFromIndex,
                                    const size_t count) {
  for (int j = 0; j < count; j++) {
    rawPut(i + j, srcBuffer->get(srcFromIndex + j));
  }
}

val FloatBuffer_Emscripten::bindVBO(const NativeGL_Emscripten* nativeGL) {
//  if (!_webGLBuffer.as<bool>()) {
  if (_webGLBuffer.isNull()) {
    _nativeGL = nativeGL;
    _webGLBuffer = _nativeGL->createBuffer();
  }

  _nativeGL->bindBuffer(_webGLBuffer);

  if (_webGLBufferTimeStamp != _timestamp) {
    _webGLBufferTimeStamp = _timestamp;
    _nativeGL->bufferData(_buffer);
  }

  return _webGLBuffer;
}

FloatBuffer_Emscripten::~FloatBuffer_Emscripten() {
//  if (_webGLBuffer.as<bool>()) {
  if (!_webGLBuffer.isNull()) {
    _nativeGL->deleteBuffer(_webGLBuffer);
    _webGLBuffer = val::null();
    _nativeGL = NULL;
  }
}

val FloatBuffer_Emscripten::getBuffer() const {
  return _buffer;
}
