package org.glob3.mobile.generated; 
//
//  Ellipsoid.hpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 15/05/13.
//
//





public abstract class Ellipsoid
{


  public void dispose()
  {
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