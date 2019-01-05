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

class SGNode;
class JSONBaseObject;
class JSONObject;
class SGRotateNode;
class SGTranslateNode;
class SGMaterialNode;
class SGTextureNode;
class SGGeometryNode;
class SGLayerNode;
class Color;
class IByteBuffer;
class JSONNumber;
class SceneJSParserParameters;


class SceneJSNodeParser {
public:

  class Statistics {
  private:
    int _nodesCount;
    int _materialsCount;
    int _geometriesCount;
    int _verticesCount;

  public:
    Statistics() :
    _nodesCount(0),
    _materialsCount(0),
    _geometriesCount(0),
    _verticesCount(0)
    {
    }

    void computeNode() {
      _nodesCount++;
    }

    int getNodesCount() const {
      return _nodesCount;
    }

    void computeMaterial() {
      _materialsCount++;
    }

    int getMaterialsCount() const {
      return _materialsCount;
    }

    void computeGeometry() {
      _geometriesCount++;
    }

    int getGeometriesCount() const {
      return _geometriesCount;
    }

    void computeVertex() {
      _verticesCount++;
    }

    int getVerticesCount() const {
      return _verticesCount;
    }

    const std::string asLogString() const;

    void log() const;
  };


private:

  SceneJSNodeParser();

  static SGNode* toNode(const JSONBaseObject*          jsonBaseObject,
                        SceneJSNodeParser::Statistics& statistics,
                        const SceneJSParserParameters& parameters);

  static SGNode* createNode(const JSONObject*              jsonObject,
                            SceneJSNodeParser::Statistics& statistics,
                            const SceneJSParserParameters& parameters);

  static SGRotateNode* createRotateNode(const JSONObject*              jsonObject,
                                        SceneJSNodeParser::Statistics& statistics,
                                        const SceneJSParserParameters& parameters);

  static SGTranslateNode* createTranslateNode(const JSONObject*              jsonObject,
                                              SceneJSNodeParser::Statistics& statistics,
                                              const SceneJSParserParameters& parameters);

  static SGMaterialNode* createMaterialNode(const JSONObject*              jsonObject,
                                            SceneJSNodeParser::Statistics& statistics,
                                            const SceneJSParserParameters& parameters);

  static SGTextureNode* createTextureNode(const JSONObject*              jsonObject,
                                          SceneJSNodeParser::Statistics& statistics,
                                          const SceneJSParserParameters& parameters);

  static SGGeometryNode* createGeometryNode(const JSONObject*              jsonObject,
                                            SceneJSNodeParser::Statistics& statistics,
                                            const SceneJSParserParameters& parameters);

  static SGLayerNode* createLayerNode(const JSONObject*              jsonObject,
                                      SceneJSNodeParser::Statistics& statistics,
                                      const SceneJSParserParameters& parameters);

  static const Color* parseColor(const JSONObject* jsColor,
                                 const float defaultRed,
                                 const float defaultGreen,
                                 const float defaultBlue,
                                 const float defaultAlpha);

  static void parseChildren(const JSONObject*              jsonObject,
                            SGNode*                        node,
                            SceneJSNodeParser::Statistics& statistics,
                            const SceneJSParserParameters& parameters);

  static const int parseInt(const JSONNumber* jsonNumber,
                            const int defaultValue);

public:

  static SGNode* parseFromJSONBaseObject(const JSONBaseObject*          jsonObject,
                                         const SceneJSParserParameters& parameters);

  static SGNode* parseFromJSON(const std::string&             json,
                               const SceneJSParserParameters& parameters);

  static SGNode* parseFromJSON(const IByteBuffer*             json,
                               const SceneJSParserParameters& parameters);

  static SGNode* parseFromBSON(const IByteBuffer*             bson,
                               const SceneJSParserParameters& parameters);
  
};

#endif
