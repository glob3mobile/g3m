//
//  G3MMeshParser.h
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/29/14.
//
//

#ifndef __G3MiOSSDK__G3MMeshParser__
#define __G3MiOSSDK__G3MMeshParser__

#include <map>
#include <vector>

class Color;
class JSONArray;
class JSONString;
class URL;
class G3MMeshMaterial;
class Mesh;
class Planet;
class JSONObject;
class Vector3F;
class Geodetic3D;
class IFloatBuffer;
class IShortBuffer;
class Vector3D;


class G3MMeshParser {
private:
  static Color* parseColor(const JSONArray* jsonColor);
  static URL*   parseURL(const JSONString* jsonURL);

  static std::map<std::string, G3MMeshMaterial*> parseMaterials(const JSONArray* jsonMaterials);

  static std::vector<Mesh*> parseMeshes(std::map<std::string, G3MMeshMaterial*>& materials,
                                        const JSONArray* jsonMeshes,
                                        const Planet* planet);


  static const Vector3F parseVector3F(const JSONArray* jsonArray);

  static const Geodetic3D parseGeodetic3D(const JSONArray* jsonArray);

  static IFloatBuffer* parseFloatBuffer(const JSONArray* jsonArray);
  static IShortBuffer* parseShortBuffer(const JSONArray* jsonArray);

  static IFloatBuffer* parseGeodeticFloatBuffer(const JSONArray* jsonArray,
                                                const Geodetic3D& geodeticCenter,
                                                const Vector3D& center,
                                                const Planet* planet);

  static int toGLPrimitive(const std::string& primitive);

public:
  static std::vector<Mesh*> parse(const JSONObject* jsonObject,
                                  const Planet* planet);

  static Mesh* parseMesh(std::map<std::string, G3MMeshMaterial*>& materials,
                         const JSONObject* jsonMesh,
                         const Planet* planet);

  static Mesh* parseMesh(const JSONObject* jsonMesh,
                         const Planet* planet);

};

#endif
