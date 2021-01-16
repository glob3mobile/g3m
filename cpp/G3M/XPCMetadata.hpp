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
class XPCDimension;
class XPCNode;
class XPCPointCloud;
class G3MRenderContext;
class GLState;
class Frustum;

class XPCMetadata {
public:
  
  static XPCMetadata* fromJSON(const JSONObject* jsonObject);
  
  ~XPCMetadata();


  long long render(const XPCPointCloud* pointCloud,
                   const G3MRenderContext* rc,
                   GLState* glState,
                   const Frustum* frustum,
                   const float pointSize,
                   const bool dynamicPointSize,
                   long long nowInMS);

  
private:

  const std::vector<XPCDimension*>* _extraDimensions;
  const std::vector<XPCNode*>*      _rootNodes;
  const size_t                      _rootNodesSize;
  
  XPCMetadata(const std::vector<XPCDimension*>* extraDimensions,
              const std::vector<XPCNode*>* rootNodes);
  
};

#endif
