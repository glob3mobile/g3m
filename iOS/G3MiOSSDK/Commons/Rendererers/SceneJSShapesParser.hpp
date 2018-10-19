//
//  SceneJSShapesParser.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/7/12.
//
//

#ifndef __G3MiOSSDK__SceneJSShapesParser__
#define __G3MiOSSDK__SceneJSShapesParser__

#include <string>

#include "AltitudeMode.hpp"

class SceneJSParserParameters;
class Geodetic3D;
class IByteBuffer;
class SGShape;


class SceneJSShapesParser {
private:
  SceneJSShapesParser() {

  }

public:

  static SGShape* parseFromJSON(const std::string&             json,
                                const std::string&             uriPrefix,
                                const bool                     isTransparent,
                                const SceneJSParserParameters& parameters,
                                const Geodetic3D&              position,
                                const AltitudeMode             altitudeMode);

  static SGShape* parseFromJSON(const IByteBuffer*             json,
                                const std::string&             uriPrefix,
                                const bool                     isTransparent,
                                const SceneJSParserParameters& parameters,
                                const Geodetic3D&              position,
                                const AltitudeMode             altitudeMode);

  static SGShape* parseFromBSON(const IByteBuffer*             bson,
                                const std::string&             uriPrefix,
                                const bool                     isTransparent,
                                const SceneJSParserParameters& parameters,
                                const Geodetic3D&              position,
                                const AltitudeMode             altitudeMode);
  
};

#endif
