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
//class Geodetic2D;
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
  
      final Vector3D finalVector = northInPlane.rotateAroundAxis(normal, Angle.fromRadians(angleInRadians));
      final Vector3D cartesianPosition = cartesianCenter.add(finalVector);
  
      result.add(new Geodetic3D(ellipsoid.toGeodetic3D(cartesianPosition)));
    }
  
    return result;
  }
  public static java.util.ArrayList<Geodetic2D> splitOverCircle(Ellipsoid ellipsoid, Geodetic2D center, double radiusInMeters, int splits)
  {
     return splitOverCircle(ellipsoid, center, radiusInMeters, splits, Angle.zero());
  }
  public static java.util.ArrayList<Geodetic2D> splitOverCircle(Ellipsoid ellipsoid, Geodetic2D center, double radiusInMeters, int splits, Angle startAngle)
  {
    java.util.ArrayList<Geodetic2D> result2D = new java.util.ArrayList<Geodetic2D>();
    java.util.ArrayList<Geodetic3D> result3D = splitOverCircle(ellipsoid, new Geodetic3D(center, 0), radiusInMeters, splits, startAngle);
  
    for (int i = 0; i < splits; i++)
    {
      result2D.add(new Geodetic2D(result3D.get(i).asGeodetic2D()));
      if (result3D.get(i) != null)
         result3D.get(i).dispose();
    }
  
    return result2D;
  }
}