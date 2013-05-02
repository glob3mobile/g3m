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
class SceneJSParserStatistics;

class SceneJSShapesParser {
private:
  Shape* _rootShape;
  const std::string& _uriPrefix;

  SceneJSShapesParser(const JSONBaseObject* jsonObject,
                      const std::string& uriPrefix,
                      bool isTransparent);

  Shape* getRootShape() const {
    return _rootShape;
  }

  void pvtParse(const JSONBaseObject* json,
                bool isTransparent);

  SGNode* toNode(const JSONBaseObject* jsonBaseObject) const;

  int parseChildren(const JSONObject* jsonObject,
                    SGNode* node) const;

  void checkProcessedKeys(const JSONObject* jsonObject,
                          int processedKeys) const;

  SGNode*          createNode         (const JSONObject* jsonObject) const;
  SGRotateNode*    createRotateNode   (const JSONObject* jsonObject) const;
  SGTranslateNode* createTranslateNode(const JSONObject* jsonObject) const;
  SGMaterialNode*  createMaterialNode (const JSONObject* jsonObject) const;
  SGTextureNode*   createTextureNode  (const JSONObject* jsonObject) const;
  SGGeometryNode*  createGeometryNode (const JSONObject* jsonObject) const;
  SGLayerNode*     createLayerNode    (const JSONObject* jsonObject) const;

  Color* parseColor(const JSONObject* jsColor) const;

  SceneJSParserStatistics* _statistics;

public:

  static Shape* parseFromJSONBaseObject(const JSONBaseObject* jsonObject,
                                        const std::string& uriPrefix,
                                        bool isTransparent);

  static Shape* parseFromJSON(const std::string& json,
                              const std::string& uriPrefix,
                              bool isTransparent);

  static Shape* parseFromJSON(const IByteBuffer* json,
                              const std::string& uriPrefix,
                              bool isTransparent);

  static Shape* parseFromBSON(IByteBuffer* bson,
                              const std::string& uriPrefix,
                              bool isTransparent);
  
};

#endif
