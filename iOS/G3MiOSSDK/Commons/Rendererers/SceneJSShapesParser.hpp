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

class SGShape;
class JSONBaseObject;
class Geodetic3D;
class IByteBuffer;


class SceneJSShapesParser {
private:
  SceneJSShapesParser() {

  }

public:

  static SGShape* parseFromJSONBaseObject(const JSONBaseObject* jsonObject,
                                          const std::string&    uriPrefix,
                                          bool                  isTransparent,
                                          bool                  depthTest,
                                          Geodetic3D*           position,
                                          AltitudeMode          altitudeMode);

  static SGShape* parseFromJSON(const std::string& json,
                                const std::string& uriPrefix,
                                bool               isTransparent,
                                bool               depthTest,
                                Geodetic3D*        position,
                                AltitudeMode       altitudeMode);

  static SGShape* parseFromJSON(const IByteBuffer* json,
                                const std::string& uriPrefix,
                                bool               isTransparent,
                                bool               depthTest,
                                Geodetic3D*        position,
                                AltitudeMode       altitudeMode);

  static SGShape* parseFromBSON(const IByteBuffer* bson,
                                const std::string& uriPrefix,
                                bool               isTransparent,
                                bool               depthTest,
                                Geodetic3D*        position,
                                AltitudeMode       altitudeMode);
  
};

#endif
