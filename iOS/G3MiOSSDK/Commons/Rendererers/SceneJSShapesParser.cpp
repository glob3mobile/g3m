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
#include "JSONString.hpp"
#include "JSONArray.hpp"
#include "JSONNumber.hpp"

#include "SGNode.hpp"
#include "SGShape.hpp"
#include "SGRotateNode.hpp"
#include "SGTranslateNode.hpp"
#include "SGMaterialNode.hpp"
#include "SGTextureNode.hpp"
#include "SGGeometryNode.hpp"
#include "SGLayerNode.hpp"
#include "JSONBoolean.hpp"
#include "GLConstants.hpp"
#include "IFactory.hpp"
#include "IFloatBuffer.hpp"
#include "IShortBuffer.hpp"
#include "BSONParser.hpp"
#include "SceneJSParserStatistics.hpp"

SGShape* SceneJSShapesParser::parseFromJSONBaseObject(const JSONBaseObject *jsonObject,
                                                      const std::string &uriPrefix,
                                                      bool isTransparent,
                                                      Geodetic3D* position,
                                                      AltitudeMode altitudeMode) {
  return SceneJSShapesParser(jsonObject,
                             uriPrefix,
                             isTransparent,
                             position,
                             altitudeMode).getRootShape();
}

SGShape* SceneJSShapesParser::parseFromJSON(const std::string &json,
                                            const std::string &uriPrefix,
                                            bool isTransparent,
                                            Geodetic3D* position,
                                            AltitudeMode altitudeMode) {
  const JSONBaseObject* jsonObject = IJSONParser::instance()->parse(json);

  return SceneJSShapesParser(jsonObject,
                             uriPrefix,
                             isTransparent,
                             position,
                             altitudeMode).getRootShape();
}

SGShape* SceneJSShapesParser::parseFromJSON(const IByteBuffer* json,
                                            const std::string& uriPrefix,
                                            bool isTransparent,
                                            Geodetic3D* position,
                                            AltitudeMode altitudeMode) {
  const JSONBaseObject* jsonObject = IJSONParser::instance()->parse(json->getAsString());

  return SceneJSShapesParser(jsonObject,
                             uriPrefix,
                             isTransparent,
                             position,
                             altitudeMode).getRootShape();
}

SGShape* SceneJSShapesParser::parseFromBSON(IByteBuffer *bson,
                                            const std::string &uriPrefix,
                                            bool isTransparent,
                                            Geodetic3D* position,
                                            AltitudeMode altitudeMode) {
  const JSONBaseObject* jsonObject = BSONParser::parse(bson);

  return SceneJSShapesParser(jsonObject,
                             uriPrefix,
                             isTransparent,
                             position,
                             altitudeMode).getRootShape();
}

void SceneJSShapesParser::pvtParse(const JSONBaseObject* json,
                                   bool isTransparent,
                                   Geodetic3D* position,
                                   AltitudeMode altitudeMode) {
  //  _rootShape = toShape(jsonRootObject);

  SGNode* node = toNode(json);

  if (node != NULL) {
    _rootShape = new SGShape(node, _uriPrefix, isTransparent, position, altitudeMode);
  }

  delete json;
}

SceneJSShapesParser::SceneJSShapesParser(const JSONBaseObject* jsonObject,
                                         const std::string& uriPrefix,
                                         bool isTransparent,
                                         Geodetic3D* position,
                                         AltitudeMode altitudeMode) :
_uriPrefix(uriPrefix),
_rootShape(NULL)
{
  _statistics = new SceneJSParserStatistics();
  pvtParse(jsonObject,
           isTransparent,
           position,
           altitudeMode);

  _statistics->log();
  delete _statistics;
}

int SceneJSShapesParser::parseChildren(const JSONObject* jsonObject,
                                       SGNode* node) const {
  int processedKeys = 0;

  const JSONArray* jsNodes = jsonObject->getAsArray("nodes");
  if (jsNodes != NULL) {
    const int nodesCount = jsNodes->size();
    for (int i = 0; i < nodesCount; i++) {
      const JSONObject* child = jsNodes->getAsObject(i);
      if (child != NULL) {
        SGNode* childNode = toNode(child);
        if (childNode != NULL) {
          node->addNode(childNode);
        }
      }
    }
    processedKeys++;
  }

  return processedKeys;
}

void SceneJSShapesParser::checkProcessedKeys(const JSONObject* jsonObject,
                                             int processedKeys) const {
  std::vector<std::string> keys = jsonObject->keys();
  if (processedKeys != keys.size()) {
    //    for (int i = 0; i < keys.size(); i++) {
    //      printf("%s\n", keys.at(i).c_str());
    //    }

    ILogger::instance()->logWarning("Not all keys processed in node, processed %i of %i",
                                    processedKeys,
                                    keys.size());
  }
}

SGNode* SceneJSShapesParser::createNode(const JSONObject* jsonObject) const {

  int processedKeys = 1; // "type" is already processed

  const std::string id = jsonObject->getAsString("id", "");
  if (id.compare("") != 0) {
    processedKeys++;
  }

  const std::string sId = jsonObject->getAsString("sid", "");
  if (sId.compare("") != 0) {
    processedKeys++;
  }

  SGNode* node = new SGNode(id, sId);

  processedKeys += parseChildren(jsonObject, node);

  checkProcessedKeys(jsonObject, processedKeys);

  return node;
}

SGRotateNode* SceneJSShapesParser::createRotateNode(const JSONObject* jsonObject) const {
  int processedKeys = 1; // "type" is already processed

  const std::string id = jsonObject->getAsString("id", "");
  if (id.compare("") != 0) {
    processedKeys++;
  }

  const std::string sId = jsonObject->getAsString("sid", "");
  if (sId.compare("") != 0) {
    processedKeys++;
  }

  const JSONNumber* jsX = jsonObject->getAsNumber("x");
  double x = 0.0;
  if (jsX != NULL) {
    x = jsX->value();
    processedKeys++;
  }

  const JSONNumber* jsY = jsonObject->getAsNumber("y");
  double y = 0.0;
  if (jsY != NULL) {
    y = jsY->value();
    processedKeys++;
  }

  const JSONNumber* jsZ = jsonObject->getAsNumber("z");
  double z = 0.0;
  if (jsZ != NULL) {
    z = jsZ->value();
    processedKeys++;
  }

  const JSONNumber* jsAngle = jsonObject->getAsNumber("angle");
  double angle = 0;
  if (jsAngle != NULL) {
    angle = jsAngle->value();
    processedKeys++;
  }

  SGRotateNode* node = new SGRotateNode(id, sId,
                                        x, y, z,
                                        angle);

  processedKeys += parseChildren(jsonObject, node);

  checkProcessedKeys(jsonObject, processedKeys);

  return node;
}

SGTranslateNode* SceneJSShapesParser::createTranslateNode(const JSONObject* jsonObject) const {
  int processedKeys = 1; // "type" is already processed

  const std::string id = jsonObject->getAsString("id", "");
  if (id.compare("") != 0) {
    processedKeys++;
  }

  const std::string sId = jsonObject->getAsString("sid", "");
  if (sId.compare("") != 0) {
    processedKeys++;
  }

  const JSONNumber* jsX = jsonObject->getAsNumber("x");
  double x = 0.0;
  if (jsX != NULL) {
    x = jsX->value();
    processedKeys++;
  }

  const JSONNumber* jsY = jsonObject->getAsNumber("y");
  double y = 0.0;
  if (jsY != NULL) {
    y = jsY->value();
    processedKeys++;
  }

  const JSONNumber* jsZ = jsonObject->getAsNumber("z");
  double z = 0.0;
  if (jsZ != NULL) {
    z = jsZ->value();
    processedKeys++;
  }

  SGTranslateNode* node = new SGTranslateNode(id, sId,
                                              x, y, z);

  processedKeys += parseChildren(jsonObject, node);

  checkProcessedKeys(jsonObject, processedKeys);

  return node;
}

Color* SceneJSShapesParser::parseColor(const JSONObject* jsColor) const {
  const float r = (float) jsColor->getAsNumber("r", 0.0);
  const float g = (float) jsColor->getAsNumber("g", 0.0);
  const float b = (float) jsColor->getAsNumber("b", 0.0);
  const float a = (float) jsColor->getAsNumber("a", 1.0);

  return Color::newFromRGBA(r, g, b, a);
}

SGMaterialNode* SceneJSShapesParser::createMaterialNode(const JSONObject* jsonObject) const {
  int processedKeys = 1; // "type" is already processed

  const std::string id = jsonObject->getAsString("id", "");
  if (id.compare("") != 0) {
    processedKeys++;
  }

  const std::string sId = jsonObject->getAsString("sid", "");
  if (sId.compare("") != 0) {
    processedKeys++;
  }

  const JSONObject* jsBaseColor = jsonObject->getAsObject("baseColor");
  Color* baseColor;
  if (jsBaseColor == NULL) {
    baseColor = Color::newFromRGBA(0, 0, 0, 1);
  }
  else {
    baseColor = parseColor(jsBaseColor);
    processedKeys++;
  }

  const JSONObject* jsSpecularColor = jsonObject->getAsObject("specularColor");
  Color* specularColor;
  if (jsSpecularColor == NULL) {
    specularColor = Color::newFromRGBA(0, 0, 0, 1);
  }
  else {
    specularColor = parseColor(jsSpecularColor);
    processedKeys++;
  }

  const JSONNumber* jsShine = jsonObject->getAsNumber("shine");
  double shine = 10;
  if (jsShine != NULL) {
    shine = jsShine->value();
    processedKeys++;
  }

  const JSONNumber* jsSpecular = jsonObject->getAsNumber("specular");
  double specular = 1.0;
  if (jsSpecular != NULL) {
    specular = jsSpecular->value();
    processedKeys++;
  }

  const JSONNumber* jsAlpha = jsonObject->getAsNumber("alpha");
  double alpha = 1.0;
  if (jsAlpha != NULL) {
    alpha = jsAlpha->value();
    processedKeys++;
  }

  const JSONNumber* jsEmit = jsonObject->getAsNumber("emit");
  double emit = 0.0;
  if (jsEmit != NULL) {
    emit = jsEmit->value();
    processedKeys++;
  }

  SGMaterialNode* node = new SGMaterialNode(id, sId,
                                            baseColor,
                                            specularColor,
                                            specular,
                                            shine,
                                            alpha,
                                            emit);

  processedKeys += parseChildren(jsonObject, node);

  checkProcessedKeys(jsonObject, processedKeys);

  return node;
}

SGLayerNode* SceneJSShapesParser::createLayerNode(const JSONObject* jsonObject) const {
  int processedKeys = 0; //  Layer has not "type"


  const std::string id = jsonObject->getAsString("id", "");
  if (id.compare("") != 0) {
    processedKeys++;
  }

  const std::string sId = jsonObject->getAsString("sid", "");
  if (sId.compare("") != 0) {
    processedKeys++;
  }

  const std::string uri = jsonObject->getAsString("uri", "");
  if (uri.compare("") != 0) {
    processedKeys++;
  }

  const std::string applyTo = jsonObject->getAsString("applyTo", "");
  if (applyTo.compare("") != 0) {
    processedKeys++;
  }

  const std::string blendMode = jsonObject->getAsString("blendMode", "");
  if (blendMode.compare("") != 0) {
    processedKeys++;
  }

  const JSONBoolean* jsFlipY = jsonObject->getAsBoolean("flipY");
  bool flipY = true;
  if (jsFlipY != NULL) {
    flipY = jsFlipY->value();
    processedKeys++;
  }

  const std::string magFilter = jsonObject->getAsString("magFilter", "");
  if (magFilter.compare("") != 0) {
    processedKeys++;
  }

  const std::string minFilter = jsonObject->getAsString("minFilter", "");
  if (minFilter.compare("") != 0) {
    processedKeys++;
  }

  const std::string wrapS = jsonObject->getAsString("wrapS", "");
  if (wrapS.compare("") != 0) {
    processedKeys++;
  }

  const std::string wrapT = jsonObject->getAsString("wrapT", "");
  if (wrapT.compare("") != 0) {
    processedKeys++;
  }

  SGLayerNode* node = new SGLayerNode(id, sId,
                                      uri,
                                      applyTo,
                                      blendMode,
                                      flipY,
                                      magFilter,
                                      minFilter,
                                      wrapS,
                                      wrapT);

  processedKeys += parseChildren(jsonObject, node);

  checkProcessedKeys(jsonObject, processedKeys);

  return node;
}


SGTextureNode* SceneJSShapesParser::createTextureNode(const JSONObject* jsonObject) const {
  int processedKeys = 1; // "type" is already processed

  const std::string id = jsonObject->getAsString("id", "");
  if (id.compare("") != 0) {
    processedKeys++;
  }

  const std::string sId = jsonObject->getAsString("sid", "");
  if (sId.compare("") != 0) {
    processedKeys++;
  }

  SGTextureNode* node = new SGTextureNode(id, sId);

  processedKeys += parseChildren(jsonObject, node);

  const JSONArray* jsLayers = jsonObject->getAsArray("layers");
  if (jsLayers != NULL) {
    int layersCount = jsLayers->size();
    for (int i = 0; i < layersCount; i++) {
      const JSONObject* jsLayer = jsLayers->getAsObject(i);
      if (jsLayer != NULL) {
        node->addLayer( createLayerNode(jsLayer) );
      }
    }

    processedKeys++;
  }

  checkProcessedKeys(jsonObject, processedKeys);

  return node;
}

SGGeometryNode* SceneJSShapesParser::createGeometryNode(const JSONObject* jsonObject) const {
  int processedKeys = 1; // "type" is already processed

  const std::string id = jsonObject->getAsString("id", "");
  if (id.compare("") != 0) {
    processedKeys++;
  }

  const std::string sId = jsonObject->getAsString("sid", "");
  if (sId.compare("") != 0) {
    processedKeys++;
  }


  const JSONString* jsPrimitive = jsonObject->getAsString("primitive");
  int primitive = GLPrimitive::triangles(); // triangles is the default
  if (jsPrimitive != NULL) {
    const std::string strPrimitive = jsPrimitive->value();

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
    processedKeys++;
  }

  const JSONArray* jsPositions = jsonObject->getAsArray("positions");
  if (jsPositions == NULL) {
    ILogger::instance()->logError("Mandatory positions are not present");
    return NULL;
  }
  processedKeys++;
  int verticesCount = jsPositions->size();
  IFloatBuffer* vertices = IFactory::instance()->createFloatBuffer(verticesCount);
  for (int i = 0; i < verticesCount; i++) {
    vertices->put(i, (float) jsPositions->getAsNumber(i)->value());
    _statistics->computeVertex();
  }

  const JSONArray* jsColors = jsonObject->getAsArray("colors");
  IFloatBuffer* colors = NULL;
  if (jsColors != NULL) {
    const int colorsCount = jsColors->size();
    colors = IFactory::instance()->createFloatBuffer(colorsCount);
    for (int i = 0; i < colorsCount; i++) {
      const float value = (float) jsColors->getAsNumber(i)->value();
      colors->put(i, value);
    }
    processedKeys++;
  }

  const JSONArray* jsUV = jsonObject->getAsArray("uv");
  IFloatBuffer* uv = NULL;
  if (jsUV != NULL) {
    const int uvCount = jsUV->size();
    uv = IFactory::instance()->createFloatBuffer(uvCount);
    bool isY = false;
    for (int i = 0; i < uvCount; i++) {
      float value = (float) jsUV->getAsNumber(i)->value();
      if (isY) {
        value = 1 - value;
      }
      isY = !isY;
      uv->put(i, value);
    }
    processedKeys++;
  }

  const JSONArray* jsNormals = jsonObject->getAsArray("normals");
  IFloatBuffer* normals = NULL;
  //TODO: WORKING JM
  if (jsNormals != NULL) {
    const int normalsCount = jsNormals->size();
    normals = IFactory::instance()->createFloatBuffer(normalsCount);
    for (int i = 0; i < normalsCount; i++) {
      float value = (float) jsNormals->getAsNumber(i)->value();
      normals->put(i, value);
    }
    processedKeys++;
  }

  const JSONArray* jsIndices = jsonObject->getAsArray("indices");
  if (jsIndices == NULL) {
    ILogger::instance()->logError("Non indexed geometries not supported");
    return NULL;
  }
  int indicesOutOfRange = 0;
  int indicesCount = jsIndices->size();
  IShortBuffer* indices = IFactory::instance()->createShortBuffer(indicesCount);
  for (int i = 0; i < indicesCount; i++) {
    const long long indice = (long long) jsIndices->getAsNumber(i)->value();
    if (indice > 32767) {
      indicesOutOfRange++;
    }
    indices->rawPut(i, (short) indice);
  }
  processedKeys++;

  if (indicesOutOfRange > 0) {
    ILogger::instance()->logError("SceneJSShapesParser: There are %d (of %d) indices out of range.",
                                  indicesOutOfRange,
                                  indicesCount);
  }

  SGGeometryNode* node = new SGGeometryNode(id, sId,
                                            primitive,
                                            vertices,
                                            colors,
                                            uv,
                                            normals,
                                            indices);

  processedKeys += parseChildren(jsonObject, node);

  checkProcessedKeys(jsonObject, processedKeys);

  return node;
}

SGNode* SceneJSShapesParser::toNode(const JSONBaseObject* jsonBaseObject) const {

  if (jsonBaseObject == NULL) {
    return NULL;
  }

  const JSONObject* jsonObject = jsonBaseObject->asObject();

  SGNode* result = NULL;

  if (jsonObject != NULL) {
    const JSONString* jsType = jsonObject->getAsString("type");
    if (jsType != NULL) {
      const std::string type = jsType->value();
      if (type.compare("node") == 0) {
        result = createNode(jsonObject);
        _statistics->computeNode();
      }
      else if (type.compare("rotate") == 0) {
        result = createRotateNode(jsonObject);
      }
      else if (type.compare("translate") == 0) {
        result = createTranslateNode(jsonObject);
      }
      else if (type.compare("material") == 0) {
        result = createMaterialNode(jsonObject);
        _statistics->computeMaterial();
      }
      else if (type.compare("texture") == 0) {
        result = createTextureNode(jsonObject);
      }
      else if (type.compare("geometry") == 0) {
        result = createGeometryNode(jsonObject);
        _statistics->computeGeometry();
      }
      else {
        ILogger::instance()->logWarning("SceneJS: Unknown type \"%s\"", type.c_str());
      }
    }
  }
  
  return result;
}
