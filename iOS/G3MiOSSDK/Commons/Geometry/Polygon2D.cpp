//
//  Polygon2D.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 24/3/16.
//
//

#include "Polygon2D.hpp"


bool Polygon2D::isEdgeInside(const int i,
                         const int j,
                         const std::vector<Vector2D*> remainingCorners,
                         bool counterClockWise) {
  //http://stackoverflow.com/questions/693837/how-to-determine-a-diagonal-is-in-or-out-of-a-concave-polygon
  
  const int nVertices = remainingCorners.size() - 1;
  
  int iadd1 = i + 1;
  int isub1 = i - 1;
  
  if (iadd1 == (nVertices + 1)) {
    iadd1 = 0;
  }
  if (isub1 == -1) {
    isub1 = nVertices - 1;
  }
  
#ifdef C_CODE
  const Vector2D v1 = remainingCorners[iadd1]->sub(*remainingCorners[i]);
  const Vector2D v2 = remainingCorners[isub1]->sub(*remainingCorners[i]);
  const Vector2D v3 = remainingCorners[j]->sub(*remainingCorners[i]);
#endif
#ifdef JAVA_CODE
  final Vector2D v1 = remainingCorners.get(iadd1).sub(remainingCorners.get(i));
  final Vector2D v2 = remainingCorners.get(isub1).sub(remainingCorners.get(i));
  final Vector2D v3 = remainingCorners.get(j).sub(remainingCorners.get(i));
#endif
  
  double av1 = v1.angle()._degrees;
  double av2 = v2.angle()._degrees;
  double av3 = v3.angle()._degrees;
  
  
  while (av1 < 0) {
    av1 += 360;
  }
  
  while (av2 < 0) {
    av2 += 360;
  }
  
  while (av3 < 0) {
    av3 += 360;
  }
  
  
  if (counterClockWise) {
    
    while (av1 > av2) {
      av2 += 360;
    }
    
    if ((av1 <= av3) && (av3 <= av2)) {
      return true;
    }
    av3 += 360;
    if ((av1 <= av3) && (av3 <= av2)) {
      return true;
    }
  }
  else {
    
    while (av1 < av2) {
      av1 += 360;
    }
    
    if ((av1 >= av3) && (av3 >= av2)) {
      return true;
    }
    av3 += 360;
    if ((av1 >= av3) && (av3 >= av2)) {
      return true;
    }
  }
  
  return false;
}

short Polygon2D::addTrianglesCuttingEars(ShortBufferBuilder& indexes, const short firstIndex) {
  
  //As seen in http://www.geometrictools.com/Documentation/TriangulationByEarClipping.pdf
  
  int i1 = 0, i2 = 0, i3 = 0;
  //   ILogger::instance()->logInfo("Looking for ears");
  
  std::vector<Vector2D*> remainingCorners;
  std::vector<short> remainingIndexes;
  
  for (int i = 0; i < _nVertices; i++) {
    remainingCorners.push_back(_coor2D[i]);
    remainingIndexes.push_back((short) (i + firstIndex));
  }
  
  while (remainingCorners.size() > 2) {
    
    bool earFound = false;
    for (int i = 0; i < (remainingCorners.size() - 1); i++) {
      
      i1 = i;
      i2 = (i + 1) % (remainingCorners.size());
      i3 = (i + 2) % (remainingCorners.size());
      
      const bool edgeInside = isEdgeInside(i1, i3, remainingCorners, _verticesCCW);
      if (!edgeInside) {
        //               ILogger::instance()->logInfo("T: %d, %d, %d -> Edge Not Inside", i1, i2, i3);
        continue;
      }
      
      const bool edgeIntersects = edgeIntersectsAnyOtherEdge(i1, i3, remainingCorners);
      if (edgeIntersects) {
        //               ILogger::instance()->logInfo("T: %d, %d, %d -> Edge Intersects", i1, i2, i3);
        continue;
      }
      
      const bool triangleContainsVertex = isAnyVertexInsideTriangle(i1, i2, i3, remainingCorners);
      if (triangleContainsVertex) {
        //               ILogger::instance()->logInfo("T: %d, %d, %d -> Triangle contains vertex", i1, i2, i3);
        continue;
      }
      
      //            ILogger::instance()->logInfo("T: %d, %d, %d -> IS EAR!", i1, i2, i3);
      earFound = true;
      break;
    }
    
    
    if (earFound) { //Valid triangle (ear)
      indexes.add(remainingIndexes[i1]);
      indexes.add(remainingIndexes[i2]);
      indexes.add(remainingIndexes[i3]);
      
      //Removing ear
#ifdef C_CODE
      remainingCorners.erase(remainingCorners.begin() + i2);
      remainingIndexes.erase(remainingIndexes.begin() + i2);
#endif
#ifdef JAVA_CODE
      remainingCorners.remove(i2);
      remainingIndexes.remove(i2);
#endif
    }
    else {
      ILogger::instance()->logError("NO EAR!!!!");
    }
    
  }
  return (short) (firstIndex + _nVertices);
}
