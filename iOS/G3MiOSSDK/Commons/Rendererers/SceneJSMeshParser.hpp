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

class SceneJSParserParameters;
class IByteBuffer;
class SGMesh;


class SceneJSMeshParser {
private:
  SceneJSMeshParser() {
  }

public:

  static SGMesh* parseFromJSON(const std::string&             json,
                               const std::string&             uriPrefix,
                               const bool                     isTransparent,
                               const SceneJSParserParameters& parameters);

  static SGMesh* parseFromJSON(const IByteBuffer*             json,
                               const std::string&             uriPrefix,
                               const bool                     isTransparent,
                               const SceneJSParserParameters& parameters);

  static SGMesh* parseFromBSON(const IByteBuffer*             bson,
                               const std::string&             uriPrefix,
                               const bool                     isTransparent,
                               const SceneJSParserParameters& parameters);

};

#endif
