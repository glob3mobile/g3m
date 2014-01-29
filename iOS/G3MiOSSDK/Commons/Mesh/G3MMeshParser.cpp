//
//  G3MMeshParser.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/29/14.
//
//

#include "G3MMeshParser.hpp"

#include "JSONObject.hpp"
#include "JSONArray.hpp"
#include "JSONString.hpp"
#include "G3MMeshMaterial.hpp"
#include "Color.hpp"
#include "URL.hpp"
#include "Vector3F.hpp"
#include "IFactory.hpp"
#include "IFloatBuffer.hpp"
#include "IShortBuffer.hpp"
#include "DirectMesh.hpp"
#include "IndexedMesh.hpp"
#include "Planet.hpp"

Color* G3MMeshParser::parseColor(const JSONArray* jsonColor) {
  if (jsonColor == NULL) {
    return NULL;
  }
  const float r = (float) jsonColor->getAsNumber(0, 0);
  const float g = (float) jsonColor->getAsNumber(1, 0);
  const float b = (float) jsonColor->getAsNumber(2, 0);
  const float a = (float) jsonColor->getAsNumber(3, 0);
  return Color::newFromRGBA(r, g, b, a);
}

URL* G3MMeshParser::parseURL(const JSONString* jsonURL) {
  if (jsonURL == NULL) {
    return NULL;
  }
  return new URL( jsonURL->value() );
}

std::map<std::string, G3MMeshMaterial*> G3MMeshParser::parseMaterials(const JSONArray* jsonMaterials) {
  std::map<std::string, G3MMeshMaterial*> result;

  if (jsonMaterials != NULL) {
    const int materialsSize = jsonMaterials->size();
    for (int i = 0; i < materialsSize; i++) {
      const JSONObject* jsonMaterial = jsonMaterials->getAsObject(i);

      const std::string id = jsonMaterial->getAsString("id")->value();
      const JSONArray* jsonColor = jsonMaterial->getAsArray("color");
      const JSONString* jsonTextureURL = jsonMaterial->getAsString("textureURL");

      result[id] = new G3MMeshMaterial(id,
                                       parseColor(jsonColor),
                                       parseURL(jsonTextureURL));
    }
  }

  return result;
}

const Vector3F G3MMeshParser::parseVector3F(const JSONArray* jsonArray) {
  if (jsonArray == NULL) {
    return Vector3F::zero();
  }
  if (jsonArray->size() < 3) {
    ILogger::instance()->logError("Vector3F invalid format");
    return Vector3F::zero();
  }

  const float x = (float) jsonArray->getAsNumber(0, 0);
  const float y = (float) jsonArray->getAsNumber(1, 0);
  const float z = (float) jsonArray->getAsNumber(2, 0);
  return Vector3F(x, y, z);
}


const Geodetic3D G3MMeshParser::parseGeodetic3D(const JSONArray* jsonArray) {
  if (jsonArray == NULL) {
    return Geodetic3D::zero();
  }
  const double longitudeInDegrees = jsonArray->getAsNumber(0, 0);
  const double latitudeInDegrees  = jsonArray->getAsNumber(1, 0);
  const double height             = jsonArray->getAsNumber(2, 0);
  return Geodetic3D::fromDegrees(latitudeInDegrees, longitudeInDegrees, height);
}


IFloatBuffer* G3MMeshParser::parseFloatBuffer(const JSONArray* jsonArray) {
  if (jsonArray == NULL) {
    return NULL;
  }
  const int size = jsonArray->size();
  IFloatBuffer* result = IFactory::instance()->createFloatBuffer(size);
  for (int i = 0; i < size; i++) {
    const float value = (float) jsonArray->getAsNumber(i, 0);
    result->rawPut(i, value);
  }
  return result;
}

IFloatBuffer* G3MMeshParser::parseGeodeticFloatBuffer(const JSONArray* jsonArray,
                                                      const Geodetic3D& geodeticCenter,
                                                      const Vector3D& center,
                                                      const Planet* planet) {
  if (jsonArray == NULL) {
    return NULL;
  }
  const int size = jsonArray->size();
  IFloatBuffer* result = IFactory::instance()->createFloatBuffer(size);
  for (int i = 0; i < size; i += 3) {
    const double longitudeInDegrees = jsonArray->getAsNumber(i,   0) + geodeticCenter._longitude._degrees;
    const double latitudeInDegrees  = jsonArray->getAsNumber(i+1, 0) + geodeticCenter._latitude._degrees;
    const double height             = jsonArray->getAsNumber(i+2, 0) + geodeticCenter._height;

    const Vector3D cartesian = planet->toCartesian(Angle::fromDegrees(latitudeInDegrees),
                                                   Angle::fromDegrees(longitudeInDegrees),
                                                   height);

    result->rawPut(i  , (float) (cartesian._x - center._x));
    result->rawPut(i+1, (float) (cartesian._y - center._y));
    result->rawPut(i+2, (float) (cartesian._z - center._z));
  }
  return result;
}

IShortBuffer* G3MMeshParser::parseShortBuffer(const JSONArray* jsonArray) {
  if (jsonArray == NULL) {
    return NULL;
  }
  const int size = jsonArray->size();
  IShortBuffer* result = IFactory::instance()->createShortBuffer(size);
  for (int i = 0; i < size; i++) {
    const short value = (short) jsonArray->getAsNumber(i, 0);
    result->rawPut(i, value);
  }
  return result;

}

int G3MMeshParser::toGLPrimitive(const std::string& primitive) {
  if (primitive == "Triangles") {
    return GLPrimitive::triangles();
  }
  else if (primitive == "TriangleStrip") {
    return GLPrimitive::triangleStrip();
  }
  else if (primitive == "TriangleFan") {
    return GLPrimitive::triangleFan();
  }
  else if (primitive == "Lines") {
    return GLPrimitive::lines();
  }
  else if (primitive == "LineStrip") {
    return GLPrimitive::lineStrip();
  }
  else if (primitive == "LineLoop") {
    return GLPrimitive::lineLoop();
  }
  else if (primitive == "Points") {
    return GLPrimitive::points();
  }
  else {
    ILogger::instance()->logError("Invalid primitive named \"%s\"", primitive.c_str());
    return GLPrimitive::triangles();
  }
}

Mesh* G3MMeshParser::parseMesh(std::map<std::string, G3MMeshMaterial*>& materials,
                               const JSONObject* jsonMesh,
                               const Planet* planet) {
  if (jsonMesh == NULL) {
    return NULL;
  }

  const std::string materialID = jsonMesh->getAsString("material", "");
  if (materials.find(materialID) == materials.end()) {
    ILogger::instance()->logError("Can't find material \"%s\"", materialID.c_str());
    return NULL;
  }
  G3MMeshMaterial* material = materials[materialID];

  const std::string primitive      = jsonMesh->getAsString("primitive", "Triangles");
  const float       pointSize      = (float) jsonMesh->getAsNumber("pointSize", 1);
  const float       lineWidth      = (float) jsonMesh->getAsNumber("lineWidth", 1);
  const bool        depthTest      = jsonMesh->getAsBoolean("depthTest", true);

  const std::string verticesFormat = jsonMesh->getAsString("verticesFormat", "Cartesian");
  const bool isGeodetic = (verticesFormat == "Geodetic");

  double centerX;
  double centerY;
  double centerZ;
  IFloatBuffer* vertices;
  if (isGeodetic) {
    const Geodetic3D geodeticCenter = parseGeodetic3D( jsonMesh->getAsArray("center") );

    const Vector3D cartesian = planet->toCartesian(geodeticCenter);

    centerX = cartesian._x;
    centerY = cartesian._y;
    centerZ = cartesian._z;

    vertices  = parseGeodeticFloatBuffer(jsonMesh->getAsArray("vertices"),
                                         geodeticCenter,
                                         Vector3D(centerX, centerY, centerZ),
                                         planet);
  }
  else {
    const Vector3F center = parseVector3F( jsonMesh->getAsArray("center") );
    centerX = center._x;
    centerY = center._y;
    centerZ = center._z;

    vertices  = parseFloatBuffer( jsonMesh->getAsArray("vertices") );
  }
  
//  const Vector3F    center         = parseVector3F(jsonMesh->getAsArray("center"));

//  IFloatBuffer* vertices  = parseFloatBuffer( jsonMesh->getAsArray("vertices") );

  IFloatBuffer* normals   = parseFloatBuffer( jsonMesh->getAsArray("normals")  );
  IFloatBuffer* colors    = parseFloatBuffer( jsonMesh->getAsArray("colors")   );
#warning TODO texCoords
  //IFloatBuffer* texCoords = parseFloatBuffer( jsonMesh->getAsArray("texCoords") );

  IShortBuffer* indices   = parseShortBuffer( jsonMesh->getAsArray("indices") );

  Mesh* mesh;
  if (indices == NULL) {
    mesh = new DirectMesh(toGLPrimitive(primitive),
                          true, // owner
                          Vector3D(centerX, centerY, centerZ),
                          vertices,
                          lineWidth,
                          pointSize,
                          material->_color, // flatColor
                          colors,
                          0, // colorsIntensity
                          depthTest,
                          normals);
  }
  else {
    mesh = new IndexedMesh(toGLPrimitive(primitive),
                           true, // owner
                           Vector3D(centerX, centerY, centerZ),
                           vertices,
                           indices,
                           lineWidth,
                           pointSize,
                           material->_color, // flatColor
                           colors,
                           0, // colorsIntensity
                           depthTest,
                           normals);
  }
  return mesh;
}

std::vector<Mesh*> G3MMeshParser::parseMeshes(std::map<std::string, G3MMeshMaterial*>& materials,
                                              const JSONArray* jsonMeshes,
                                              const Planet* planet) {
  std::vector<Mesh*> result;
  if (jsonMeshes != NULL) {
    const int jsonMeshesSize = jsonMeshes->size();
    for (int i = 0; i < jsonMeshesSize; i++) {
      Mesh* mesh = parseMesh(materials,
                             jsonMeshes->getAsObject(i),
                             planet);
      if (mesh != NULL) {
        result.push_back(mesh);
      }
    }
  }
  return result;
}


std::vector<Mesh*> G3MMeshParser::parse(const JSONObject* jsonObject,
                                        const Planet* planet) {
  if (jsonObject == NULL) {
    return std::vector<Mesh*>();
  }

  std::map<std::string, G3MMeshMaterial*> materials = parseMaterials( jsonObject->getAsArray("materials") );

  std::vector<Mesh*> meshes = parseMeshes(materials,
                                          jsonObject->getAsArray("meshes"),
                                          planet);

#ifdef C_CODE
  for (std::map<std::string, G3MMeshMaterial*>::iterator it = materials.begin();
       it != materials.end();
       ++it) {
    delete it->second;
  }
#endif
#ifdef JAVA_CODE
  for (final G3MMeshMaterial material : materials.values()) {
    material.dispose();
  }
#endif

  return meshes;
}
