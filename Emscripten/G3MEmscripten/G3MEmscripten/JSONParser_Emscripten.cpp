

#include "JSONParser_Emscripten.hpp"

#include "G3M/IByteBuffer.hpp"

#include <emscripten/val.h>

#include "G3M/JSONNull.hpp"
#include "G3M/JSONBoolean.hpp"
#include "G3M/JSONInteger.hpp"
#include "G3M/JSONLong.hpp"
#include "G3M/JSONFloat.hpp"
#include "G3M/JSONDouble.hpp"
#include "G3M/JSONString.hpp"
#include "G3M/JSONArray.hpp"
#include "G3M/JSONObject.hpp"


using namespace emscripten;


const JSONBaseObject* convert(const val& json,
                              const bool nullAsObject,
                              const val& Object);

const JSONBaseObject* JSONParser_Emscripten::parse(const IByteBuffer* buffer,
                                                   bool nullAsObject)
{
  return parse(buffer->getAsString(), nullAsObject);
}

const JSONBaseObject* JSONParser_Emscripten::parse(const std::string& json,
                                                   bool nullAsObject)
{
  const val result = val::global("JSON").call<val>("parse", json);
  return convert(result, nullAsObject, val::global("Object"));
}

const JSONBaseObject* convert(const val& json,
                              const bool nullAsObject,
                              const val& Object)
{
  if (json.isNull()) {
    return nullAsObject ? new JSONNull() : NULL;
  }

  if (json.isTrue()) {
    return new JSONBoolean(true);
  }
  if (json.isFalse()) {
    return new JSONBoolean(false);
  }

  if (json.isNumber()) {
    const double doubleValue = json.as<double>();

    const int intValue = (int) doubleValue;
    if (doubleValue == intValue) {
      return new JSONInteger(intValue);
    }

    const long long longValue = (long long) doubleValue;
    if (doubleValue == longValue) {
      return new JSONLong(longValue);
    }

    const float floatValue = (float) doubleValue;
    if (doubleValue == floatValue) {
      return new JSONFloat(floatValue);
    }

    return new JSONDouble(doubleValue);
  }

  if (json.isString()) {
    return new JSONString(json.as<std::string>());
  }

  if (json.isArray()) {
    const int length = json["length"].as<int>();
    JSONArray* array = new JSONArray(length);
    for (int i = 0; i < length; i++) {
      val element = json[i];
      array->add( convert(element, nullAsObject, Object) );
    }
    return array;
  }

  {
    // object
    JSONObject* object = new JSONObject();

    val jsKeys = Object.call<val>("keys", json);
    const int keysLength = jsKeys["length"].as<int>();
    for (int i = 0; i < keysLength; i++) {
      const val jsKey   = jsKeys[i];
      const val jsValue = json[jsKey];

      const JSONBaseObject* value = convert(jsValue, nullAsObject, Object);
      if (value != NULL) {
        const std::string key = jsKey.as<std::string>();
        object->put(key, value);
      }
    }
  }

  return NULL;
}
