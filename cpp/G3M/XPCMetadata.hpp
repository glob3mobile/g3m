//
//  XPCMetadata.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez-Deck on 1/15/21.
//

#ifndef XPCMetadata_hpp
#define XPCMetadata_hpp

#include <vector>

class JSONObject;
class XPCPointCloud;
class G3MRenderContext;
class XPCDimension;
class XPCNode;
class GLState;
class Frustum;
class XPCPointColorizer;

class XPCMetadata {
public:
  
  static XPCMetadata* fromJSON(const JSONObject* jsonObject);
  
  ~XPCMetadata();


  long long render(const XPCPointCloud* pointCloud,
                   const G3MRenderContext* rc,
                   GLState* glState,
                   const Frustum* frustum,
                   long long nowInMS);

  
private:

  const std::vector<XPCDimension*>* _dimensions;
  const std::vector<XPCNode*>*      _rootNodes;
  const size_t                      _rootNodesSize;
  
  XPCMetadata(const std::vector<XPCDimension*>* dimensions,
              const std::vector<XPCNode*>* rootNodes);

};

#endif
