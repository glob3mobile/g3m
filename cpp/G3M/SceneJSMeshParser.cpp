//
//  SceneJSMeshParser.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 2/17/17.
//
//

#include "SceneJSMeshParser.hpp"

#include "SceneJSNodeParser.hpp"
#include "SGMesh.hpp"


SGMesh* SceneJSMeshParser::parseFromJSON(const std::string&             json,
                                         const std::string&             uriPrefix,
                                         const bool                     isTransparent,
                                         const SceneJSParserParameters& parameters) {
  SGNode* node = SceneJSNodeParser::parseFromJSON(json, parameters);
  return (node == NULL) ? NULL : new SGMesh(node, uriPrefix, isTransparent);
}

SGMesh* SceneJSMeshParser::parseFromJSON(const IByteBuffer*             json,
                                         const std::string&             uriPrefix,
                                         const bool                     isTransparent,
                                         const SceneJSParserParameters& parameters) {
  SGNode* node = SceneJSNodeParser::parseFromJSON(json, parameters);
  return (node == NULL) ? NULL : new SGMesh(node, uriPrefix, isTransparent);
}

SGMesh* SceneJSMeshParser::parseFromBSON(const IByteBuffer*             bson,
                                         const std::string&             uriPrefix,
                                         const bool                     isTransparent,
                                         const SceneJSParserParameters& parameters) {
  SGNode* node = SceneJSNodeParser::parseFromBSON(bson, parameters);
  return (node == NULL) ? NULL : new SGMesh(node, uriPrefix, isTransparent);
}
