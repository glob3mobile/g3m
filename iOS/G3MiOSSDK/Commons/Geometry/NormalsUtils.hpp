//
//  NormalsUtils.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/15/13.
//
//

#ifndef __G3MiOSSDK__NormalsUtils__
#define __G3MiOSSDK__NormalsUtils__

class IFloatBuffer;
class IShortBuffer;

#include "Vector3F.cpp"

class NormalsUtils {
private:
  NormalsUtils() {
  }

  inline static Vector3F getVertex(const IFloatBuffer* vertices,
                                   short index);

public:

  static IFloatBuffer* createTriangleSmoothNormals(const IFloatBuffer* vertices,
                                                   const IShortBuffer* indices);

//  static IFloatBuffer* createTriangleStripSmoothNormals(const IFloatBuffer* vertices,
//                                                        const IShortBuffer* indices);

};

#endif
