package org.glob3.mobile.generated; 
//
//  LayoutUtils.cpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo on 18/03/13.
//
//

//
//  LayoutUtils.hpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo on 18/03/13.
//
//




//class Geodetic3D;
//class Ellipsoid;


public class LayoutUtils
{
  private LayoutUtils()
  {
  }

  public static java.util.ArrayList<Geodetic3D> splitOverCircle(Ellipsoid ellipsoid, Geodetic3D center, double radiusInMeters, int splits)
  {
     return splitOverCircle(ellipsoid, center, radiusInMeters, splits, Angle.zero());
  }
  public static java.util.ArrayList<Geodetic3D> splitOverCircle(Ellipsoid ellipsoid, Geodetic3D center, double radiusInMeters, int splits, Angle startAngle)
  {
    java.util.ArrayList<Geodetic3D> result = new java.util.ArrayList<Geodetic3D>();
  
    final double startAngleInRadians = startAngle._radians;
    final double deltaInRadians = (IMathUtils.instance().pi() * 2.0) / splits;
    final Vector3D cartesianCenter = ellipsoid.toCartesian(center);
    final Vector3D normal = ellipsoid.geodeticSurfaceNormal(center);
    final Vector3D northInPlane = Vector3D.upZ().projectionInPlane(normal).normalized().times(radiusInMeters);
  
    for (int i = 0; i < splits; i++)
    {
      final double angleInRadians = startAngleInRadians + (deltaInRadians * i);
  
      final Vector3D cartesianPosition = northInPlane.rotateAroundAxis(normal, Angle.fromRadians(angleInRadians));
  
      result.add(new Geodetic3D(ellipsoid.toGeodetic3D(cartesianPosition)));
    }
  
    return result;
  }

}