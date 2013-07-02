package org.glob3.mobile.generated; 
//
//  Sphere.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 04/06/13.
//
//

//
//  Sphere.h
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 04/06/13.
//
//



public class Sphere extends Geometry3D
{

  private final Vector3D _center ;
  private final double _radius;

  private final double _radiusSquared;

  public Sphere(Vector3D center, double radius)
  {
     _center = new Vector3D(center);
     _radius = radius;
     _radiusSquared = radius *radius;
  }

  public final Vector3D getCenter()
  {
     return _center;
  }
  public final double getRadius()
  {
     return _radius;
  }
  public final double getRadiusSquared()
  {
     return _radiusSquared;
  }

}