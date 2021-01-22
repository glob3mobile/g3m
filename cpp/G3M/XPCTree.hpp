//
//  XPCTree.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez-Deck on 1/18/21.
//

#ifndef XPCTree_hpp
#define XPCTree_hpp

#include <string>

class XPCNode;
class XPCPointCloud;
class G3MRenderContext;
class GLState;
class Frustum;
class Sector;


class XPCTree {
private:
  const std::string _id;
  XPCNode*          _rootNode;

public:

  XPCTree(const std::string& id,
          XPCNode* rootNode);

  ~XPCTree();

  long long render(const XPCPointCloud* pointCloud,
                   const G3MRenderContext* rc,
                   GLState* glState,
                   const Frustum* frustum,
                   long long nowInMS,
                   bool renderDebug) const;

  const Sector* getSector() const;

};

#endif

