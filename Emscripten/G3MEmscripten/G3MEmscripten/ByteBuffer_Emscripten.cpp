

#include "ByteBuffer_Emscripten.hpp"

#include <sstream>
#include <emscripten.h>

using namespace emscripten;


ByteBuffer_Emscripten::ByteBuffer_Emscripten(const size_t size) :
_buffer( val::global("Int8Array").new_( val(size) ) ),
_timestamp(0)
{

}

ByteBuffer_Emscripten::ByteBuffer_Emscripten(const val& buffer) :
_buffer(buffer),
_timestamp(0)
{

}

size_t ByteBuffer_Emscripten::size() const {
  return _buffer.as<bool>() ? _buffer["length"].as<size_t>() : 0;
}

int ByteBuffer_Emscripten::timestamp() const {
  return _timestamp;
}

unsigned char ByteBuffer_Emscripten::get(size_t i) const {
  return _buffer[i].as<unsigned char>();
}

void ByteBuffer_Emscripten::put(size_t i, unsigned char value) {
  if (_buffer[i].as<unsigned char>() != value) {
    _buffer.set(i, value);
    _timestamp++;
  }
}

void ByteBuffer_Emscripten::rawPut(size_t i, unsigned char value) {
  _buffer.set(i, value);
}

const std::string ByteBuffer_Emscripten::description() const {
  std::ostringstream oss;

  oss << "ByteBuffer_Emscripten(";
  oss << "size=";
  oss << size();
  oss << ", timestamp=";
  oss << _timestamp;
  oss << ", values=(";
  oss << _buffer.call<std::string>("toString");
  oss << ")";

  oss << ")";

  return oss.str();
}

EM_JS(const char*, utf8ArrayToStr, (const val& array), {
  var out, i, len, c;
  var char2, char3;

  out = "";
  len = array.length;
  i = 0;
  while (i < len) {
    c = array[i++];
    switch (c >> 4) {
    case 0:
    case 1:
    case 2:
    case 3:
    case 4:
    case 5:
    case 6:
    case 7:
      // 0xxxxxxx
      out += String.fromCharCode(c);
      break;
    case 12:
    case 13:
      // 110x xxxx   10xx xxxx
      char2 = array[i++];
      out += String.fromCharCode(((c & 0x1F) << 6) | (char2 & 0x3F));
      break;
    case 14:
      // 1110 xxxx  10xx xxxx  10xx xxxx
      char2 = array[i++];
      char3 = array[i++];
      out += String.fromCharCode(((c & 0x0F) << 12)
          | ((char2 & 0x3F) << 6) | ((char3 & 0x3F) << 0));
      break;
    }
  }

  var lengthBytes = lengthBytesUTF8(out)+1;
  var stringOnWasmHeap = _malloc(lengthBytes);
  stringToUTF8(out, stringOnWasmHeap, lengthBytes);
  return stringOnWasmHeap;
});

const std::string ByteBuffer_Emscripten::getAsString() const {
  return std::string( utf8ArrayToStr(_buffer) );
}
