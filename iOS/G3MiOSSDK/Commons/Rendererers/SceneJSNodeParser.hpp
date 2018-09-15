//
//  SceneJSNodeParser.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/17/17.
//
//

#ifndef SceneJSNodeParser_hpp
#define SceneJSNodeParser_hpp

#include <string>

class JSONBaseObject;
class JSONObject;
class SceneJSParserStatistics;
class SGNode;
class SGRotateNode;
class SGTranslateNode;
class SGMaterialNode;
class SGTextureNode;
class SGGeometryNode;
class SGLayerNode;
class IByteBuffer;
class Color;


class SceneJSNodeParser {
private:

  SceneJSNodeParser();

  static SGNode* toNode(const JSONBaseObject*    jsonBaseObject,
                        SceneJSParserStatistics& statistics,
                        const bool               depthTest);

  static SGNode* createNode(const JSONObject*        jsonObject,
                            SceneJSParserStatistics& statistics,
                            const bool               depthTest);

  static SGRotateNode* createRotateNode(const JSONObject*        jsonObject,
                                        SceneJSParserStatistics& statistics,
                                        const bool               depthTest);

  static SGTranslateNode* createTranslateNode(const JSONObject*        jsonObject,
                                              SceneJSParserStatistics& statistics,
                                              const bool               depthTest);

  static SGMaterialNode* createMaterialNode(const JSONObject*        jsonObject,
                                            SceneJSParserStatistics& statistics,
                                            const bool               depthTest);

  static SGTextureNode* createTextureNode(const JSONObject*        jsonObject,
                                          SceneJSParserStatistics& statistics,
                                          const bool               depthTest);

  static SGGeometryNode* createGeometryNode(const JSONObject*        jsonObject,
                                            SceneJSParserStatistics& statistics,
                                            const bool               depthTest);

  static SGLayerNode* createLayerNode(const JSONObject*        jsonObject,
                                      SceneJSParserStatistics& statistics,
                                      const bool               depthTest);

  static Color* parseColor(const JSONObject* jsColor);

  static int parseChildren(const JSONObject*        jsonObject,
                           SGNode*                  node,
                           SceneJSParserStatistics& statistics,
                           const bool               depthTest);

  static void checkProcessedKeys(const JSONObject* jsonObject,
                                 int processedKeys);

public:

  static SGNode* parseFromJSONBaseObject(const JSONBaseObject* jsonObject,
                                         const bool            depthTest,
                                         const bool            deleteJSONObject);

  static SGNode* parseFromJSON(const std::string& json,
                               const bool         depthTest);

  static SGNode* parseFromJSON(const IByteBuffer* json,
                               const bool         depthTest);

  static SGNode* parseFromBSON(const IByteBuffer* bson,
                               const bool         depthTest);
  
};

#endif
