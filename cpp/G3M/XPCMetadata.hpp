//
//  XPCMetadata.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez-Deck on 1/15/21.
//

#ifndef XPCMetadata_hpp
#define XPCMetadata_hpp

#include <vector>

class IByteBuffer;
class XPCPointCloud;
class G3MRenderContext;
class GLState;
class Frustum;
class XPCDimension;
class XPCTree;


class XPCMetadata {
public:
  
  static XPCMetadata* fromBuffer(const IByteBuffer* buffer);
  
  ~XPCMetadata();

  long long render(const XPCPointCloud* pointCloud,
                   const G3MRenderContext* rc,
                   GLState* glState,
                   const Frustum* frustum,
                   long long nowInMS,
                   bool renderDebug);

  const size_t getTreesCount() const;
  const XPCTree* getTree(const size_t i) const;

  const size_t getDimensionsCount() const;
  const XPCDimension* getDimension(const size_t i) const;


private:
  XPCMetadata(const XPCMetadata& that);

  const std::vector<XPCDimension*>* _dimensions;
  const std::vector<XPCTree*>*      _trees;
  const size_t                      _treesSize;
  
  XPCMetadata(const std::vector<XPCDimension*>* dimensions,
              const std::vector<XPCTree*>*      trees);

};

#endif
