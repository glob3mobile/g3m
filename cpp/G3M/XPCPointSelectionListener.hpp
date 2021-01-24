//
//  XPCPointSelectionListener.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez-Deck on 1/24/21.
//

#ifndef XPCPointSelectionListener_hpp
#define XPCPointSelectionListener_hpp

#include <string>

class XPCPointCloud;
class Vector3D;
class Geodetic3D;


class XPCPointSelectionListener {
public:

  virtual ~XPCPointSelectionListener() {
  }

  virtual bool onSelectedPoint(const XPCPointCloud* pointCloud,
                               const Vector3D& cartesian,
                               const Geodetic3D& geodetic,
                               const std::string& treeID,
                               const std::string& nodeID,
                               const int pointIndex,
                               const double distanceToRay) const = 0;

};

#endif
