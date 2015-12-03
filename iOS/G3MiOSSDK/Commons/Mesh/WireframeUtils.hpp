//
//  WireframeUtils.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 1/12/15.
//
//

#ifndef WireframeUtils_hpp
#define WireframeUtils_hpp

#include <stdio.h>

#include "IShortBuffer.hpp"
#include "IndexedMesh.hpp"
#include "IndexedGeometryMesh.hpp"

class WireframeUtils{
public:
  
  static IShortBuffer* createIndicesFromTriangleStrip(const IShortBuffer* triangleStripIndices,
                                                      int lastIndex = -1);
  
  static AbstractMesh* createWireframeMesh(const IndexedGeometryMesh* mesh, int lastVertex = -1, int lastIndex = -1);
  
  
};

#endif /* WireframeUtils_hpp */
