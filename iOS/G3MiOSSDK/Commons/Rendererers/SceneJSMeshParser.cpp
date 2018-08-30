//
//  SceneJSMeshParser.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/17/17.
//
//

#include "SceneJSMeshParser.hpp"

#include "SceneJSNodeParser.hpp"
#include "SGMesh.hpp"


SGMesh* SceneJSMeshParser::parseFromJSONBaseObject(const JSONBaseObject* jsonObject,
                                                   const std::string&    uriPrefix,
                                                   bool                  isTransparent,
                                                   bool                  depthTest) {
  SGNode* node = SceneJSNodeParser::parseFromJSONBaseObject(jsonObject, depthTest, true);
  return (node == NULL) ? NULL : new SGMesh(node, uriPrefix, isTransparent);
}

SGMesh* SceneJSMeshParser::parseFromJSON(const std::string& json,
                                         const std::string& uriPrefix,
                                         bool               isTransparent,
                                         bool               depthTest) {
  SGNode* node = SceneJSNodeParser::parseFromJSON(json, depthTest);
  return (node == NULL) ? NULL : new SGMesh(node, uriPrefix, isTransparent);
}

SGMesh* SceneJSMeshParser::parseFromJSON(const IByteBuffer* json,
                                         const std::string& uriPrefix,
                                         bool               isTransparent,
                                         bool               depthTest) {
  SGNode* node = SceneJSNodeParser::parseFromJSON(json, depthTest);
  return (node == NULL) ? NULL : new SGMesh(node, uriPrefix, isTransparent);
}

SGMesh* SceneJSMeshParser::parseFromBSON(const IByteBuffer* bson,
                                         const std::string& uriPrefix,
                                         bool               isTransparent,
                                         bool               depthTest) {
  SGNode* node = SceneJSNodeParser::parseFromBSON(bson, depthTest);
  return (node == NULL) ? NULL : new SGMesh(node, uriPrefix, isTransparent);
}
