//
//  SceneJSShapesParser.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/7/12.
//
//

#include "SceneJSShapesParser.hpp"

#include "SceneJSNodeParser.hpp"
#include "SGShape.hpp"


SGShape* SceneJSShapesParser::parseFromJSONBaseObject(const JSONBaseObject* jsonObject,
                                                      const std::string&    uriPrefix,
                                                      bool                  isTransparent,
                                                      bool                  depthTest,
                                                      Geodetic3D*           position,
                                                      AltitudeMode          altitudeMode) {
  SGNode* node = SceneJSNodeParser::parseFromJSONBaseObject(jsonObject, depthTest);
  return (node == NULL) ? NULL : new SGShape(node, uriPrefix, isTransparent, position, altitudeMode);
}

SGShape* SceneJSShapesParser::parseFromJSON(const std::string& json,
                                            const std::string& uriPrefix,
                                            bool               isTransparent,
                                            bool               depthTest,
                                            Geodetic3D*        position,
                                            AltitudeMode       altitudeMode) {
  SGNode* node = SceneJSNodeParser::parseFromJSON(json, depthTest);
  return (node == NULL) ? NULL : new SGShape(node, uriPrefix, isTransparent, position, altitudeMode);
}

SGShape* SceneJSShapesParser::parseFromJSON(const IByteBuffer* json,
                                            const std::string& uriPrefix,
                                            bool               isTransparent,
                                            bool               depthTest,
                                            Geodetic3D*        position,
                                            AltitudeMode       altitudeMode) {
  SGNode* node = SceneJSNodeParser::parseFromJSON(json, depthTest);
  return (node == NULL) ? NULL : new SGShape(node, uriPrefix, isTransparent, position, altitudeMode);
}

SGShape* SceneJSShapesParser::parseFromBSON(const IByteBuffer* bson,
                                            const std::string& uriPrefix,
                                            bool               isTransparent,
                                            bool               depthTest,
                                            Geodetic3D*        position,
                                            AltitudeMode       altitudeMode) {
  SGNode* node = SceneJSNodeParser::parseFromBSON(bson, depthTest);
  return (node == NULL) ? NULL : new SGShape(node, uriPrefix, isTransparent, position, altitudeMode);
}
