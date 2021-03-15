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
class DirectMesh;
class XPCSelectionResult;
class ITimer;
class XPCRenderingState;
class BoundingVolume;


class XPCNode : public RCObject {
private:

  const std::string _id;

  const Sector* _sector;

  const double _minHeight;
  const double _maxHeight;

  std::vector<XPCNode*>* _children;
  size_t _childrenSize;

  DirectMesh* _mesh;

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
  bool _canceled;

  IDownloader* _downloader;
  long long    _contentRequestID;

  void loadContent(const XPCPointCloud* pointCloud,
                   const std::string& treeID,
                   const G3MRenderContext* rc,
                   const bool isSelectedNode,
                   const BoundingVolume* selection,
                   const bool nodeFullInsideSelection,
                   const BoundingVolume* fence,
                   const bool nodeFullInsideFence);

  void cancelLoadContent();
  void unloadContent();
  void unloadChildren();

  XPCNode(const std::string& id,
          const Sector* sector,
          const double minHeight,
          const double maxHeight);

  ~XPCNode();

public:

  const std::string getID() const {
    return _id;
  }

  static XPCNode* fromByteBufferIterator(ByteBufferIterator& it);

  const Sector* getSector() const;

  const double getMinHeight() const;

  const double getMaxHeight() const;

  void errorDownloadingContent();

  void setContent(std::vector<XPCNode*>* children,
                  DirectMesh* mesh);

  bool isCanceled() const;


  long long render(const XPCPointCloud* pointCloud,
                   const std::string& treeID,
                   const G3MRenderContext* rc,
                   ITimer* lastSplitTimer,
                   GLState* glState,
                   const Frustum* frustum,
                   long long nowInMS,
                   bool renderDebug,
                   XPCRenderingState& renderingState,
                   const BoundingVolume* selection,
                   const BoundingVolume* fence);

  const bool selectPoints(XPCSelectionResult* selectionResult,
                          XPCPointCloud* pointCloud,
                          const std::string& treeID) const;

  void cancel();

  void clean();

};

#endif
