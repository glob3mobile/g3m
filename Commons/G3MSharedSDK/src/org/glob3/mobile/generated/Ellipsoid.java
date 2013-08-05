package org.glob3.mobile.generated; 
//
//  Ellipsoid.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 04/06/13.
//
//

//
//  Ellipsoid.h
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 04/06/13.
//
//



public class Ellipsoid
{
  private final Vector3D _center ;
  private final Vector3D _radii ;

  private final Vector3D _radiiSquared ;
  private final Vector3D _radiiToTheFourth ;
  private final Vector3D _oneOverRadiiSquared ;


  public Ellipsoid(Vector3D center, Vector3D radii)
  {
     _center = new Vector3D(center);
     _radii = new Vector3D(radii);
     _radiiSquared = new Vector3D(new Vector3D(radii._x * radii._x, radii._y * radii._y, radii._z * radii._z));
     _radiiToTheFourth = new Vector3D(new Vector3D(_radiiSquared._x * _radiiSquared._x, _radiiSquared._y * _radiiSquared._y, _radiiSquared._z * _radiiSquared._z));
     _oneOverRadiiSquared = new Vector3D(new Vector3D(1.0 / (radii._x * radii._x), 1.0 / (radii._y * radii._y), 1.0 / (radii._z * radii._z)));
  }

  public final Vector3D getCenter()
  {
    return _center;
  }

  public final Vector3D getRadii()
  {
    return _radii;
  }

  public final Vector3D getRadiiSquared()
  {
    return _radiiSquared;
  }

  public final Vector3D getRadiiToTheFourth()
  {
    return _radiiToTheFourth;
  }

  public final Vector3D getOneOverRadiiSquared()
  {
    return _oneOverRadiiSquared;
  }

  public final double getMeanRadius()
  {
    return (_radii._x + _radii._y + _radii._y) /3;
  }


}