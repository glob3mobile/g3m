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
#include "IIntBuffer.hpp"


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

  //  _rootShape = toShape(jsonRootObject);

  SGNode* node = toNode(jsonRootObject);

  _rootShape = new SGShape(node);

  delete jsonRootObject;
}

int SceneJSShapesParser::parseCommons(JSONObject* jsonObject,
                                      SGNode* node) const {
  int processedKeys = 0;

  JSONString* jsId = jsonObject->getAsString("id");
  if (jsId != NULL) {
    node->setId(jsId->value());
    processedKeys++;
  }

  JSONString* jsSId = jsonObject->getAsString("sid");
  if (jsSId != NULL) {
    node->setSId(jsSId->value());
    processedKeys++;
  }

  JSONArray* jsNodes = jsonObject->getAsArray("nodes");
  if (jsNodes != NULL) {
    const int nodesCount = jsNodes->size();
    for (int i = 0; i < nodesCount; i++) {
      JSONObject* child = jsNodes->getAsObject(i);
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

void SceneJSShapesParser::checkProcessedKeys(JSONObject* jsonObject,
                                             int processedKeys) const {
  std::vector<std::string> keys = jsonObject->keys();
  if (processedKeys != keys.size()) {
    for (int i = 0; i < keys.size(); i++) {
      printf("%s\n", keys.at(i).c_str());
    }

    ILogger::instance()->logWarning("Not all keys processed in node, processed %i of %i",
                                    processedKeys,
                                    keys.size());
  }
}

SGNode* SceneJSShapesParser::createNode(JSONObject* jsonObject) const {

  int processedKeys = 1; // "type" is already processed

  SGNode* node = new SGNode();

  processedKeys += parseCommons(jsonObject, node);

  //  std::vector<std::string> keys = jsonObject->keys();
  //  if (processedKeys != keys.size()) {
  //    for (int i = 0; i < keys.size(); i++) {
  //      printf("%s\n", keys.at(i).c_str());
  //    }
  //
  ////    ILogger::instance()->logWarning("Not all keys processed in node of type \"%s\"", type.c_str());
  //    ILogger::instance()->logWarning("Not all keys processed in node");
  //  }
  //

  checkProcessedKeys(jsonObject, processedKeys);

  return node;
}

SGRotateNode* SceneJSShapesParser::createRotateNode(JSONObject* jsonObject) const {
  int processedKeys = 1; // "type" is already processed

  SGRotateNode* node = new SGRotateNode();

  processedKeys += parseCommons(jsonObject, node);

  checkProcessedKeys(jsonObject, processedKeys);

  return node;
}

SGTranslateNode* SceneJSShapesParser::createTranslateNode(JSONObject* jsonObject) const {
  int processedKeys = 1; // "type" is already processed

  SGTranslateNode* node = new SGTranslateNode();

  processedKeys += parseCommons(jsonObject, node);

  checkProcessedKeys(jsonObject, processedKeys);

  return node;
}

SGMaterialNode* SceneJSShapesParser::createMaterialNode(JSONObject* jsonObject) const {
  int processedKeys = 1; // "type" is already processed

  SGMaterialNode* node = new SGMaterialNode();

  processedKeys += parseCommons(jsonObject, node);

  JSONObject* jsSpecularColor = jsonObject->getAsObject("specularColor");
  if (jsSpecularColor != NULL) {
    const double r = jsSpecularColor->getAsNumber("r")->value();
    const double g = jsSpecularColor->getAsNumber("g")->value();
    const double b = jsSpecularColor->getAsNumber("b")->value();
    const double a = jsSpecularColor->getAsNumber("a")->value();
    node->setSpecularColor(Color::newFromRGBA((float) r, (float) g, (float) b, (float) a));
    processedKeys++;
  }

  checkProcessedKeys(jsonObject, processedKeys);

  return node;
}

SGLayerNode* SceneJSShapesParser::createLayerNode(JSONObject* jsonObject) const {
  int processedKeys = 1; // "type" is already processed

  SGLayerNode* node = new SGLayerNode();

  processedKeys += parseCommons(jsonObject, node);

  int ____DIEGO_AT_WORK;
//  JSONString* jsUri = jsonObject->getAsString("uri");
//  if (jsUri != NULL) {
//    node->setUri( jsUri->value() );
//    processedKeys++;
//  }
//
//  JSONString* jsApplyTo = jsonObject->getAsString("applyTo");
//  if (jsApplyTo != NULL) {
//    node->setApplyTo( jsApplyTo->value() );
//    processedKeys++;
//  }
//
//  JSONString* jsBlendMode = jsonObject->getAsString("blendMode");
//  if (jsBlendMode != NULL) {
//    node->setBlendMode( jsBlendMode->value() );
//    processedKeys++;
//  }
//
//  JSONBoolean* jsFlipY = jsonObject->getAsBoolean("flipY");
//  if (jsFlipY != NULL) {
//    node->setFlipY( jsFlipY->value() );
//    processedKeys++;
//  }
//
//  JSONString* jsMagFilter = jsonObject->getAsString("magFilter");
//  if (jsMagFilter != NULL) {
//    node->setMagFilter( jsMagFilter->value() );
//    processedKeys++;
//  }
//
//  JSONString* jsMinFilter = jsonObject->getAsString("minFilter");
//  if (jsMinFilter != NULL) {
//    node->setMinFilter( jsMinFilter->value() );
//    processedKeys++;
//  }
//
//  JSONString* jsWrapS = jsonObject->getAsString("wrapS");
//  if (jsWrapS != NULL) {
//    node->setWrapS( jsWrapS->value() );
//    processedKeys++;
//  }
//
//  JSONString* jsWrapT = jsonObject->getAsString("wrapT");
//  if (jsWrapT != NULL) {
//    node->setWrapT( jsWrapT->value() );
//    processedKeys++;
//  }

  checkProcessedKeys(jsonObject, processedKeys);

  return node;
}


SGTextureNode* SceneJSShapesParser::createTextureNode(JSONObject* jsonObject) const {
  int processedKeys = 1; // "type" is already processed

  SGTextureNode* node = new SGTextureNode();

  processedKeys += parseCommons(jsonObject, node);

  JSONArray* jsLayers = jsonObject->getAsArray("layers");
  if (jsLayers != NULL) {
    int layersCount = jsLayers->size();
    for (int i = 0; i < layersCount; i++) {
      JSONObject* jsLayer = jsLayers->getAsObject(i);
      if (jsLayer != NULL) {
        node->addLayer( createLayerNode(jsLayer) );
      }
    }

    processedKeys++;
  }

  checkProcessedKeys(jsonObject, processedKeys);

  return node;
}

SGGeometryNode* SceneJSShapesParser::createGeometryNode(JSONObject* jsonObject) const {
  int processedKeys = 1; // "type" is already processed

  JSONString* jsPrimitive = jsonObject->getAsString("primitive");
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
  }

  JSONArray* jsPositions = jsonObject->getAsArray("positions");
  if (jsPositions == NULL) {
    ILogger::instance()->logError("Mandatory positions are not present");
    return NULL;
  }
  processedKeys++;
  int verticesCount = jsPositions->size();
  IFloatBuffer* vertices = IFactory::instance()->createFloatBuffer(verticesCount);
  for (int i = 0; i < verticesCount; i++) {
    vertices->put(i, (float) jsPositions->getAsNumber(i)->value());
  }

  JSONArray* jsColors = jsonObject->getAsArray("colors");
  IFloatBuffer* colors = NULL;
  if (jsColors != NULL) {
    const int colorsCount = jsColors->size();
    colors = IFactory::instance()->createFloatBuffer(colorsCount);
    for (int i = 0; i < colorsCount; i++) {
      colors->put(i, (float) jsColors->getAsNumber(i)->value());
    }
    processedKeys++;
  }

  JSONArray* jsUV = jsonObject->getAsArray("uv");
  IFloatBuffer* uv = NULL;
  if (jsUV != NULL) {
    const int uvCount = jsUV->size();
    uv = IFactory::instance()->createFloatBuffer(uvCount);
    for (int i = 0; i < uvCount; i++) {
      uv->put(i, (float) jsUV->getAsNumber(i)->value());
    }
    processedKeys++;
  }

  JSONArray* jsNormals = jsonObject->getAsArray("normals");
  IFloatBuffer* normals = NULL;
  if (jsNormals != NULL) {
    processedKeys++;
  }

  JSONArray* jsIndices = jsonObject->getAsArray("indices");
  if (jsIndices == NULL) {
    ILogger::instance()->logError("Non indexed geometries not supported");
    return NULL;
  }
  int indicesCount = jsIndices->size();
  IIntBuffer* indices = IFactory::instance()->createIntBuffer(indicesCount);
  for (int i = 0; i < indicesCount; i++) {
    indices->put(i, (int) jsIndices->getAsNumber(i)->value());
  }
  processedKeys++;

  SGGeometryNode* node = new SGGeometryNode(primitive,
                                            vertices,
                                            colors,
                                            uv,
                                            normals,
                                            indices);

  processedKeys += parseCommons(jsonObject, node);

  checkProcessedKeys(jsonObject, processedKeys);

  return node;
}



SGNode* SceneJSShapesParser::toNode(JSONBaseObject* jsonBaseObject) const {

  if (jsonBaseObject == NULL) {
    return NULL;
  }

  int ____DIEGO_AT_WORK;
  JSONObject* jsonObject = jsonBaseObject->asObject();

  SGNode* result = NULL;

  if (jsonObject != NULL) {
    JSONString* jsType = jsonObject->getAsString("type");
    if (jsType != NULL) {
      const std::string type = jsType->value();
      if (type.compare("node") == 0) {
        result = createNode(jsonObject);
      }
      else if (type.compare("rotate") == 0) {
        result = createRotateNode(jsonObject);
      }
      else if (type.compare("translate") == 0) {
        result = createTranslateNode(jsonObject);
      }
      else if (type.compare("material") == 0) {
        result = createMaterialNode(jsonObject);
      }
      else if (type.compare("texture") == 0) {
        result = createTextureNode(jsonObject);
      }
      else if (type.compare("geometry") == 0) {
        result = createGeometryNode(jsonObject);
      }
      else {
        ILogger::instance()->logWarning("Unknown type \"%s\"", type.c_str());
      }
    }
  }
  
  return result;
}
