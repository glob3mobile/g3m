

#include "JSONParser_Emscripten.hpp"

#include <emscripten/val.h>
#include <emscripten/em_js.h>
#include <limits>

#include "EMStorage.hpp"

#include "G3M/IByteBuffer.hpp"
#include "G3M/ILogger.hpp"
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


JSONBaseObject* _convert(const val& json,
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

    // casting a double outside the target range is undefined behaviour, so check the range first
    if ((doubleValue >= std::numeric_limits<int>::min()) &&
        (doubleValue <= std::numeric_limits<int>::max())) {
      const int intValue = (int) doubleValue;
      if (doubleValue == intValue) {
        return new JSONInteger(intValue);
      }
    }

    // -2^63 is exact as a double, 2^63 (the first value out of range) is its negation
    const double minLongLong = (double) std::numeric_limits<long long>::min();
    if ((doubleValue >= minLongLong) &&
        (doubleValue < -minLongLong)) {
      const long long longValue = (long long) doubleValue;
      if (doubleValue == longValue) {
        return new JSONLong(longValue);
      }
    }

    if ((doubleValue >= -std::numeric_limits<float>::max()) &&
        (doubleValue <= std::numeric_limits<float>::max())) {
      const float floatValue = (float) doubleValue;
      if (doubleValue == floatValue) {
        return new JSONFloat(floatValue);
      }
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
      array->add( _convert(element, nullAsObject, Object) );
    }
    return array;
  }

  {
    // object
    JSONObject* object = new JSONObject();

    val jsKeys = Object.call<val>("keys", json);
    const int keysLength = jsKeys["length"].as<int>();
    for (int i = 0; i < keysLength; i++) {
      val jsKey   = jsKeys[i];
      val jsValue = json[jsKey];

      //      EMStorage::consoleLog( val("jsKey") );
      //      EMStorage::consoleLog( jsKey );
      //
      //      EMStorage::consoleLog( val("jsValue") );
      //      EMStorage::consoleLog( jsValue );
      
      JSONBaseObject* value = _convert(jsValue, nullAsObject, Object);
      if (value != NULL) {
        const std::string key = jsKey.as<std::string>();
        object->put(key, value);
      }
    }

    return object;
  }

  return NULL;
}

// JSON.parse throws on invalid text; C++ can't catch a JS exception, so the error comes back as a value
EM_JS_DEPS(JSONParser_Emscripten, "$UTF8ToString");
EM_JS(int, JSONParser_Emscripten_parseToStorage, (const char* json), {
  var result;
  try {
    result = { "value" : JSON.parse(UTF8ToString(json)) };
  }
  catch (e) {
    result = { "error" : String(e) };
  }
  return document.EMStorage.put(result);
});

const JSONBaseObject* JSONParser_Emscripten::parse(const IByteBuffer* buffer,
                                                   bool nullAsObject)
{
  return parse(buffer->getAsString(), nullAsObject);
}

const JSONBaseObject* JSONParser_Emscripten::parse(const std::string& json,
                                                   bool nullAsObject)
{
  const val jsResult = EMStorage::take( JSONParser_Emscripten_parseToStorage(json.c_str()) );

  const val jsError = jsResult["error"];
  if (!jsError.isUndefined()) {
    ILogger::instance()->logError("Can't parse JSON: %s", jsError.as<std::string>().c_str());
    return NULL;
  }

  const JSONBaseObject* result = _convert(jsResult["value"], nullAsObject, val::global("Object"));

//  EMStorage::consoleLog( val(result->description()) );

  return result;
}
