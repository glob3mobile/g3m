//
//  XPCSelectionResult.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez-Deck on 1/22/21.
//

#ifndef XPCSelectionResult_hpp
#define XPCSelectionResult_hpp

class XPCPointCloud;
class Ray;
class G3MRenderContext;
class GLState;
class Sphere;
class Planet;

#include "MutableVector3D.hpp"


class XPCSelectionResult {
private:
  double          _nearestSquaredDistance;
  MutableVector3D _nearestPoint;


  // point full id
#ifdef C_CODE
  const XPCPointCloud* _pointCloud;
#else
  XPCPointCloud* _pointCloud;
#endif
  std::string _treeID;
  std::string _nodeID;
  int _pointIndex;


  mutable Sphere* _selectionSphere;

public:
  const Ray* _ray;

  XPCSelectionResult(const Ray* ray);

  ~XPCSelectionResult();

  const Sphere* getSelectionSphere() const;

  const bool notifyPointCloud(const Planet* planet) const;

  void render(const G3MRenderContext* rc,
              GLState* glState) const;

  bool isInterestedIn(const Sphere* area) const;

  bool evaluateCantidate(const MutableVector3D& cartesianPoint,
                         const XPCPointCloud* pointCloud,
                         const std::string& treeID,
                         const std::string& nodeID,
                         const int pointIndex);

};

#endif
