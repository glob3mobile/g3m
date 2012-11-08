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

#include "CompositeShape.hpp"
#include "MeshShape.hpp"
#include "IndexedMesh.hpp"
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

  _rootShape = toShape(jsonRootObject);
}

Shape* SceneJSShapesParser::convertNode(const std::string& type,
                                        JSONObject* jsonObject) const {

  int processedKeys = 1; // "type" is already processed

  CompositeShape* result = new CompositeShape();

  JSONString* jsId = jsonObject->getAsString("id");
  if (jsId != NULL) {
    result->setId(jsId->value());
    processedKeys++;
  }

  JSONArray* jsNodes = jsonObject->getAsArray("nodes");
  if (jsNodes != NULL) {
    const int nodesCount = jsNodes->size();
    for (int i = 0; i < nodesCount; i++) {
      JSONObject* child = jsNodes->getAsObject(i);
      if (child != NULL) {
        Shape* childShape = toShape(child);
        if (childShape != NULL) {
          result->addShape(childShape);
        }
      }
    }
    processedKeys++;
  }

  std::vector<std::string> keys = jsonObject->keys();
  if (processedKeys != keys.size()) {
    for (int i = 0; i < keys.size(); i++) {
      printf("%s\n", keys.at(i).c_str());
    }

    ILogger::instance()->logWarning("Not all keys processed in node of type \"%s\"", type.c_str());
  }

  return result;
}

Shape* SceneJSShapesParser::convertGeometry(JSONObject* jsonObject) const {


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

//  JSONArray* jsNormals = jsonObject->getAsArray("normals");
//  JSONArray* jsUV = jsonObject->getAsArray("uv");

  
  JSONArray* jsPositions = jsonObject->getAsArray("positions");
  if (jsPositions == NULL) {
    ILogger::instance()->logError("Mandatory positions are not present");
    return NULL;
  }
  int verticesCount = jsPositions->size();
  IFloatBuffer* vertices = IFactory::instance()->createFloatBuffer(verticesCount);
  for (int i = 0; i < verticesCount; i++) {
    vertices->put(i, (float) jsPositions->getAsNumber(i)->value());
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


//  IndexedMesh(const int primitive,
//              bool owner,
//              const Vector3D& center,
//              IFloatBuffer* vertices,
//              IIntBuffer* indices,
//              float lineWidth,
//              Color* flatColor = NULL,
//              IFloatBuffer* colors = NULL,
//              const float colorsIntensity = (float)0.0);

  Mesh* mesh = new IndexedMesh(primitive,
                               true,
                               Vector3D::zero(),
                               vertices,
                               indices,
                               1,
                               Color::newFromRGBA(1, 0, 1, 1));
  
  MeshShape* result = new MeshShape(NULL, mesh);

  return result;
}

Shape* SceneJSShapesParser::toShape(JSONBaseObject* jsonBaseObject) const {

  if (jsonBaseObject == NULL) {
    return NULL;
  }

  int ____DIEGO_AT_WORK;
  JSONObject* jsonObject = jsonBaseObject->asObject();

  Shape* result = NULL;

  if (jsonObject != NULL) {
    JSONString* jsType = jsonObject->getAsString("type");
    if (jsType != NULL) {
      const std::string type = jsType->value();
      if (type.compare("node") == 0) {
        result = convertNode(type, jsonObject);
      }
      else if (type.compare("rotate") == 0) {
        result = convertNode(type, jsonObject);
      }
      else if (type.compare("translate") == 0) {
        result = convertNode(type, jsonObject);
      }
      else if (type.compare("material") == 0) {
        result = convertNode(type, jsonObject);
      }
      else if (type.compare("texture") == 0) {
        result = convertNode(type, jsonObject);
      }
      else if (type.compare("geometry") == 0) {
        result = convertGeometry(jsonObject);
      }
      else {
        ILogger::instance()->logWarning("Unknown type \"%s\"", type.c_str());
      }
    }

    delete jsonObject;
  }
  
  return result;
}
