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
class XPCSelectionResult;
class ITimer;
class BoundingVolume;

#include "XPCRenderingState.hpp"


class XPCTree {
private:
  const std::string _id;
  XPCNode*          _rootNode;

  mutable XPCRenderingState _renderingState;

public:

  XPCTree(const std::string& id,
          XPCNode* rootNode);

  ~XPCTree();

  const Sector* getSector() const;

  const double getMinHeight() const;

  const double getMaxHeight() const;

  long long render(const XPCPointCloud* pointCloud,
                   const G3MRenderContext* rc,
                   ITimer* lastSplitTimer,
                   GLState* glState,
                   const Frustum* frustum,
                   long long nowInMS,
                   bool renderDebug,
                   const BoundingVolume* selection,
                   const BoundingVolume* fence) const;

  const bool selectPoints(XPCSelectionResult* selectionResult,
                          XPCPointCloud* pointCloud) const;

  void cancel() const;

  void cleanNodes();

};

#endif
