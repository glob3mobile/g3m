//
//  Quadric.h
//  G3MiOSSDK
//
//  Created by Agustín Trujillo on 29/09/13.
//
//

#ifndef __G3MiOSSDK__Quadric__
#define __G3MiOSSDK__Quadric__

#include "MutableMatrix44D.hpp"
#include "Ellipsoid.hpp"


class Quadric {
  
private:
  MutableMatrix44D Q;
  
  Quadric(MutableMatrix44D M): Q(M) {}
  
  
public:
  static Quadric fromEllipsoid(const Ellipsoid& ellipsoid) {
    // assuming ellipsoid is centered on origin
    const Vector3D R = ellipsoid.getOneOverRadiiSquared();
    return Quadric(MutableMatrix44D(R.x(),  0,      0,      0,
                                    0,      R.y(),  0,      0,
                                    0,      0,      R.z(),  0,
                                    0,      0,      0,      -1));
    
  }
  
  Quadric transformBy(const MutableMatrix44D& M) const {
    MutableMatrix44D I = M.inversed();
    MutableMatrix44D T = I.transposed();
    return Quadric(T.multiply(Q).multiply(I));
  }
  
  std::vector<double> intersectionsDistances(const Vector3D& origin,
                                             const Vector3D& direction) const;
};

#endif /* defined(__G3MiOSSDK__Quadric__) */
