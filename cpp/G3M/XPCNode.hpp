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

#include "RCObject.hpp"

class Sector;
class Sphere;
class G3MRenderContext;
class XPCPointCloud;
class GLState;
class Frustum;
class IDownloader;
class ByteBufferIterator;
class XPCPoint;


class XPCNode : public RCObject {
private:

  const std::string _id;

  const Sector* _sector;

  const double _minZ;
  const double _maxZ;

  std::vector<XPCNode*>* _children;
  size_t _childrenSize;

  std::vector<XPCPoint*>* _points;

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


  IDownloader* _downloader;
  long long    _contentRequestID;

  void loadContent(const XPCPointCloud* pointCloud,
                   const std::string& treeID,
                   const G3MRenderContext* rc);

  XPCNode(const std::string& id,
          const Sector* sector,
          const double minZ,
          const double maxZ);

  ~XPCNode();

public:

  static XPCNode* fromByteBufferIterator(ByteBufferIterator& it);


  long long render(const XPCPointCloud* pointCloud,
                   const std::string& treeID,
                   const G3MRenderContext* rc,
                   GLState* glState,
                   const Frustum* frustum,
                   long long nowInMS);

  const Sector* getSector() const;

  void errorDownloadingContent();

  void setContent(std::vector<XPCNode*>* children,
                  std::vector<XPCPoint*>* points);

};

#endif
