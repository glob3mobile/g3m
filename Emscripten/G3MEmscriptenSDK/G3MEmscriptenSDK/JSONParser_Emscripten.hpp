
#ifndef JSONParser_Emscripten_hpp
#define JSONParser_Emscripten_hpp

#include "IJSONParser.hpp"

class JSONParser_Emscripten : public IJSONParser {

public:
  const JSONBaseObject* parse(const std::string& json,
                              bool nullAsObject);

  const JSONBaseObject* parse(const IByteBuffer* buffer,
                              bool nullAsObject);

};

#endif
