package org.glob3.mobile.generated;
//
//  Planet.cpp
//  G3M
//
//  Created by Jose Miguel SN on 17/05/13.
//
//

//
//  Planet.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 31/05/12.
//



//class Geodetic3D;
//class Vector3D;
//class MutableVector3D;
//class Geodetic2D;
//class Angle;
//class MutableMatrix44D;
//class Effect;
//class Camera;
//class Sector;
//class CoordinateSystem;
//class Ray;


public abstract class Planet
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

  public abstract void geodeticSurfaceNormal(Angle latitude, Angle longitude, MutableVector3D result);

  public final java.util.ArrayList<Double> intersectionsDistances(Vector3D origin, Vector3D direction)
  {
    return intersectionsDistances(origin._x, origin._y, origin._z, direction._x, direction._y, direction._z);
  }

  public final java.util.ArrayList<Double> intersectionsDistances(Vector3D origin, MutableVector3D direction)
  {
    return intersectionsDistances(origin._x, origin._y, origin._z, direction.x(), direction.y(), direction.z());
  }

  public abstract java.util.ArrayList<Double> intersectionsDistances(double originX, double originY, double originZ, double directionX, double directionY, double directionZ);

  public abstract Vector3D toCartesian(Angle latitude, Angle longitude, double height);
  public abstract Vector3D toCartesian(Geodetic3D geodetic);
  public abstract Vector3D toCartesian(Geodetic2D geodetic);
  public abstract Vector3D toCartesian(Geodetic2D geodetic, double height);

  public abstract void toCartesian(Angle latitude, Angle longitude, double height, MutableVector3D result);
  public abstract void toCartesian(Geodetic3D geodetic, MutableVector3D result);
  public abstract void toCartesian(Geodetic2D geodetic, MutableVector3D result);
  public abstract void toCartesian(Geodetic2D geodetic, double height, MutableVector3D result);


  public abstract Geodetic2D toGeodetic2D(Vector3D positionOnEllipsoid);

  public abstract Geodetic3D toGeodetic3D(Vector3D position);

  public abstract Vector3D scaleToGeodeticSurface(Vector3D position);

  public abstract Vector3D scaleToGeocentricSurface(Vector3D position);

  public abstract Geodetic2D getMidPoint (Geodetic2D P0, Geodetic2D P1);


  public abstract double computePreciseLatLonDistance(Geodetic2D g1, Geodetic2D g2);

  public abstract double computeFastLatLonDistance(Geodetic2D g1, Geodetic2D g2);

  public final Vector3D closestIntersection(Vector3D origin, Vector3D direction)
  {
    if (origin.isNan() || direction.isNan())
    {
      return Vector3D.NANV;
    }
    java.util.ArrayList<Double> distances = intersectionsDistances(origin._x, origin._y, origin._z, direction._x, direction._y, direction._z);
    if (distances.isEmpty())
    {
      return Vector3D.NANV;
    }
    return origin.add(direction.times(distances.get(0)));
  }

  public final Vector3D closestIntersection(Ray ray)
  {
    return closestIntersection(ray._origin, ray._direction);
  }

  public abstract MutableMatrix44D createGeodeticTransformMatrix(Angle latitude, Angle longitude, double height);

  public final MutableMatrix44D createGeodeticTransformMatrix(Geodetic3D position)
  {
    return createGeodeticTransformMatrix(position._latitude, position._longitude, position._height);
  }

  public abstract boolean isFlat();

  public abstract void beginSingleDrag(Vector3D origin, Vector3D initialRay);

  public abstract MutableMatrix44D singleDrag(Vector3D finalRay);

  public abstract Effect createEffectFromLastSingleDrag();

  public abstract void beginDoubleDrag(Vector3D origin, Vector3D centerRay, Vector3D initialRay0, Vector3D initialRay1);

  public abstract MutableMatrix44D doubleDrag(Vector3D finalRay0, Vector3D finalRay1);

  public abstract Effect createDoubleTapEffect(Vector3D origin, Vector3D centerRay, Vector3D tapRay);

  public abstract double distanceToHorizon(Vector3D position);

  public abstract MutableMatrix44D drag(Geodetic3D origin, Geodetic3D destination);

  public abstract Vector3D getNorth();

  public abstract void applyCameraConstrains(Camera previousCamera, Camera nextCamera);

  public abstract Geodetic3D getDefaultCameraPosition(Sector rendereSector);

  public final CoordinateSystem getCoordinateSystemAt(Geodetic3D position)
  {
  
    final Vector3D origin = toCartesian(position);
    final Vector3D z = centricSurfaceNormal(origin);
    final Vector3D y = getNorth().projectionInPlane(z);
    final Vector3D x = y.cross(z);
  
    return new CoordinateSystem(x, y, z, origin);
  }

  public abstract String getType();

}