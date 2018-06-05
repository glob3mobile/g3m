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
#include "Geodetic3D.hpp"

//class Shape;
class SGShape;
class JSONBaseObject;
class IByteBuffer;

class SceneJSShapesParser {
private:
    
  SceneJSShapesParser(){
      
  }
  

public:

  static SGShape* parseFromJSONBaseObject(const JSONBaseObject* jsonObject,
                                          const std::string& uriPrefix,
                                          bool isTransparent,
                                          bool depthTest,
                                          Geodetic3D* position,
                                          AltitudeMode altitudeMode);

  static SGShape* parseFromJSON(const std::string& json,
                                const std::string& uriPrefix,
                                bool isTransparent,
                                bool depthTest,
                                Geodetic3D* position,
                                AltitudeMode altitudeMode);

  static SGShape* parseFromJSON(const IByteBuffer* json,
                                const std::string& uriPrefix,
                                bool isTransparent,
                                bool depthTest,
                                Geodetic3D* position,
                                AltitudeMode altitudeMode);

  static SGShape* parseFromBSON(IByteBuffer* bson,
                                const std::string& uriPrefix,
                                bool isTransparent,
                                bool depthTest,
                                Geodetic3D* position,
                                AltitudeMode altitudeMode);
  
};

#endif
