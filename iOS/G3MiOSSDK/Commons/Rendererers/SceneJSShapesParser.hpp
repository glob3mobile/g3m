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
class JSONObject;
class SGNode;
class SGRotateNode;
class SGMaterialNode;
class SGTextureNode;
class SGGeometryNode;
class SGTranslateNode;
class SGLayerNode;
class Color;

class SceneJSShapesParser {
private:
  Shape* _rootShape;
  const std::string& _uriPrefix;
  
  SceneJSShapesParser(const std::string& json,
                      const std::string& uriPrefix);
  SceneJSShapesParser(const IByteBuffer* json,
                      const std::string& uriPrefix);

  Shape* getRootShape() const {
    return _rootShape;
  }

  void pvtParse(const std::string& json);

  SGNode* toNode(JSONBaseObject* jsonBaseObject) const;

  int parseChildren(JSONObject* jsonObject,
                    SGNode* node) const;

  void checkProcessedKeys(JSONObject* jsonObject,
                          int processedKeys) const;

  SGNode*          createNode         (JSONObject* jsonObject) const;
  SGRotateNode*    createRotateNode   (JSONObject* jsonObject) const;
  SGTranslateNode* createTranslateNode(JSONObject* jsonObject) const;
  SGMaterialNode*  createMaterialNode (JSONObject* jsonObject) const;
  SGTextureNode*   createTextureNode  (JSONObject* jsonObject) const;
  SGGeometryNode*  createGeometryNode (JSONObject* jsonObject) const;

  SGLayerNode*     createLayerNode  (JSONObject* jsonObject) const;


  Color* parseColor(JSONObject* jsColor) const;

public:

  static Shape* parse(const std::string& json,
                      const std::string& uriPrefix);
  static Shape* parse(const IByteBuffer* json,
                      const std::string& uriPrefix);

};

#endif
