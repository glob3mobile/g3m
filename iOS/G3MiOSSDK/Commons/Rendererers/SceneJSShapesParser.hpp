//
//  SceneJSShapesParser.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/7/12.
//
//

#ifndef __G3MiOSSDK__SceneJSShapesParser__
#define __G3MiOSSDK__SceneJSShapesParser__


#include <string>
class Shape;
class IByteBuffer;
class JSONBaseObject;


class SceneJSShapesParser {
private:
  Shape* _rootShape;

  SceneJSShapesParser(const std::string& json);
  SceneJSShapesParser(const IByteBuffer* json);

  Shape* getRootShape() const {
    return _rootShape;
  }

  void pvtParse(const std::string& json);

  Shape* toShape(JSONBaseObject* jsonBaseObject) const;

public:

  static Shape* parse(const std::string& json);
  static Shape* parse(const IByteBuffer* json);

};

#endif
