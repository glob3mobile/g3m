//
//  XPCSelectionResult.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez-Deck on 1/22/21.
//

#include "XPCSelectionResult.hpp"

#include "Ray.hpp"
#include "Color.hpp"
#include "Sphere.hpp"
#include "IMathUtils.hpp"

XPCSelectionResult::XPCSelectionResult(const Ray* ray) :
_ray(ray),
_hotArea(NULL),
_minimumSquaredDistanceToRay(NAND)
{
}

XPCSelectionResult::~XPCSelectionResult() {
  delete _ray;
  delete _hotArea;
}

//const Ray* XPCSelectionResult::getRay() const {
//  return _ray;
//}

void XPCSelectionResult::render(const G3MRenderContext* rc,
                                GLState* glState) const {
//  _ray->render(rc, glState, Color::YELLOW);

  getHotArea()->render(rc, glState, Color::YELLOW);
#warning TODO__ RENDER SELECTED POINT
}

bool XPCSelectionResult::isInterestedIn(const Sphere* area) const {
  if (ISNAN(_minimumSquaredDistanceToRay)) {
    return true;
  }
  if (_hotArea == NULL) {
    _hotArea = new Sphere(_bestPoint.asVector3D(),
                          IMathUtils::instance()->sqrt( _minimumSquaredDistanceToRay ) );
  }
  return getHotArea()->touchesSphere(area);
}

const Sphere* XPCSelectionResult::getHotArea() const {
  if (_hotArea == NULL) {
    _hotArea = new Sphere(_bestPoint.asVector3D(),
                          IMathUtils::instance()->sqrt( _minimumSquaredDistanceToRay ) );
  }
  return _hotArea;
}

bool XPCSelectionResult::evaluateCantidate(const MutableVector3D& point) {
  const double squaredDistanceToRay = _ray->squaredDistanceTo(point);
  if (
      ISNAN(_minimumSquaredDistanceToRay) ||
      (squaredDistanceToRay < _minimumSquaredDistanceToRay)
      ) {
    _bestPoint.copyFrom(point);
    _minimumSquaredDistanceToRay = squaredDistanceToRay;

    delete _hotArea;
    _hotArea = NULL;

    return true;
  }

  return false;
}
