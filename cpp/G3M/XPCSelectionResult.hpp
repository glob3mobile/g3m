//
//  XPCSelectionResult.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez-Deck on 1/22/21.
//

#ifndef XPCSelectionResult_hpp
#define XPCSelectionResult_hpp

class Ray;
class G3MRenderContext;
class GLState;
class Sphere;
#include "MutableVector3D.hpp"


class XPCSelectionResult {
private:
  mutable Sphere* _hotArea;
  double _minimumSquaredDistanceToRay;
  MutableVector3D _bestPoint;

public:
  const Ray* _ray;

  XPCSelectionResult(const Ray* ray);

  ~XPCSelectionResult();

//  const Ray* getRay() const;

  const Sphere* getHotArea() const;

  void render(const G3MRenderContext* rc,
              GLState* glState) const;

  bool isInterestedIn(const Sphere* area) const;

  bool evaluateCantidate(const MutableVector3D& point);

};

#endif
