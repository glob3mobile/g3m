//
//  XPCMetadata.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez-Deck on 1/15/21.
//

#ifndef XPCMetadata_hpp
#define XPCMetadata_hpp

#include <vector>

#include "Geodetic3D.hpp"

class IByteBuffer;
class XPCPointCloud;
class G3MRenderContext;
class GLState;
class Frustum;
class XPCDimension;
class XPCTree;
class XPCSelectionResult;
class ITimer;
class BoundingVolume;


class XPCMetadata {
public:
  
  static XPCMetadata* fromBuffer(const IByteBuffer* buffer);

  const Geodetic3D _averagePosition;
  const double _minHeight;
  const double _maxHeight;


  ~XPCMetadata();

  long long render(const XPCPointCloud* pointCloud,
                   const G3MRenderContext* rc,
                   ITimer* lastSplitTimer,
                   GLState* glState,
                   const Frustum* frustum,
                   long long nowInMS,
                   bool renderDebug,
                   const BoundingVolume* selection,
                   const BoundingVolume* fence);

  const bool selectPoints(XPCSelectionResult* selectionResult,
                          XPCPointCloud* pointCloud) const;

  const size_t getTreesCount() const;
  const XPCTree* getTree(const size_t i) const;

  const size_t getDimensionsCount() const;
  const XPCDimension* getDimension(const size_t i) const;

  void cancel();

  void reloadNodes();

private:
  XPCMetadata(const XPCMetadata& that);

  const std::vector<XPCDimension*>* _dimensions;
  const std::vector<XPCTree*>*      _trees;
  const size_t                      _treesSize;
  
  XPCMetadata(const Geodetic3D& averagePosition,
              const double minHeight,
              const double maxHeight,
              const std::vector<XPCDimension*>* dimensions,
              const std::vector<XPCTree*>*      trees);

};

#endif
