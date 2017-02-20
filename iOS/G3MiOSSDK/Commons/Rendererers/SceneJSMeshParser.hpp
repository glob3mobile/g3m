//
//  SceneJSMeshParser.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/17/17.
//
//

#ifndef SceneJSMeshParser_hpp
#define SceneJSMeshParser_hpp

#include <string>

class SGMesh;
class JSONBaseObject;
class IByteBuffer;


class SceneJSMeshParser {
private:
  SceneJSMeshParser() {
  }

public:

  static SGMesh* parseFromJSONBaseObject(const JSONBaseObject* jsonObject,
                                         const std::string&    uriPrefix,
                                         bool                  isTransparent,
                                         bool                  depthTest);

  static SGMesh* parseFromJSON(const std::string& json,
                               const std::string& uriPrefix,
                               bool               isTransparent,
                               bool               depthTest);

  static SGMesh* parseFromJSON(const IByteBuffer* json,
                               const std::string& uriPrefix,
                               bool               isTransparent,
                               bool               depthTest);

  static SGMesh* parseFromBSON(const IByteBuffer* bson,
                               const std::string& uriPrefix,
                               bool               isTransparent,
                               bool               depthTest);

};

#endif
