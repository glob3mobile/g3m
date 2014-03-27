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



//class Effect;
//class Camera;
//class Sector;
//class Vector2I;
//class CoordinateSystem;

public abstract class Planet
{

  public static Planet createEarth()
  {
    return new EllipsoidalPlanet(new Ellipsoid(Vector3D.zero, new Vector3D(6378137.0, 6378137.0, 6356752.314245)));
  }
  public static Planet createSphericalEarth()
  {
    return new SphericalPlanet(new Sphere(Vector3D.zero, 6378137.0));
  }
  public static Planet createFlatEarth()
  {
    return new FlatPlanet(new Vector2D(4 *6378137.0, 2 *6378137.0));
  }


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

  /*virtual std::list<Vector3D> computeCurve(const Vector3D& start,
                                           const Vector3D& stop,
                                           double granularity) const = 0;*/

  public abstract Geodetic2D getMidPoint (Geodetic2D P0, Geodetic2D P1);


  public abstract double computePreciseLatLonDistance(Geodetic2D g1, Geodetic2D g2);

  public abstract double computeFastLatLonDistance(Geodetic2D g1, Geodetic2D g2);

  //virtual Vector3D closestPointToSphere(const Vector3D& pos, const Vector3D& ray) const = 0;

  public abstract Vector3D closestIntersection(Vector3D pos, Vector3D ray);


  public abstract MutableMatrix44D createGeodeticTransformMatrix(Geodetic3D position);

  public abstract boolean isFlat();

  //virtual void beginSingleDrag(const Vector3D& origin, const Vector3D& initialRay) const = 0;
  public abstract void beginSingleDrag(Vector3D origin, Vector3D touchedPosition);

  public abstract MutableMatrix44D singleDrag(Vector3D finalRay);

  public abstract Effect createEffectFromLastSingleDrag();

  public abstract void beginDoubleDrag(Vector3D origin, Vector3D centerRay, Vector3D centerPosition, Vector3D touchedPosition0, Vector3D touchedPosition1);

  public abstract MutableMatrix44D doubleDrag(Vector3D finalRay0, Vector3D finalRay1);

  public abstract Effect createDoubleTapEffect(Vector3D origin, Vector3D centerRay, Vector3D touchedPosition);

  public abstract double distanceToHorizon(Vector3D position);

  public abstract MutableMatrix44D drag(Geodetic3D origin, Geodetic3D destination);

  public abstract Vector3D getNorth();

  public abstract void applyCameraConstrainers(Camera previousCamera, Camera nextCamera);

  public abstract Geodetic3D getDefaultCameraPosition(Sector shownSector);

<<<<<<< HEAD
  public final MutableMatrix44D createTransformMatrix(Geodetic3D position, Angle heading, Angle pitch, Angle roll, Vector3D scale, Vector3D translation)
  {
    final MutableMatrix44D geodeticTransform = createGeodeticTransformMatrix(position);
    final MutableMatrix44D headingRotation = MutableMatrix44D.createRotationMatrix(heading, Vector3D.downZ());
    final MutableMatrix44D pitchRotation = MutableMatrix44D.createRotationMatrix(pitch, Vector3D.upX());
    final MutableMatrix44D rollRotation = MutableMatrix44D.createRotationMatrix(roll, Vector3D.upY());
    final MutableMatrix44D scaleM = MutableMatrix44D.createScaleMatrix(scale._x, scale._y, scale._z);
    final MutableMatrix44D translationM = MutableMatrix44D.createTranslationMatrix(translation._x, translation._y, translation._z);
    final MutableMatrix44D localTransform = headingRotation.multiply(pitchRotation).multiply(rollRotation).multiply(translationM).multiply(scaleM);
    return new MutableMatrix44D(geodeticTransform.multiply(localTransform));
=======
  public final CoordinateSystem getCoordinateSystemAt(Geodetic3D geo)
  {
  
    Vector3D origin = toCartesian(geo);
    Vector3D z = centricSurfaceNormal(origin);
    Vector3D y = getNorth().projectionInPlane(z);
    Vector3D x = y.cross(z);
  
    return new CoordinateSystem(x,y,z, origin);
>>>>>>> origin/purgatory
  }
}