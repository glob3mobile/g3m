package org.glob3.mobile.generated;import java.util.*;

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
//



//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Effect;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Camera;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Sector;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Vector2I;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class CoordinateSystem;

public abstract class Planet
{

  public void dispose()
  {
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual Vector3D getRadii() const = 0;
  public abstract Vector3D getRadii();

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual Vector3D centricSurfaceNormal(const Vector3D& positionOnEllipsoid) const = 0;
  public abstract Vector3D centricSurfaceNormal(Vector3D positionOnEllipsoid);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual Vector3D geodeticSurfaceNormal(const Vector3D& positionOnEllipsoid) const = 0;
  public abstract Vector3D geodeticSurfaceNormal(Vector3D positionOnEllipsoid);
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual Vector3D geodeticSurfaceNormal(const MutableVector3D& positionOnEllipsoid) const = 0;
  public abstract Vector3D geodeticSurfaceNormal(MutableVector3D positionOnEllipsoid);
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual Vector3D geodeticSurfaceNormal(const Angle& latitude, const Angle& longitude) const = 0;
  public abstract Vector3D geodeticSurfaceNormal(Angle latitude, Angle longitude);
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual Vector3D geodeticSurfaceNormal(const Geodetic3D& geodetic) const = 0;
  public abstract Vector3D geodeticSurfaceNormal(Geodetic3D geodetic);
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual Vector3D geodeticSurfaceNormal(const Geodetic2D& geodetic) const = 0;
  public abstract Vector3D geodeticSurfaceNormal(Geodetic2D geodetic);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual void geodeticSurfaceNormal(const Angle& latitude, const Angle& longitude, MutableVector3D& result) const = 0;
  public abstract void geodeticSurfaceNormal(Angle latitude, Angle longitude, tangible.RefObject<MutableVector3D> result);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: java.util.ArrayList<double> intersectionsDistances(const Vector3D& origin, const Vector3D& direction) const
  public final java.util.ArrayList<Double> intersectionsDistances(Vector3D origin, Vector3D direction)
  {
	return intersectionsDistances(origin._x, origin._y, origin._z, direction._x, direction._y, direction._z);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: java.util.ArrayList<double> intersectionsDistances(const Vector3D& origin, const MutableVector3D& direction) const
  public final java.util.ArrayList<Double> intersectionsDistances(Vector3D origin, MutableVector3D direction)
  {
	return intersectionsDistances(origin._x, origin._y, origin._z, direction.x(), direction.y(), direction.z());
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual java.util.ArrayList<double> intersectionsDistances(double originX, double originY, double originZ, double directionX, double directionY, double directionZ) const = 0;
  public abstract java.util.ArrayList<Double> intersectionsDistances(double originX, double originY, double originZ, double directionX, double directionY, double directionZ);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual Vector3D toCartesian(const Angle& latitude, const Angle& longitude, const double height) const = 0;
  public abstract Vector3D toCartesian(Angle latitude, Angle longitude, double height);
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual Vector3D toCartesian(const Geodetic3D& geodetic) const = 0;
  public abstract Vector3D toCartesian(Geodetic3D geodetic);
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual Vector3D toCartesian(const Geodetic2D& geodetic) const = 0;
  public abstract Vector3D toCartesian(Geodetic2D geodetic);
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual Vector3D toCartesian(const Geodetic2D& geodetic, const double height) const = 0;
  public abstract Vector3D toCartesian(Geodetic2D geodetic, double height);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual void toCartesian(const Angle& latitude, const Angle& longitude, const double height, MutableVector3D& result) const = 0;
  public abstract void toCartesian(Angle latitude, Angle longitude, double height, tangible.RefObject<MutableVector3D> result);
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual void toCartesian(const Geodetic3D& geodetic, MutableVector3D& result) const = 0;
  public abstract void toCartesian(Geodetic3D geodetic, tangible.RefObject<MutableVector3D> result);
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual void toCartesian(const Geodetic2D& geodetic, MutableVector3D& result) const = 0;
  public abstract void toCartesian(Geodetic2D geodetic, tangible.RefObject<MutableVector3D> result);
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual void toCartesian(const Geodetic2D& geodetic, const double height, MutableVector3D& result) const = 0;
  public abstract void toCartesian(Geodetic2D geodetic, double height, tangible.RefObject<MutableVector3D> result);


//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual Geodetic2D toGeodetic2D(const Vector3D& positionOnEllipsoid) const = 0;
  public abstract Geodetic2D toGeodetic2D(Vector3D positionOnEllipsoid);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual Geodetic3D toGeodetic3D(const Vector3D& position) const = 0;
  public abstract Geodetic3D toGeodetic3D(Vector3D position);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual Vector3D scaleToGeodeticSurface(const Vector3D& position) const = 0;
  public abstract Vector3D scaleToGeodeticSurface(Vector3D position);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual Vector3D scaleToGeocentricSurface(const Vector3D& position) const = 0;
  public abstract Vector3D scaleToGeocentricSurface(Vector3D position);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual Geodetic2D getMidPoint (const Geodetic2D& P0, const Geodetic2D& P1) const = 0;
  public abstract Geodetic2D getMidPoint (Geodetic2D P0, Geodetic2D P1);


//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual double computePreciseLatLonDistance(const Geodetic2D& g1, const Geodetic2D& g2) const = 0;
  public abstract double computePreciseLatLonDistance(Geodetic2D g1, Geodetic2D g2);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual double computeFastLatLonDistance(const Geodetic2D& g1, const Geodetic2D& g2) const = 0;
  public abstract double computeFastLatLonDistance(Geodetic2D g1, Geodetic2D g2);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector3D closestIntersection(const Vector3D& pos, const Vector3D& ray) const
  public final Vector3D closestIntersection(Vector3D pos, Vector3D ray)
  {
	if (pos.isNan() || ray.isNan())
	{
	  return Vector3D.nan();
	}
	java.util.ArrayList<Double> distances = intersectionsDistances(pos._x, pos._y, pos._z, ray._x, ray._y, ray._z);
	if (distances.isEmpty())
	{
	  return Vector3D.nan();
	}
	return pos.add(ray.times(distances.get(0)));
  }


//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual MutableMatrix44D createGeodeticTransformMatrix(const Geodetic3D& position) const = 0;
  public abstract MutableMatrix44D createGeodeticTransformMatrix(Geodetic3D position);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual boolean isFlat() const = 0;
  public abstract boolean isFlat();

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual void beginSingleDrag(const Vector3D& origin, const Vector3D& initialRay) const = 0;
  public abstract void beginSingleDrag(Vector3D origin, Vector3D initialRay);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual MutableMatrix44D singleDrag(const Vector3D& finalRay) const = 0;
  public abstract MutableMatrix44D singleDrag(Vector3D finalRay);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual Effect* createEffectFromLastSingleDrag() const = 0;
  public abstract Effect createEffectFromLastSingleDrag();

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual void beginDoubleDrag(const Vector3D& origin, const Vector3D& centerRay, const Vector3D& initialRay0, const Vector3D& initialRay1) const = 0;
  public abstract void beginDoubleDrag(Vector3D origin, Vector3D centerRay, Vector3D initialRay0, Vector3D initialRay1);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual MutableMatrix44D doubleDrag(const Vector3D& finalRay0, const Vector3D& finalRay1) const = 0;
  public abstract MutableMatrix44D doubleDrag(Vector3D finalRay0, Vector3D finalRay1);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual Effect* createDoubleTapEffect(const Vector3D& origin, const Vector3D& centerRay, const Vector3D& tapRay) const = 0;
  public abstract Effect createDoubleTapEffect(Vector3D origin, Vector3D centerRay, Vector3D tapRay);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual double distanceToHorizon(const Vector3D& position) const = 0;
  public abstract double distanceToHorizon(Vector3D position);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual MutableMatrix44D drag(const Geodetic3D& origin, const Geodetic3D& destination) const = 0;
  public abstract MutableMatrix44D drag(Geodetic3D origin, Geodetic3D destination);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual Vector3D getNorth() const = 0;
  public abstract Vector3D getNorth();

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual void applyCameraConstrainers(const Camera* previousCamera, Camera* nextCamera) const = 0;
  public abstract void applyCameraConstrainers(Camera previousCamera, Camera nextCamera);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual Geodetic3D getDefaultCameraPosition(const Sector& rendereSector) const = 0;
  public abstract Geodetic3D getDefaultCameraPosition(Sector rendereSector);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: CoordinateSystem getCoordinateSystemAt(const Geodetic3D& geo) const
  public final CoordinateSystem getCoordinateSystemAt(Geodetic3D geo)
  {
  
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: Vector3D origin = toCartesian(geo);
	Vector3D origin = toCartesian(new Geodetic3D(geo));
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: Vector3D z = centricSurfaceNormal(origin);
	Vector3D z = centricSurfaceNormal(new Vector3D(origin));
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: Vector3D y = getNorth().projectionInPlane(z);
	Vector3D y = getNorth().projectionInPlane(new Vector3D(z));
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: Vector3D x = y.cross(z);
	Vector3D x = y.cross(new Vector3D(z));
  
	return new CoordinateSystem(x,y,z, origin);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual const String getType() const = 0;
  public abstract String getType();

}
