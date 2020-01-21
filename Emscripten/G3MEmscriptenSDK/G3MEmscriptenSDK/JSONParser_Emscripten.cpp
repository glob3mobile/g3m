

#include "JSONParser_Emscripten.hpp"

#include "IByteBuffer.hpp"

#include <emscripten/val.h>

#include "JSONNull.hpp"
#include "JSONBoolean.hpp"
#include "JSONInteger.hpp"
#include "JSONLong.hpp"
#include "JSONFloat.hpp"
#include "JSONDouble.hpp"
#include "JSONString.hpp"
#include "JSONArray.hpp"


using namespace emscripten;


const JSONBaseObject* convert(val& json,
                              bool nullAsObject);

const JSONBaseObject* JSONParser_Emscripten::parse(const IByteBuffer* buffer,
                                                   bool nullAsObject)
{
  return parse(buffer->getAsString(), nullAsObject);
}

const JSONBaseObject* JSONParser_Emscripten::parse(const std::string& json,
                                                   bool nullAsObject)
{
  val JSON = val::global("JSON");

  val result = JSON.call<val>("parse", json);
  return convert(result, nullAsObject);
}

//var printError = function(error, explicit) {
//    console.log(`[${explicit ? 'EXPLICIT' : 'INEXPLICIT'}] ${error.name}: ${error.message}`);
//}
//
//try {
//    var json = `
//        {
//            "first": "Jane",
//            last: "Doe",
//        }
//    `
//    console.log(JSON.parse(json));
//} catch (e) {
//    if (e instanceof SyntaxError) {
//        printError(e, true);
//    } else {
//        printError(e, false);
//    }
//}

const JSONBaseObject* convert(val& json,
                              bool nullAsObject)
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
       array->add( convert(element, nullAsObject) );
    }
    return array;
  }

//  if (json.is)

#error TODO

}
