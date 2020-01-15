

#include "JSONParser_Emscripten.hpp"

#include "IByteBuffer.hpp"

#include "ErrorHandling.hpp"


const JSONBaseObject* JSONParser_Emscripten::parse(const IByteBuffer* buffer,
                                                   bool nullAsObject)
{
  return parse(buffer->getAsString(), nullAsObject);
}

const JSONBaseObject* JSONParser_Emscripten::parse(const std::string& json,
                                                   bool nullAsObject)
{
  #warning TODO!!!!!!
  THROW_EXCEPTION("TODO!");
}
