

#include "ShortBuffer_Emscripten.hpp"

#include <sstream>

#include "NativeGL_Emscripten.hpp"


using namespace emscripten;

long long ShortBuffer_Emscripten::_nextID = 0;

ShortBuffer_Emscripten::ShortBuffer_Emscripten(size_t size) :
_buffer( val::global("Uint16Array").new_( val(size) ) ),
_timestamp(0),
_id(_nextID++),
_nativeGL(NULL),
_webGLBuffer( val::null() )
{

}

ShortBuffer_Emscripten::~ShortBuffer_Emscripten() {
  if (!_webGLBuffer.isNull()) {
    _nativeGL->deleteBuffer(_webGLBuffer);
    _webGLBuffer = val::null();
    _nativeGL = NULL;
  }
}

long long ShortBuffer_Emscripten::getID() const {
  return _id;
}

const size_t ShortBuffer_Emscripten::size() const {
  return _buffer.as<bool>() ? _buffer["length"].as<size_t>() : 0;
}

int ShortBuffer_Emscripten::timestamp() const {
  return _timestamp;
}

const std::string ShortBuffer_Emscripten::description() const {
  std::ostringstream oss;

  oss << "ShortBuffer_Emscripten(";
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

short ShortBuffer_Emscripten::get(const size_t i) const {
  return _buffer[i].as<short>();
}

void ShortBuffer_Emscripten::put(const size_t i,
                                 short value) {
  if (_buffer[i].as<short>() != value) {
    _buffer.set(i, value);
    _timestamp++;
  }
}

void ShortBuffer_Emscripten::rawPut(const size_t i,
                                    short value) {
  _buffer.set(i, value);
}

val ShortBuffer_Emscripten::getWebGLBuffer(const NativeGL_Emscripten* nativeGL) {
  if (_webGLBuffer.isNull()) {
    _nativeGL = nativeGL;
    _webGLBuffer = _nativeGL->createBuffer();
  }
  return _webGLBuffer;
}

val ShortBuffer_Emscripten::getBuffer() {
  return _buffer;
}
