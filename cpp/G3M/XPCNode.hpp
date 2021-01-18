//
//  XPCNode.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez-Deck on 1/15/21.
//

#ifndef XPCNode_hpp
#define XPCNode_hpp

#include <vector>
#include <string>

class Sector;
class Sphere;
class G3MRenderContext;
class XPCPointCloud;
class GLState;
class Frustum;


class XPCNode {
private:

  const std::string _id;

  const Sector* _sector;

  const double _minZ;
  const double _maxZ;

  const std::vector<XPCNode*>* _children;
  const size_t _childrenSize;

  Sphere* _bounds;
  const Sphere* getBounds(const G3MRenderContext* rc,
                          const XPCPointCloud* pointCloud);
  
  Sphere* calculateBounds(const G3MRenderContext* rc,
                          const XPCPointCloud* pointCloud);


  bool _renderedInPreviousFrame;

  double    _projectedArea;
  long long _projectedAreaTS;

  bool _loadedContent;
  bool _loadingContent;

public:

  XPCNode(const std::string& id,
          const Sector* sector,
          const double minZ,
          const double maxZ);

  ~XPCNode();

  long long render(const XPCPointCloud* pointCloud,
                   const G3MRenderContext* rc,
                   GLState* glState,
                   const Frustum* frustum,
                   long long nowInMS);

};

#endif
