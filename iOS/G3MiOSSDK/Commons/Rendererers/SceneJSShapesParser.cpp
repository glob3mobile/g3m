//
//  SceneJSShapesParser.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/7/12.
//
//

#include "SceneJSShapesParser.hpp"

#include "IJSONParser.hpp"
#include "IByteBuffer.hpp"
#include "JSONObject.hpp"

Shape* SceneJSShapesParser::parse(const std::string& json) {
  return SceneJSShapesParser(json).getRootShape();
}

Shape* SceneJSShapesParser::parse(const IByteBuffer* json) {
  return SceneJSShapesParser(json).getRootShape();
}


SceneJSShapesParser::SceneJSShapesParser(const IByteBuffer* json) :
_rootShape(NULL)
{
  pvtParse(json->getAsString());
}

SceneJSShapesParser::SceneJSShapesParser(const std::string& json):
_rootShape(NULL)
{
  pvtParse(json);
}


void SceneJSShapesParser::pvtParse(const std::string& json) {
  JSONBaseObject* jsonRootObject = IJSONParser::instance()->parse(json);

  _rootShape = toShape(jsonRootObject);
}

Shape* SceneJSShapesParser::toShape(JSONBaseObject* jsonBaseObject) const {
//  int ____DIEGO_AT_WORK;
  JSONObject* jsonObject = jsonBaseObject->asObject();

  std::vector<std::string> keys = jsonObject->keys();

  for (int i = 0; i < keys.size(); i++) {
    std::string key = keys[i];
    std::string value = jsonObject->get(key)->description();
    printf("%s=%s", key.c_str(), value.c_str());
  }

  return NULL;
}
