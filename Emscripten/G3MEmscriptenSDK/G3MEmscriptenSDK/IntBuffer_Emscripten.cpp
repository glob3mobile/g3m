

#include "IntBuffer_Emscripten.hpp"

#include "G3M/ILogger.hpp"
#include "G3M/ErrorHandling.hpp"

#include <sstream>


long long IntBuffer_Emscripten::_nextID = 0;


IntBuffer_Emscripten::IntBuffer_Emscripten(const size_t size) :
_size(size),
_timestamp(0),
_id(_nextID++)
{
  _values = new int[size];

  if (_values == NULL) {
    ILogger::instance()->logError("Allocating error.");
  }
}

long long IntBuffer_Emscripten::getID() const {
  return _id;
}

IntBuffer_Emscripten::~IntBuffer_Emscripten() {
  delete [] _values;
}

const size_t IntBuffer_Emscripten::size() const {
  return _size;
}

int IntBuffer_Emscripten::timestamp() const {
  return _timestamp;
}

int IntBuffer_Emscripten::get(const size_t i) const {
  if (i >= _size) {
    THROW_EXCEPTION("Buffer Overflow");
  }

  return _values[i];
}

void IntBuffer_Emscripten::put(const size_t i, int value) {
  if (i >= _size) {
    THROW_EXCEPTION("Buffer Overflow");
  }

  if (_values[i] != value) {
    _values[i] = value;
    _timestamp++;
  }
}

void IntBuffer_Emscripten::rawPut(const size_t i, int value) {
  if (i >= _size) {
    THROW_EXCEPTION("Buffer Overflow");
  }

  _values[i] = value;
}

const std::string IntBuffer_Emscripten::description() const {
  std::ostringstream oss;

  oss << "IntBuffer_Emscripten(";
  oss << "size=";
  oss << _size;
  oss << ", timestamp=";
  oss << _timestamp;
  oss << ", values=";
  oss << _values;
  oss << ")";

  return oss.str();
}
