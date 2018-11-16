//
//  SceneJSNodeParser.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/17/17.
//
//

#include "SceneJSNodeParser.hpp"

#include "IJSONParser.hpp"
#include "BSONParser.hpp"
#include "JSONObject.hpp"
#include "JSONString.hpp"
#include "JSONArray.hpp"
#include "JSONNumber.hpp"
#include "SGNode.hpp"
#include "SGLayerNode.hpp"
#include "SGRotateNode.hpp"
#include "SGTranslateNode.hpp"
#include "SGMaterialNode.hpp"
#include "SGTextureNode.hpp"
#include "SGGeometryNode.hpp"
#include "Color.hpp"
#include "GLConstants.hpp"
#include "IFactory.hpp"
#include "IShortBuffer.hpp"
#include "IFloatBuffer.hpp"
#include "JSONBoolean.hpp"
#include "IStringBuilder.hpp"
#include "SceneJSParserParameters.hpp"


const std::string SceneJSNodeParser::Statistics::asLogString() const {
  IStringBuilder* statsSB = IStringBuilder::newStringBuilder();
  
  statsSB->addString("Nodes=");
  statsSB->addInt(getNodesCount());
  statsSB->addString("; Materials=");
  statsSB->addInt(getMaterialsCount());
  statsSB->addString("; Geometries=");
  statsSB->addInt(getGeometriesCount());
  statsSB->addString("; Vertices=");
  statsSB->addInt(getVerticesCount());
  statsSB->addString("; Vert/Geom=");
  statsSB->addFloat((float) getVerticesCount() / getGeometriesCount());
  statsSB->addString("; Vert/Mat=");
  statsSB->addFloat((float) getVerticesCount() / getMaterialsCount());
  statsSB->addString("; Vert/Nod=");
  statsSB->addFloat((float) getVerticesCount() / getNodesCount());
  
  const std::string stats = statsSB->getString();
  delete statsSB;
  
  return stats;
}

void SceneJSNodeParser::Statistics::log() const {
  if (ILogger::instance()) {
    ILogger::instance()->logInfo("\nSceneJSNodeParser::Statistics: %s", asLogString().c_str());
  }
}


SceneJSNodeParser::SceneJSNodeParser() {
}

SGNode* SceneJSNodeParser::parseFromJSON(const std::string&             json,
                                         const SceneJSParserParameters& parameters) {
  const JSONBaseObject* jsonObject = IJSONParser::instance()->parse(json);
  SGNode* result = parseFromJSONBaseObject(jsonObject, parameters);
  delete jsonObject;
  return result;
}

SGNode* SceneJSNodeParser::parseFromJSON(const IByteBuffer*             json,
                                         const SceneJSParserParameters& parameters) {
  const JSONBaseObject* jsonObject = IJSONParser::instance()->parse(json);
  SGNode* result = parseFromJSONBaseObject(jsonObject, parameters);
  delete jsonObject;
  return result;
}

SGNode* SceneJSNodeParser::parseFromBSON(const IByteBuffer*             bson,
                                         const SceneJSParserParameters& parameters) {
  const JSONBaseObject* jsonObject = BSONParser::parse(bson);
  SGNode* result = parseFromJSONBaseObject(jsonObject, parameters);
  delete jsonObject;
  return result;
}

SGNode* SceneJSNodeParser::parseFromJSONBaseObject(const JSONBaseObject*          jsonObject,
                                                   const SceneJSParserParameters& parameters) {
  SceneJSNodeParser::Statistics statistics = SceneJSNodeParser::Statistics();
  SGNode* result = toNode(jsonObject, statistics, parameters);
  statistics.log();
  return result;
}

SGNode* SceneJSNodeParser::toNode(const JSONBaseObject*          jsonBaseObject,
                                  SceneJSNodeParser::Statistics& statistics,
                                  const SceneJSParserParameters& parameters) {
  SGNode* result = NULL;
  
  if (jsonBaseObject != NULL) {
    const JSONObject* jsonObject = jsonBaseObject->asObject();
    if (jsonObject != NULL) {
      const JSONString* jsType = jsonObject->getAsString("type");
      if (jsType != NULL) {
        const std::string type = jsType->value();
        if (type.compare("node") == 0) {
          result = createNode(jsonObject, statistics, parameters);
          statistics.computeNode();
        }
        else if (type.compare("rotate") == 0) {
          result = createRotateNode(jsonObject, statistics, parameters);
        }
        else if (type.compare("translate") == 0) {
          result = createTranslateNode(jsonObject, statistics, parameters);
        }
        else if (type.compare("material") == 0) {
          result = createMaterialNode(jsonObject, statistics, parameters);
          statistics.computeMaterial();
        }
        else if (type.compare("texture") == 0) {
          result = createTextureNode(jsonObject, statistics, parameters);
        }
        else if (type.compare("geometry") == 0) {
          result = createGeometryNode(jsonObject, statistics, parameters);
          statistics.computeGeometry();
        }
        else {
          ILogger::instance()->logWarning("SceneJS: Unknown type \"%s\"", type.c_str());
        }
      }
    }
  }
  
  return result;
}

SGNode* SceneJSNodeParser::createNode(const JSONObject*              jsonObject,
                                      SceneJSNodeParser::Statistics& statistics,
                                      const SceneJSParserParameters& parameters) {
  const std::string id  = jsonObject->getAsString("id",  "");
  const std::string sID = jsonObject->getAsString("sid", "");
  SGNode* node = new SGNode(id, sID);
  parseChildren(jsonObject, node, statistics, parameters);
  return node;
}

void SceneJSNodeParser::parseChildren(const JSONObject*              jsonObject,
                                      SGNode*                        node,
                                      SceneJSNodeParser::Statistics& statistics,
                                      const SceneJSParserParameters& parameters) {
  const JSONArray* jsNodes = jsonObject->getAsArray("nodes");
  if (jsNodes != NULL) {
    const size_t nodesCount = jsNodes->size();
    for (size_t i = 0; i < nodesCount; i++) {
      const JSONObject* child = jsNodes->getAsObject(i);
      if (child != NULL) {
        SGNode* childNode = toNode(child, statistics, parameters);
        if (childNode != NULL) {
          node->addNode(childNode);
        }
      }
    }
  }
}

SGRotateNode* SceneJSNodeParser::createRotateNode(const JSONObject*              jsonObject,
                                                  SceneJSNodeParser::Statistics& statistics,
                                                  const SceneJSParserParameters& parameters) {
  const std::string id  = jsonObject->getAsString("id",  "");
  const std::string sID = jsonObject->getAsString("sid", "");
  
  const double x = jsonObject->getAsNumber("x", 0);
  const double y = jsonObject->getAsNumber("y", 0);
  const double z = jsonObject->getAsNumber("z", 0);
  
  const double angle = jsonObject->getAsNumber("angle", 0);
  
  SGRotateNode* node = new SGRotateNode(id, sID,
                                        x, y, z,
                                        angle);
  
  parseChildren(jsonObject, node, statistics, parameters);
  
  return node;
}

SGTranslateNode* SceneJSNodeParser::createTranslateNode(const JSONObject*              jsonObject,
                                                        SceneJSNodeParser::Statistics& statistics,
                                                        const SceneJSParserParameters& parameters) {
  const std::string id  = jsonObject->getAsString("id",  "");
  const std::string sID = jsonObject->getAsString("sid", "");
  
  const double x = jsonObject->getAsNumber("x", 0);
  const double y = jsonObject->getAsNumber("y", 0);
  const double z = jsonObject->getAsNumber("z", 0);
  
  SGTranslateNode* node = new SGTranslateNode(id, sID,
                                              x, y, z);
  
  parseChildren(jsonObject, node, statistics, parameters);
  
  return node;
}

SGMaterialNode* SceneJSNodeParser::createMaterialNode(const JSONObject*              jsonObject,
                                                      SceneJSNodeParser::Statistics& statistics,
                                                      const SceneJSParserParameters& parameters) {
  const std::string id  = jsonObject->getAsString("id",  "");
  const std::string sID = jsonObject->getAsString("sid", "");
  
  const Color* baseColor     = parseColor(jsonObject->getAsObject("baseColor"),
                                          Color::newFromRGBA(0, 0, 0, 1));
  const Color* specularColor = parseColor(jsonObject->getAsObject("specularColor"),
                                          Color::newFromRGBA(0, 0, 0, 1));
  
  const double shine    = jsonObject->getAsNumber("shine", 10);
  const double specular = jsonObject->getAsNumber("specular", 1);
  const double alpha    = jsonObject->getAsNumber("alpha", 1);
  const double emit     = jsonObject->getAsNumber("emit", 0);
  
  SGMaterialNode* node = new SGMaterialNode(id, sID,
                                            baseColor,
                                            specularColor,
                                            specular,
                                            shine,
                                            alpha,
                                            emit);
  
  parseChildren(jsonObject, node, statistics, parameters);
  
  return node;
}

const Color* SceneJSNodeParser::parseColor(const JSONObject* jsColor,
                                           const Color* defaultColor) {
  if (jsColor == NULL) {
    return defaultColor;
  }
  
  const float r = (float) jsColor->getAsNumber("r", 0.0);
  const float g = (float) jsColor->getAsNumber("g", 0.0);
  const float b = (float) jsColor->getAsNumber("b", 0.0);
  const float a = (float) jsColor->getAsNumber("a", 1.0);
  
  return Color::newFromRGBA(r, g, b, a);
}

SGTextureNode* SceneJSNodeParser::createTextureNode(const JSONObject*              jsonObject,
                                                    SceneJSNodeParser::Statistics& statistics,
                                                    const SceneJSParserParameters& parameters) {
  const std::string id  = jsonObject->getAsString("id",  "");
  const std::string sID = jsonObject->getAsString("sid", "");
  
  SGTextureNode* node = new SGTextureNode(id, sID);
  
  parseChildren(jsonObject, node, statistics, parameters);
  
  const JSONArray* jsLayers = jsonObject->getAsArray("layers");
  if (jsLayers != NULL) {
    size_t layersCount = jsLayers->size();
    for (size_t i = 0; i < layersCount; i++) {
      const JSONObject* jsLayer = jsLayers->getAsObject(i);
      if (jsLayer != NULL) {
        node->addLayer( createLayerNode(jsLayer, statistics, parameters) );
      }
    }
  }
  
  return node;
}

SGGeometryNode* SceneJSNodeParser::createGeometryNode(const JSONObject*              jsonObject,
                                                      SceneJSNodeParser::Statistics& statistics,
                                                      const SceneJSParserParameters& parameters) {
  const std::string id  = jsonObject->getAsString("id",  "");
  const std::string sID = jsonObject->getAsString("sid", "");
  
  int primitive = GLPrimitive::triangles();
  {
    const std::string strPrimitive = jsonObject->getAsString("primitive", "triangles");
    if (strPrimitive.compare("points") == 0) {
      primitive = GLPrimitive::points();
    }
    else if (strPrimitive.compare("lines") == 0) {
      primitive = GLPrimitive::lines();
    }
    else if (strPrimitive.compare("line-loop") == 0) {
      primitive = GLPrimitive::lineLoop();
    }
    else if (strPrimitive.compare("line-strip") == 0) {
      primitive = GLPrimitive::lineStrip();
    }
    else if (strPrimitive.compare("triangles") == 0) {
      primitive = GLPrimitive::triangles();
    }
    else if (strPrimitive.compare("triangle-strip") == 0) {
      primitive = GLPrimitive::triangleStrip();
    }
    else if (strPrimitive.compare("triangle-fan") == 0) {
      primitive = GLPrimitive::triangleFan();
    }
  }
  
  const JSONArray* jsPositions = jsonObject->getAsArray("positions");
  if (jsPositions == NULL) {
    ILogger::instance()->logError("Mandatory positions are not present");
    return NULL;
  }
  
  const size_t verticesCount = jsPositions->size();
  IFloatBuffer* vertices = IFactory::instance()->createFloatBuffer(verticesCount);
  for (size_t i = 0; i < verticesCount; i++) {
    vertices->put(i, (float) jsPositions->getAsNumber(i)->value());
    statistics.computeVertex();
  }
  
  const JSONArray* jsColors = jsonObject->getAsArray("colors");
  IFloatBuffer* colors = NULL;
  if (jsColors != NULL) {
    const size_t colorsCount = jsColors->size();
    colors = IFactory::instance()->createFloatBuffer(colorsCount);
    for (size_t i = 0; i < colorsCount; i++) {
      const float value = (float) jsColors->getAsNumber(i)->value();
      colors->put(i, value);
    }
  }
  
  const JSONArray* jsUV = jsonObject->getAsArray("uv");
  IFloatBuffer* uv = NULL;
  if (jsUV != NULL) {
    const size_t uvCount = jsUV->size();
    uv = IFactory::instance()->createFloatBuffer(uvCount);
    bool isY = false;
    for (size_t i = 0; i < uvCount; i++) {
      float value = (float) jsUV->getAsNumber(i)->value();
      if (isY) {
        value = 1 - value;
      }
      isY = !isY;
      uv->put(i, value);
    }
  }
  
  const JSONArray* jsNormals = jsonObject->getAsArray("normals");
  IFloatBuffer* normals = NULL;
  if (jsNormals != NULL) {
    const size_t normalsCount = jsNormals->size();
    normals = IFactory::instance()->createFloatBuffer(normalsCount);
    for (size_t i = 0; i < normalsCount; i++) {
      float value = (float) jsNormals->getAsNumber(i)->value();
      normals->put(i, value);
    }
  }
  
  const JSONArray* jsIndices = jsonObject->getAsArray("indices");
  if (jsIndices == NULL) {
    ILogger::instance()->logError("Non indexed geometries not supported");
    return NULL;
  }
  int indicesOutOfRange = 0;
  const size_t indicesCount = jsIndices->size();
  IShortBuffer* indices = IFactory::instance()->createShortBuffer(indicesCount);
  for (size_t i = 0; i < indicesCount; i++) {
    const long long indice = (long long) jsIndices->getAsNumber(i)->value();
    if (indice > 32767) {
      indicesOutOfRange++;
    }
    indices->rawPut(i, (short) indice);
  }
  
  if (indicesOutOfRange > 0) {
    ILogger::instance()->logError("SceneJSShapesParser: There are %d (of %d) indices out of range.",
                                  indicesOutOfRange,
                                  indicesCount);
  }
  
  SGGeometryNode* node = new SGGeometryNode(id, sID,
                                            primitive,
                                            vertices,
                                            colors,
                                            uv,
                                            normals,
                                            indices,
                                            parameters._depthTest);
  
  parseChildren(jsonObject, node, statistics, parameters);
  
  return node;
}

const int SceneJSNodeParser::parseInt(const JSONNumber* jsonNumber,
                                      const int defaultValue) {
  if (jsonNumber == NULL) {
    return defaultValue;
  }
  return (int) jsonNumber->value();
}

SGLayerNode* SceneJSNodeParser::createLayerNode(const JSONObject*              jsonObject,
                                                SceneJSNodeParser::Statistics& statistics,
                                                const SceneJSParserParameters& parameters) {
  const std::string id  = jsonObject->getAsString("id",  "");
  const std::string sID = jsonObject->getAsString("sid", "");
  const std::string uri = jsonObject->getAsString("uri", "");
  
//  const std::string applyTo = jsonObject->getAsString("applyTo", "baseColor");
//
//  const std::string blendMode = jsonObject->getAsString("blendMode", "add");
//
//  const bool flipY = jsonObject->getAsBoolean("flipY", true);
//
//  const std::string magFilter = jsonObject->getAsString("magFilter", "linear");
//  const std::string minFilter = jsonObject->getAsString("minFilter", "linear");

//  const std::string wrapS = jsonObject->getAsString("wrapS", "clampToEdge");
//  const std::string wrapT = jsonObject->getAsString("wrapT", "clampToEdge");

  
////  const bool generateMipmap = false;
//  const int wrapS = GLTextureParameterValue::repeat();
//  const int wrapT = GLTextureParameterValue::mirroredRepeat();

  const int wrapS = parseInt(jsonObject->getAsNumber("wrapS"), parameters._defaultWrapS);
  const int wrapT = parseInt(jsonObject->getAsNumber("wrapT"), parameters._defaultWrapT);

  SGLayerNode* node = new SGLayerNode(id,
                                      sID,
                                      uri,
                                      wrapS,
                                      wrapT,
                                      parameters._generateMipmap);
  
  parseChildren(jsonObject, node, statistics, parameters);
  
  return node;
}
