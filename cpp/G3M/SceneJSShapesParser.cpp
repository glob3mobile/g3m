//
//  SceneJSShapesParser.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 11/7/12.
//
//

#include "SceneJSShapesParser.hpp"

#include "SceneJSNodeParser.hpp"
#include "Geodetic3D.hpp"
#include "SGShape.hpp"


SGShape* SceneJSShapesParser::parseFromJSON(const std::string&             json,
                                            const std::string&             uriPrefix,
                                            const bool                     isTransparent,
                                            const SceneJSParserParameters& parameters,
                                            const Geodetic3D&              position,
                                            const AltitudeMode             altitudeMode) {
  SGNode* node = SceneJSNodeParser::parseFromJSON(json, parameters);
  return (node == NULL) ? NULL : new SGShape(node, uriPrefix, isTransparent, new Geodetic3D(position), altitudeMode);
}

SGShape* SceneJSShapesParser::parseFromJSON(const IByteBuffer*             json,
                                            const std::string&             uriPrefix,
                                            const bool                     isTransparent,
                                            const SceneJSParserParameters& parameters,
                                            const Geodetic3D&              position,
                                            const AltitudeMode             altitudeMode) {
  SGNode* node = SceneJSNodeParser::parseFromJSON(json, parameters);
  return (node == NULL) ? NULL : new SGShape(node, uriPrefix, isTransparent, new Geodetic3D(position), altitudeMode);
}

SGShape* SceneJSShapesParser::parseFromBSON(const IByteBuffer*             bson,
                                            const std::string&             uriPrefix,
                                            const bool                     isTransparent,
                                            const SceneJSParserParameters& parameters,
                                            const Geodetic3D&              position,
                                            const AltitudeMode             altitudeMode) {
  SGNode* node = SceneJSNodeParser::parseFromBSON(bson, parameters);
  return (node == NULL) ? NULL : new SGShape(node, uriPrefix, isTransparent, new Geodetic3D(position), altitudeMode);
}
