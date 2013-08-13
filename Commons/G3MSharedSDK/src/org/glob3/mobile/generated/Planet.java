package org.glob3.mobile.generated; 
//
//  Planet.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 17/05/13.
//
//

//
//  Planet.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 31/05/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//



public abstract class Planet extends Disposable
{

  public static Planet createEarth()
  {
    return new EllipsoidalPlanet(new Ellipsoid(Vector3D.zero(), new Vector3D(6378137.0, 6378137.0, 6356752.314245)));
  }
  public static Planet createSphericalEarth()
  {
    return new SphericalPlanet(new Sphere(Vector3D.zero(), 6378137.0));
  }

  public void dispose()
  {
  super.dispose();

  }

  public abstract Vector3D getRadii();
  public abstract Vector3D centricSurfaceNormal(Vector3D positionOnEllipsoid);
  public abstract Vector3D geodeticSurfaceNormal(Vector3D positionOnEllipsoid);

  public abstract Vector3D geodeticSurfaceNormal(MutableVector3D positionOnEllipsoid);


  public abstract Vector3D geodeticSurfaceNormal(Angle latitude, Angle longitude);

  public abstract Vector3D geodeticSurfaceNormal(Geodetic3D geodetic);
  public abstract Vector3D geodeticSurfaceNormal(Geodetic2D geodetic);
  public abstract java.util.ArrayList<Double> intersectionsDistances(Vector3D origin, Vector3D direction);

  public abstract Vector3D toCartesian(Angle latitude, Angle longitude, double height);

  public abstract Vector3D toCartesian(Geodetic3D geodetic);
  public abstract Vector3D toCartesian(Geodetic2D geodetic);
  public abstract Vector3D toCartesian(Geodetic2D geodetic, double height);
  public abstract Geodetic2D toGeodetic2D(Vector3D positionOnEllipsoid);

  public abstract Geodetic3D toGeodetic3D(Vector3D position);

  public abstract Vector3D scaleToGeodeticSurface(Vector3D position);

  public abstract Vector3D scaleToGeocentricSurface(Vector3D position);

  public abstract java.util.LinkedList<Vector3D> computeCurve(Vector3D start, Vector3D stop, double granularity);

  public abstract Geodetic2D getMidPoint (Geodetic2D P0, Geodetic2D P1);


  public abstract double computePreciseLatLonDistance(Geodetic2D g1, Geodetic2D g2);

  public abstract double computeFastLatLonDistance(Geodetic2D g1, Geodetic2D g2);

  public abstract Vector3D closestPointToSphere(Vector3D pos, Vector3D ray);

  public abstract Vector3D closestIntersection(Vector3D pos, Vector3D ray);


  public abstract MutableMatrix44D createGeodeticTransformMatrix(Geodetic3D position);

}