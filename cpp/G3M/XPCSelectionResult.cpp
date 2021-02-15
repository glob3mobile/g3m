//
//  XPCSelectionResult.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez-Deck on 1/22/21.
//

#include "XPCSelectionResult.hpp"

#include "IMathUtils.hpp"
#include "Ray.hpp"
#include "Sphere.hpp"
#include "Color.hpp"
#include "ILogger.hpp"
#include "XPCNode.hpp"
#include "XPCPointCloud.hpp"
#include "Planet.hpp"
#include "Geodetic3D.hpp"


XPCSelectionResult::XPCSelectionResult(const Ray* ray) :
_ray(ray),
_selectionSphere(NULL),
_nearestSquaredDistance(NAND),
_pointCloud(NULL),
_treeID(""),
_nodeID(""),
_pointIndex(-1)
{
}

XPCSelectionResult::~XPCSelectionResult() {
  delete _ray;
  delete _selectionSphere;

  if (_pointCloud != NULL) {
    _pointCloud->_release();
  }
}

void XPCSelectionResult::render(const G3MRenderContext* rc,
                                GLState* glState) const {
  _ray->render(rc, glState, Color::YELLOW, 1);

  getSelectionSphere()->render(rc, glState, Color::YELLOW);
}

const Sphere* XPCSelectionResult::getSelectionSphere() const {
  if (_selectionSphere == NULL) {
    _selectionSphere = new Sphere(_nearestPoint.asVector3D(),
                                  IMathUtils::instance()->sqrt( _nearestSquaredDistance ) );
  }
  return _selectionSphere;
}

bool XPCSelectionResult::isInterestedIn(const Sphere* area) const {
  if (ISNAN(_nearestSquaredDistance)) {
    return true;
  }

  const double squaredDistanceToCenter = _ray->squaredDistanceTo(area->_center);

  return (squaredDistanceToCenter - area->getRadiusSquared()) < _nearestSquaredDistance;
}

bool XPCSelectionResult::evaluateCantidate(const MutableVector3D& cartesianPoint,
                                           XPCPointCloud* pointCloud,
                                           const std::string& treeID,
                                           const std::string& nodeID,
                                           const int pointIndex) {
  const double candidateSquaredDistance = _ray->squaredDistanceTo(cartesianPoint);
  if (ISNAN(_nearestSquaredDistance) ||
      (candidateSquaredDistance < _nearestSquaredDistance) ) {

    _nearestPoint.copyFrom(cartesianPoint);
    _nearestSquaredDistance = candidateSquaredDistance;

    if (pointCloud != _pointCloud) {
      if (_pointCloud != NULL) {
        _pointCloud->_release();
      }
      _pointCloud = pointCloud;
      if (_pointCloud != NULL) {
        _pointCloud->_retain();
      }
    }
    _treeID     = treeID;
    _nodeID     = nodeID;
    _pointIndex = pointIndex;

    delete _selectionSphere;
    _selectionSphere = NULL;

    return true;
  }

  return false;
}

const bool XPCSelectionResult::notifyPointCloud(const Planet* planet) {
  const Vector3D   cartesian = _nearestPoint.asVector3D();
  const Geodetic3D geodetic  = planet->toGeodetic3D(cartesian);

  return _pointCloud->selectedPoint(cartesian,
                                    geodetic,
                                    _treeID,
                                    _nodeID,
                                    _pointIndex,
                                    IMathUtils::instance()->sqrt( _nearestSquaredDistance ));
}
