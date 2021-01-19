//
//  XPCTree.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez-Deck on 1/18/21.
//

#ifndef XPCTree_hpp
#define XPCTree_hpp

class XPCNode;
class XPCPointCloud;
class G3MRenderContext;
class GLState;
class Frustum;
class Sector;


class XPCTree {
private:
  const int _id;
  XPCNode*  _rootNode;

public:

  XPCTree(const int id,
          XPCNode* rootNode);

  ~XPCTree();

  long long render(const XPCPointCloud* pointCloud,
                   const G3MRenderContext* rc,
                   GLState* glState,
                   const Frustum* frustum,
                   long long nowInMS) const;

  const Sector* getSector() const;

};

#endif

