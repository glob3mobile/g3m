package org.glob3.mobile.generated;import java.util.*;

//
//  SphericalPlanet.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 31/05/12.
//

//
//  SphericalPlanet.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 31/05/12.
//




public class SphericalPlanet extends Planet
{
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
  private final Sphere _sphere = new Sphere();
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
  public Sphere _sphere = new internal();
//#endif
  private final Vector3D _radii = new Vector3D();

  private MutableVector3D _origin = new MutableVector3D();
  private MutableVector3D _initialPoint = new MutableVector3D();
  private MutableVector3D _centerPoint = new MutableVector3D();
  private MutableVector3D _centerRay = new MutableVector3D();
  private MutableVector3D _initialPoint0 = new MutableVector3D();
  private MutableVector3D _initialPoint1 = new MutableVector3D();
  private MutableVector3D _lastDragAxis = new MutableVector3D();
  private double _lastDragRadians;
  private double _lastDragRadiansStep;
  private double _angleBetweenInitialRays;
  private double _angleBetweenInitialPoints;
  private boolean _validSingleDrag;


  public static Planet createEarth()
  {
	return new SphericalPlanet(new Sphere(Vector3D.zero, 6378137.0));
  }

  public SphericalPlanet(Sphere sphere)
  {
	  _sphere = new Sphere(sphere);
	  _radii = new Vector3D(new Vector3D(sphere._radius, sphere._radius, sphere._radius));
  }

  public void dispose()
  {
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
  super.dispose();
//#endif
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector3D getRadii() const
  public final Vector3D getRadii()
  {
	return _radii;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector3D centricSurfaceNormal(const Vector3D& position) const
  public final Vector3D centricSurfaceNormal(Vector3D position)
  {
	return position.normalized();
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector3D geodeticSurfaceNormal(const Vector3D& position) const
  public final Vector3D geodeticSurfaceNormal(Vector3D position)
  {
	return position.normalized();
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector3D geodeticSurfaceNormal(const MutableVector3D& position) const
  public final Vector3D geodeticSurfaceNormal(MutableVector3D position)
  {
	return position.normalized().asVector3D();
  }


//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector3D geodeticSurfaceNormal(const Angle& latitude, const Angle& longitude) const
  public final Vector3D geodeticSurfaceNormal(Angle latitude, Angle longitude)
  {
  //C++ TO JAVA CONVERTER TODO TASK: The #define macro COS was defined in alternate ways and cannot be replaced in-line:
	final double cosLatitude = COS(latitude._radians);
  
  //C++ TO JAVA CONVERTER TODO TASK: The #define macro SIN was defined in alternate ways and cannot be replaced in-line:
  //C++ TO JAVA CONVERTER TODO TASK: The #define macro COS was defined in alternate ways and cannot be replaced in-line:
	return new Vector3D(cosLatitude * COS(longitude._radians), cosLatitude * SIN(longitude._radians), SIN(latitude._radians));
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector3D geodeticSurfaceNormal(const Geodetic3D& geodetic) const
  public final Vector3D geodeticSurfaceNormal(Geodetic3D geodetic)
  {
	return geodeticSurfaceNormal(geodetic._latitude, geodetic._longitude);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector3D geodeticSurfaceNormal(const Geodetic2D& geodetic) const
  public final Vector3D geodeticSurfaceNormal(Geodetic2D geodetic)
  {
	return geodeticSurfaceNormal(geodetic._latitude, geodetic._longitude);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void geodeticSurfaceNormal(const Angle& latitude, const Angle& longitude, MutableVector3D& result) const
  public final void geodeticSurfaceNormal(Angle latitude, Angle longitude, tangible.RefObject<MutableVector3D> result)
  {
  //C++ TO JAVA CONVERTER TODO TASK: The #define macro COS was defined in alternate ways and cannot be replaced in-line:
	final double cosLatitude = COS(latitude._radians);
  
  //C++ TO JAVA CONVERTER TODO TASK: The #define macro SIN was defined in alternate ways and cannot be replaced in-line:
  //C++ TO JAVA CONVERTER TODO TASK: The #define macro COS was defined in alternate ways and cannot be replaced in-line:
	result.argvalue.set(cosLatitude * COS(longitude._radians), cosLatitude * SIN(longitude._radians), SIN(latitude._radians));
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: java.util.ArrayList<double> intersectionsDistances(double originX, double originY, double originZ, double directionX, double directionY, double directionZ) const
  public final java.util.ArrayList<Double> intersectionsDistances(double originX, double originY, double originZ, double directionX, double directionY, double directionZ)
  {
  
	return _sphere.intersectionsDistances(originX, originY, originZ, directionX, directionY, directionZ);
  //
  //  std::vector<double> intersections;
  //
  //  // By laborious algebraic manipulation....
  //  const double a = directionX * directionX  + directionY * directionY + directionZ * directionZ;
  //
  //  const double b = 2.0 * (originX * directionX + originY * directionY + originZ * directionZ);
  //
  //  const double c = originX * originX + originY * originY + originZ * originZ - _sphere._radiusSquared;
  //
  //  // Solve the quadratic equation: ax^2 + bx + c = 0.
  //  // Algorithm is from Wikipedia's "Quadratic equation" topic, and Wikipedia credits
  //  // Numerical Recipes in C, section 5.6: "Quadratic and Cubic Equations"
  //  const double discriminant = b * b - 4 * a * c;
  //  if (discriminant < 0.0) {
  //    // no intersections
  //    return intersections;
  //  }
  //  else if (discriminant == 0.0) {
  //    // one intersection at a tangent point
  //    //return new double[1] { -0.5 * b / a };
  //    intersections.push_back(-0.5 * b / a);
  //    return intersections;
  //  }
  //
  //  const double rootDiscriminant = IMathUtils::instance()->sqrt(discriminant);
  //  const double root1 = (-b + rootDiscriminant) / (2*a);
  //  const double root2 = (-b - rootDiscriminant) / (2*a);
  //
  //  // Two intersections - return the smallest first.
  //  if (root1 < root2) {
  //    intersections.push_back(root1);
  //    intersections.push_back(root2);
  //  }
  //  else {
  //    intersections.push_back(root2);
  //    intersections.push_back(root1);
  //  }
  //  return intersections;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector3D toCartesian(const Angle& latitude, const Angle& longitude, const double height) const
  public final Vector3D toCartesian(Angle latitude, Angle longitude, double height)
  {
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: return geodeticSurfaceNormal(latitude, longitude).times(_sphere._radius + height);
	return geodeticSurfaceNormal(new Angle(latitude), new Angle(longitude)).times(_sphere._radius + height);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector3D toCartesian(const Geodetic3D& geodetic) const
  public final Vector3D toCartesian(Geodetic3D geodetic)
  {
	return toCartesian(geodetic._latitude, geodetic._longitude, geodetic._height);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector3D toCartesian(const Geodetic2D& geodetic) const
  public final Vector3D toCartesian(Geodetic2D geodetic)
  {
	return toCartesian(geodetic._latitude, geodetic._longitude, 0.0);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector3D toCartesian(const Geodetic2D& geodetic, const double height) const
  public final Vector3D toCartesian(Geodetic2D geodetic, double height)
  {
	return toCartesian(geodetic._latitude, geodetic._longitude, height);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void toCartesian(const Angle& latitude, const Angle& longitude, const double height, MutableVector3D& result) const
  public final void toCartesian(Angle latitude, Angle longitude, double height, tangible.RefObject<MutableVector3D> result)
  {
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: geodeticSurfaceNormal(latitude, longitude, result);
	geodeticSurfaceNormal(new Angle(latitude), new Angle(longitude), result);
	final double nX = result.argvalue.x();
	final double nY = result.argvalue.y();
	final double nZ = result.argvalue.z();
  
	final double K = _sphere._radius + height;
	result.argvalue.set(nX * K, nY * K, nZ * K);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void toCartesian(const Geodetic3D& geodetic, MutableVector3D& result) const
  public final void toCartesian(Geodetic3D geodetic, tangible.RefObject<MutableVector3D> result)
  {
	toCartesian(geodetic._latitude, geodetic._longitude, geodetic._height, result);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void toCartesian(const Geodetic2D& geodetic, MutableVector3D& result) const
  public final void toCartesian(Geodetic2D geodetic, tangible.RefObject<MutableVector3D> result)
  {
	toCartesian(geodetic._latitude, geodetic._longitude, 0, result);
  }
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void toCartesian(const Geodetic2D& geodetic, const double height, MutableVector3D& result) const
  public final void toCartesian(Geodetic2D geodetic, double height, tangible.RefObject<MutableVector3D> result)
  {
	toCartesian(geodetic._latitude, geodetic._longitude, height, result);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Geodetic2D toGeodetic2D(const Vector3D& position) const
  public final Geodetic2D toGeodetic2D(Vector3D position)
  {
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: const Vector3D n = geodeticSurfaceNormal(position);
	final Vector3D n = geodeticSurfaceNormal(new Vector3D(position));
  
	final IMathUtils mu = IMathUtils.instance();
	return new Geodetic2D(Angle.fromRadians(mu.asin(n._z)), Angle.fromRadians(mu.atan2(n._y, n._x)));
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Geodetic3D toGeodetic3D(const Vector3D& position) const
  public final Geodetic3D toGeodetic3D(Vector3D position)
  {
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: const Vector3D p = scaleToGeodeticSurface(position);
	final Vector3D p = scaleToGeodeticSurface(new Vector3D(position));
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: const Vector3D h = position.sub(p);
	final Vector3D h = position.sub(new Vector3D(p));
  
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: const double height = (h.dot(position) < 0) ? -1 * h.length() : h.length();
	final double height = (h.dot(new Vector3D(position)) < 0) ? -1 * h.length() : h.length();
  
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: return Geodetic3D(toGeodetic2D(p), height);
	return new Geodetic3D(toGeodetic2D(new Vector3D(p)), height);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector3D scaleToGeodeticSurface(const Vector3D& position) const
  public final Vector3D scaleToGeodeticSurface(Vector3D position)
  {
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: return geodeticSurfaceNormal(position).times(_sphere._radius);
	return geodeticSurfaceNormal(new Vector3D(position)).times(_sphere._radius);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector3D scaleToGeocentricSurface(const Vector3D& position) const
  public final Vector3D scaleToGeocentricSurface(Vector3D position)
  {
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: return scaleToGeodeticSurface(position);
	return scaleToGeodeticSurface(new Vector3D(position));
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: java.util.LinkedList<Vector3D> computeCurve(const Vector3D& start, const Vector3D& stop, double granularity) const
  public final java.util.LinkedList<Vector3D> computeCurve(Vector3D start, Vector3D stop, double granularity)
  {
	if (granularity <= 0.0)
	{
	  //throw new ArgumentOutOfRangeException("granularity", "Granularity must be greater than zero.");
	  return new java.util.LinkedList<Vector3D>();
	}
  
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: const Vector3D normal = start.cross(stop).normalized();
	final Vector3D normal = start.cross(new Vector3D(stop)).normalized();
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: const double theta = start.angleBetween(stop)._radians;
	final double theta = start.angleBetween(new Vector3D(stop))._radians;
  
	int n = ((int)(theta / granularity) - 1) > 0 ? (int)(theta / granularity) - 1 : 0;
  
	java.util.LinkedList<Vector3D> positions = new java.util.LinkedList<Vector3D>();
  
	positions.addLast(start);
  
	for (int i = 1; i <= n; ++i)
	{
	  double phi = (i * granularity);
  
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: positions.push_back(scaleToGeocentricSurface(start.rotateAroundAxis(normal, Angle::fromRadians(phi))));
	  positions.addLast(scaleToGeocentricSurface(start.rotateAroundAxis(new Vector3D(normal), Angle.fromRadians(phi))));
	}
  
	positions.addLast(stop);
  
	return positions;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Geodetic2D getMidPoint (const Geodetic2D& P0, const Geodetic2D& P1) const
  public final Geodetic2D getMidPoint (Geodetic2D P0, Geodetic2D P1)
  {
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: const Vector3D v0 = toCartesian(P0);
	final Vector3D v0 = toCartesian(new Geodetic2D(P0));
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: const Vector3D v1 = toCartesian(P1);
	final Vector3D v1 = toCartesian(new Geodetic2D(P1));
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: const Vector3D normal = v0.cross(v1).normalized();
	final Vector3D normal = v0.cross(new Vector3D(v1)).normalized();
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: const Angle theta = v0.angleBetween(v1);
	final Angle theta = v0.angleBetween(new Vector3D(v1));
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: const Vector3D midPoint = scaleToGeocentricSurface(v0.rotateAroundAxis(normal, theta.times(0.5)));
	final Vector3D midPoint = scaleToGeocentricSurface(v0.rotateAroundAxis(new Vector3D(normal), theta.times(0.5)));
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: return toGeodetic2D(midPoint);
	return toGeodetic2D(new Vector3D(midPoint));
  }



  // compute distance from two points
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: double computePreciseLatLonDistance(const Geodetic2D& g1, const Geodetic2D& g2) const
  public final double computePreciseLatLonDistance(Geodetic2D g1, Geodetic2D g2)
  {
	final IMathUtils mu = IMathUtils.instance();
  
	final double R = _sphere._radius;
  
	// spheric distance from P to Q
	// this is the right form, but it's the most complex
	// theres is a minimum error considering SphericalPlanet instead of ellipsoid
	final double latP = g2._latitude._radians;
	final double lonP = g2._longitude._radians;
	final double latQ = g1._latitude._radians;
	final double lonQ = g1._longitude._radians;
	final double coslatP = mu.cos(latP);
	final double sinlatP = mu.sin(latP);
	final double coslonP = mu.cos(lonP);
	final double sinlonP = mu.sin(lonP);
	final double coslatQ = mu.cos(latQ);
	final double sinlatQ = mu.sin(latQ);
	final double coslonQ = mu.cos(lonQ);
	final double sinlonQ = mu.sin(lonQ);
	final double pq = (coslatP * sinlonP * coslatQ * sinlonQ + sinlatP * sinlatQ + coslatP * coslonP * coslatQ * coslonQ);
	return mu.acos(pq) * R;
  }


  // compute distance from two points
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: double computeFastLatLonDistance(const Geodetic2D& g1, const Geodetic2D& g2) const
  public final double computeFastLatLonDistance(Geodetic2D g1, Geodetic2D g2)
  {
	final IMathUtils mu = IMathUtils.instance();
  
	final double R = _sphere._radius;
  
	final double medLat = g1._latitude._degrees;
	final double medLon = g1._longitude._degrees;
  
	// this way is faster, and works properly further away from the poles
	double diflat = mu.abs(g2._latitude._degrees - medLat);
	if (diflat > 180)
	{
	  diflat = 360 - diflat;
	}
  
	double diflon = mu.abs(g2._longitude._degrees - medLon);
	if (diflon > 180)
	{
	  diflon = 360 - diflon;
	}
  
	double dist = mu.sqrt(diflat * diflat + diflon * diflon);
	return dist * DefineConstants.PI / 180 * R;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector3D closestPointToSphere(const Vector3D& pos, const Vector3D& ray) const
  public final Vector3D closestPointToSphere(Vector3D pos, Vector3D ray)
  {
	final IMathUtils mu = IMathUtils.instance();
  
	double t = 0;
  
	// compute radius for the rotation
	final double R0 = _sphere._radius;
  
	// compute the point in this ray that are to a distance R from the origin.
	final double U2 = ray.squaredLength();
	final double O2 = pos.squaredLength();
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: const double OU = pos.dot(ray);
	final double OU = pos.dot(new Vector3D(ray));
	final double a = U2;
	final double b = 2 * OU;
	final double c = O2 - R0 * R0;
	double rad = b * b - 4 * a * c;
  
	// if there is solution, the ray intersects the SphericalPlanet
	if (rad > 0)
	{
	  // compute the final point (the smaller positive t value)
	  t = (-b - mu.sqrt(rad)) / (2 * a);
	  if (t < 1)
		  t = (-b + mu.sqrt(rad)) / (2 * a);
	  // if the ideal ray intersects, but not the mesh --> case 2
	  if (t < 1)
		  rad = -12345;
	}
  
	// if no solution found, find a point in the contour line
	if (rad < 0)
	{
	  final double D = mu.sqrt(O2);
	  final double co2 = R0 * R0 / (D * D);
	  final double a_ = OU * OU - co2 * O2 * U2;
	  final double b_ = 2 * OU * O2 - co2 * 2 * OU * O2;
	  final double c_ = O2 * O2 - co2 * O2 * O2;
	  final double rad_ = b_ * b_ - 4 * a_ * c_;
	  t = (-b_ - mu.sqrt(rad_)) / (2 * a_);
	}
  
	// compute the final point
	Vector3D result = pos.add(ray.times(t));
	return result;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: MutableMatrix44D createGeodeticTransformMatrix(const Geodetic3D& position) const
  public final MutableMatrix44D createGeodeticTransformMatrix(Geodetic3D position)
  {
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: const MutableMatrix44D translation = MutableMatrix44D::createTranslationMatrix(toCartesian(position));
	final MutableMatrix44D translation = MutableMatrix44D.createTranslationMatrix(toCartesian(new Geodetic3D(position)));
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: const MutableMatrix44D rotation = MutableMatrix44D::createGeodeticRotationMatrix(position);
	final MutableMatrix44D rotation = MutableMatrix44D.createGeodeticRotationMatrix(new Geodetic3D(position));
  
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: return translation.multiply(rotation);
	return translation.multiply(new MutableMatrix44D(rotation));
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean isFlat() const
  public final boolean isFlat()
  {
	  return false;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void beginSingleDrag(const Vector3D& origin, const Vector3D& initialRay) const
  public final void beginSingleDrag(Vector3D origin, Vector3D initialRay)
  {
	_origin.copyFrom(origin);
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: _initialPoint.copyFrom(closestIntersection(origin, initialRay));
	_initialPoint.copyFrom(closestIntersection(new Vector3D(origin), new Vector3D(initialRay)));
	_validSingleDrag = false;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: MutableMatrix44D singleDrag(const Vector3D& finalRay) const
  public final MutableMatrix44D singleDrag(Vector3D finalRay)
  {
	// check if initialPoint is valid
	if (_initialPoint.isNan())
		return MutableMatrix44D.invalid();
  
	// compute final point
	final Vector3D origin = _origin.asVector3D();
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: MutableVector3D finalPoint = closestIntersection(origin, finalRay).asMutableVector3D();
	MutableVector3D finalPoint = closestIntersection(new Vector3D(origin), new Vector3D(finalRay)).asMutableVector3D();
	if (finalPoint.isNan())
	{
	  //printf ("--invalid final point in drag!!\n");
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: finalPoint.copyFrom(closestPointToSphere(origin, finalRay));
	  finalPoint.copyFrom(closestPointToSphere(new Vector3D(origin), new Vector3D(finalRay)));
	  if (finalPoint.isNan())
	  {
		ILogger.instance().logWarning("SphericalPlanet::singleDrag-> finalPoint is NaN");
		return MutableMatrix44D.invalid();
	  }
	}
  
	// compute the rotation axis
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: const Vector3D rotationAxis = _initialPoint.cross(finalPoint).asVector3D();
	final Vector3D rotationAxis = _initialPoint.cross(new MutableVector3D(finalPoint)).asVector3D();
  
	// compute the angle
	double sinus = rotationAxis.length()/_initialPoint.length()/finalPoint.length();
	final Angle rotationDelta = Angle.fromRadians(-IMathUtils.instance().asin(sinus));
	if (rotationDelta.isNan())
		return MutableMatrix44D.invalid();
  
	// save params for possible inertial animations
	_lastDragAxis.copyFrom(rotationAxis);
	double radians = rotationDelta._radians;
	_lastDragRadiansStep = radians - _lastDragRadians;
	_lastDragRadians = radians;
	_validSingleDrag = true;
  
	// return rotation matrix
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: return MutableMatrix44D::createRotationMatrix(rotationDelta, rotationAxis);
	return MutableMatrix44D.createRotationMatrix(new Angle(rotationDelta), new Vector3D(rotationAxis));
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Effect* createEffectFromLastSingleDrag() const
  public final Effect createEffectFromLastSingleDrag()
  {
	if (!_validSingleDrag || _lastDragAxis.isNan())
		return null;
	return new RotateWithAxisEffect(_lastDragAxis.asVector3D(), Angle.fromRadians(_lastDragRadiansStep));
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void beginDoubleDrag(const Vector3D& origin, const Vector3D& centerRay, const Vector3D& initialRay0, const Vector3D& initialRay1) const
  public final void beginDoubleDrag(Vector3D origin, Vector3D centerRay, Vector3D initialRay0, Vector3D initialRay1)
  {
	_origin.copyFrom(origin);
	_centerRay.copyFrom(centerRay);
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: _initialPoint0.copyFrom(closestIntersection(origin, initialRay0));
	_initialPoint0.copyFrom(closestIntersection(new Vector3D(origin), new Vector3D(initialRay0)));
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: _initialPoint1.copyFrom(closestIntersection(origin, initialRay1));
	_initialPoint1.copyFrom(closestIntersection(new Vector3D(origin), new Vector3D(initialRay1)));
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: _angleBetweenInitialPoints = _initialPoint0.angleBetween(_initialPoint1)._degrees;
	_angleBetweenInitialPoints = _initialPoint0.angleBetween(new MutableVector3D(_initialPoint1))._degrees;
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: _centerPoint.copyFrom(closestIntersection(origin, centerRay));
	_centerPoint.copyFrom(closestIntersection(new Vector3D(origin), new Vector3D(centerRay)));
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: _angleBetweenInitialRays = initialRay0.angleBetween(initialRay1)._degrees;
	_angleBetweenInitialRays = initialRay0.angleBetween(new Vector3D(initialRay1))._degrees;
  
	// middle point in 3D
	Geodetic2D g0 = toGeodetic2D(_initialPoint0.asVector3D());
	Geodetic2D g1 = toGeodetic2D(_initialPoint1.asVector3D());
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: Geodetic2D g = getMidPoint(g0, g1);
	Geodetic2D g = getMidPoint(new Geodetic2D(g0), new Geodetic2D(g1));
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: _initialPoint.copyFrom(toCartesian(g));
	_initialPoint.copyFrom(toCartesian(new Geodetic2D(g)));
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: MutableMatrix44D doubleDrag(const Vector3D& finalRay0, const Vector3D& finalRay1) const
  public final MutableMatrix44D doubleDrag(Vector3D finalRay0, Vector3D finalRay1)
  {
	// test if initialPoints are valid
	if (_initialPoint0.isNan() || _initialPoint1.isNan())
	  return MutableMatrix44D.invalid();
  
	// init params
	final IMathUtils mu = IMathUtils.instance();
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: MutableVector3D positionCamera = _origin;
	MutableVector3D positionCamera = new MutableVector3D(_origin);
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: const double finalRaysAngle = finalRay0.angleBetween(finalRay1)._degrees;
	final double finalRaysAngle = finalRay0.angleBetween(new Vector3D(finalRay1))._degrees;
	final double factor = finalRaysAngle / _angleBetweenInitialRays;
	double dAccum = 0;
	double angle0;
	double angle1;
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: double distance = _origin.sub(_centerPoint).length();
	double distance = _origin.sub(new MutableVector3D(_centerPoint)).length();
  
	// compute estimated camera translation: step 0
	double d = distance*(factor-1)/factor;
	MutableMatrix44D translation = MutableMatrix44D.createTranslationMatrix(_centerRay.asVector3D().normalized().times(d));
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: positionCamera = positionCamera.transformedBy(translation, 1.0);
	positionCamera = positionCamera.transformedBy(new MutableMatrix44D(translation), 1.0);
	dAccum += d;
	{
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: const Vector3D point0 = closestIntersection(positionCamera.asVector3D(), finalRay0);
	  final Vector3D point0 = closestIntersection(positionCamera.asVector3D(), new Vector3D(finalRay0));
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: const Vector3D point1 = closestIntersection(positionCamera.asVector3D(), finalRay1);
	  final Vector3D point1 = closestIntersection(positionCamera.asVector3D(), new Vector3D(finalRay1));
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: angle0 = point0.angleBetween(point1)._degrees;
	  angle0 = point0.angleBetween(new Vector3D(point1))._degrees;
	  if ((angle0 != angle0))
		  return MutableMatrix44D.invalid();
	}
  
	// compute estimated camera translation: step 1
	d = mu.abs((distance-d)*0.3);
	if (angle0 < _angleBetweenInitialPoints)
		d*=-1;
	translation.copyValue(MutableMatrix44D.createTranslationMatrix(_centerRay.asVector3D().normalized().times(d)));
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: positionCamera = positionCamera.transformedBy(translation, 1.0);
	positionCamera = positionCamera.transformedBy(new MutableMatrix44D(translation), 1.0);
	dAccum += d;
	{
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: const Vector3D point0 = closestIntersection(positionCamera.asVector3D(), finalRay0);
	  final Vector3D point0 = closestIntersection(positionCamera.asVector3D(), new Vector3D(finalRay0));
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: const Vector3D point1 = closestIntersection(positionCamera.asVector3D(), finalRay1);
	  final Vector3D point1 = closestIntersection(positionCamera.asVector3D(), new Vector3D(finalRay1));
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: angle1 = point0.angleBetween(point1)._degrees;
	  angle1 = point0.angleBetween(new Vector3D(point1))._degrees;
	  if ((angle1 != angle1))
		  return MutableMatrix44D.invalid();
	}
  
	// compute estimated camera translation: steps 2..n until convergence
	//int iter=0;
	double precision = mu.pow(10, mu.log10(distance)-8.0);
	double angle_n1 = angle0;
	double angle_n = angle1;
	while (mu.abs(angle_n-_angleBetweenInitialPoints) > precision)
	{
	  // iter++;
	  if ((angle_n1-angle_n)/(angle_n-_angleBetweenInitialPoints) < 0)
		  d*=-0.5;
	  translation.copyValue(MutableMatrix44D.createTranslationMatrix(_centerRay.asVector3D().normalized().times(d)));
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: positionCamera = positionCamera.transformedBy(translation, 1.0);
	  positionCamera = positionCamera.transformedBy(new MutableMatrix44D(translation), 1.0);
	  dAccum += d;
	  angle_n1 = angle_n;
	  {
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: const Vector3D point0 = closestIntersection(positionCamera.asVector3D(), finalRay0);
		final Vector3D point0 = closestIntersection(positionCamera.asVector3D(), new Vector3D(finalRay0));
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: const Vector3D point1 = closestIntersection(positionCamera.asVector3D(), finalRay1);
		final Vector3D point1 = closestIntersection(positionCamera.asVector3D(), new Vector3D(finalRay1));
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: angle_n = point0.angleBetween(point1)._degrees;
		angle_n = point0.angleBetween(new Vector3D(point1))._degrees;
		if ((angle_n != angle_n))
			return MutableMatrix44D.invalid();
	  }
	}
	//if (iter>2) printf("-----------  iteraciones=%d  precision=%f angulo final=%.4f  distancia final=%.1f\n", iter, precision, angle_n, dAccum);
  
	// start to compound matrix
	MutableMatrix44D matrix = MutableMatrix44D.identity();
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy assignment (rather than a reference assignment) - this should be verified and a 'copyFrom' method should be created if it does not yet exist:
//ORIGINAL LINE: positionCamera = _origin;
	positionCamera.copyFrom(_origin);
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: MutableVector3D viewDirection = _centerRay;
	MutableVector3D viewDirection = new MutableVector3D(_centerRay);
	MutableVector3D ray0 = finalRay0.asMutableVector3D();
	MutableVector3D ray1 = finalRay1.asMutableVector3D();
  
	// drag from initialPoint to centerPoint
	{
	  Vector3D initialPoint = _initialPoint.asVector3D();
	  final Vector3D rotationAxis = initialPoint.cross(_centerPoint.asVector3D());
	  final Angle rotationDelta = Angle.fromRadians(- mu.acos(_initialPoint.normalized().dot(_centerPoint.normalized())));
	  if (rotationDelta.isNan())
		  return MutableMatrix44D.invalid();
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: MutableMatrix44D rotation = MutableMatrix44D::createRotationMatrix(rotationDelta, rotationAxis);
	  MutableMatrix44D rotation = MutableMatrix44D.createRotationMatrix(new Angle(rotationDelta), new Vector3D(rotationAxis));
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: positionCamera = positionCamera.transformedBy(rotation, 1.0);
	  positionCamera = positionCamera.transformedBy(new MutableMatrix44D(rotation), 1.0);
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: viewDirection = viewDirection.transformedBy(rotation, 0.0);
	  viewDirection = viewDirection.transformedBy(new MutableMatrix44D(rotation), 0.0);
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: ray0 = ray0.transformedBy(rotation, 0.0);
	  ray0 = ray0.transformedBy(new MutableMatrix44D(rotation), 0.0);
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: ray1 = ray1.transformedBy(rotation, 0.0);
	  ray1 = ray1.transformedBy(new MutableMatrix44D(rotation), 0.0);
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: matrix.copyValueOfMultiplication(rotation, matrix);
	  matrix.copyValueOfMultiplication(new MutableMatrix44D(rotation), new MutableMatrix44D(matrix));
	}
  
	// move the camera forward
	{
	  MutableMatrix44D translation2 = MutableMatrix44D.createTranslationMatrix(viewDirection.asVector3D().normalized().times(dAccum));
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: positionCamera = positionCamera.transformedBy(translation2, 1.0);
	  positionCamera = positionCamera.transformedBy(new MutableMatrix44D(translation2), 1.0);
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: matrix.copyValueOfMultiplication(translation2, matrix);
	  matrix.copyValueOfMultiplication(new MutableMatrix44D(translation2), new MutableMatrix44D(matrix));
	}
  
	// compute 3D point of view center
	Vector3D centerPoint2 = closestIntersection(positionCamera.asVector3D(), viewDirection.asVector3D());
  
	// compute middle point in 3D
	Vector3D P0 = closestIntersection(positionCamera.asVector3D(), ray0.asVector3D());
	Vector3D P1 = closestIntersection(positionCamera.asVector3D(), ray1.asVector3D());
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: Geodetic2D g = getMidPoint(toGeodetic2D(P0), toGeodetic2D(P1));
	Geodetic2D g = getMidPoint(toGeodetic2D(new Vector3D(P0)), toGeodetic2D(new Vector3D(P1)));
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: Vector3D finalPoint = toCartesian(g);
	Vector3D finalPoint = toCartesian(new Geodetic2D(g));
  
	// drag globe from centerPoint to finalPoint
	{
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: const Vector3D rotationAxis = centerPoint2.cross(finalPoint);
	  final Vector3D rotationAxis = centerPoint2.cross(new Vector3D(finalPoint));
	  final Angle rotationDelta = Angle.fromRadians(- mu.acos(centerPoint2.normalized().dot(finalPoint.normalized())));
	  if (rotationDelta.isNan())
		  return MutableMatrix44D.invalid();
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: MutableMatrix44D rotation = MutableMatrix44D::createRotationMatrix(rotationDelta, rotationAxis);
	  MutableMatrix44D rotation = MutableMatrix44D.createRotationMatrix(new Angle(rotationDelta), new Vector3D(rotationAxis));
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: positionCamera = positionCamera.transformedBy(rotation, 1.0);
	  positionCamera = positionCamera.transformedBy(new MutableMatrix44D(rotation), 1.0);
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: viewDirection = viewDirection.transformedBy(rotation, 0.0);
	  viewDirection = viewDirection.transformedBy(new MutableMatrix44D(rotation), 0.0);
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: ray0 = ray0.transformedBy(rotation, 0.0);
	  ray0 = ray0.transformedBy(new MutableMatrix44D(rotation), 0.0);
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: ray1 = ray1.transformedBy(rotation, 0.0);
	  ray1 = ray1.transformedBy(new MutableMatrix44D(rotation), 0.0);
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: matrix.copyValueOfMultiplication(rotation, matrix);
	  matrix.copyValueOfMultiplication(new MutableMatrix44D(rotation), new MutableMatrix44D(matrix));
	}
  
	// camera rotation
	{
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: Vector3D normal = geodeticSurfaceNormal(centerPoint2);
	  Vector3D normal = geodeticSurfaceNormal(new Vector3D(centerPoint2));
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: Vector3D v0 = _initialPoint0.asVector3D().sub(centerPoint2).projectionInPlane(normal);
	  Vector3D v0 = _initialPoint0.asVector3D().sub(new Vector3D(centerPoint2)).projectionInPlane(new Vector3D(normal));
	  Vector3D p0 = closestIntersection(positionCamera.asVector3D(), ray0.asVector3D());
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: Vector3D v1 = p0.sub(centerPoint2).projectionInPlane(normal);
	  Vector3D v1 = p0.sub(new Vector3D(centerPoint2)).projectionInPlane(new Vector3D(normal));
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: double angle = v0.angleBetween(v1)._degrees;
	  double angle = v0.angleBetween(new Vector3D(v1))._degrees;
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: double sign = v1.cross(v0).dot(normal);
	  double sign = v1.cross(new Vector3D(v0)).dot(new Vector3D(normal));
	  if (sign<0)
		  angle = -angle;
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: MutableMatrix44D rotation = MutableMatrix44D::createGeneralRotationMatrix(Angle::fromDegrees(angle), normal, centerPoint2);
	  MutableMatrix44D rotation = MutableMatrix44D.createGeneralRotationMatrix(Angle.fromDegrees(angle), new Vector3D(normal), new Vector3D(centerPoint2));
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: matrix.copyValueOfMultiplication(rotation, matrix);
	  matrix.copyValueOfMultiplication(new MutableMatrix44D(rotation), new MutableMatrix44D(matrix));
	}
  
	return matrix;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Effect* createDoubleTapEffect(const Vector3D& origin, const Vector3D& centerRay, const Vector3D& tapRay) const
  public final Effect createDoubleTapEffect(Vector3D origin, Vector3D centerRay, Vector3D tapRay)
  {
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: const Vector3D initialPoint = closestIntersection(origin, tapRay);
	final Vector3D initialPoint = closestIntersection(new Vector3D(origin), new Vector3D(tapRay));
	if (initialPoint.isNan())
		return null;
  
	// compute central point of view
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: const Vector3D centerPoint = closestIntersection(origin, centerRay);
	final Vector3D centerPoint = closestIntersection(new Vector3D(origin), new Vector3D(centerRay));
  
	// compute drag parameters
	final IMathUtils mu = IMathUtils.instance();
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: const Vector3D axis = initialPoint.cross(centerPoint);
	final Vector3D axis = initialPoint.cross(new Vector3D(centerPoint));
	final Angle angle = Angle.fromRadians(- mu.asin(axis.length()/initialPoint.length()/centerPoint.length()));
  
	// compute zoom factor
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: const double height = toGeodetic3D(origin)._height;
	final double height = toGeodetic3D(new Vector3D(origin))._height;
	final double distance = height * 0.6;
  
	// create effect
	return new DoubleTapRotationEffect(TimeInterval.fromSeconds(0.75), axis, angle, distance);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: double distanceToHorizon(const Vector3D& position) const
  public final double distanceToHorizon(Vector3D position)
  {
	double R = _sphere._radius;
	double D = position.length();
	return Math.sqrt(D *D - R *R);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: MutableMatrix44D drag(const Geodetic3D& origin, const Geodetic3D& destination) const
  public final MutableMatrix44D drag(Geodetic3D origin, Geodetic3D destination)
  {
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: const Vector3D P0 = toCartesian(origin);
	final Vector3D P0 = toCartesian(new Geodetic3D(origin));
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: const Vector3D P1 = toCartesian(destination);
	final Vector3D P1 = toCartesian(new Geodetic3D(destination));
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: const Vector3D axis = P0.cross(P1);
	final Vector3D axis = P0.cross(new Vector3D(P1));
	if (axis.length()<1e-3)
		return MutableMatrix44D.invalid();
  
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: const Angle angle = P0.angleBetween(P1);
	final Angle angle = P0.angleBetween(new Vector3D(P1));
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: const MutableMatrix44D rotation = MutableMatrix44D::createRotationMatrix(angle, axis);
	final MutableMatrix44D rotation = MutableMatrix44D.createRotationMatrix(new Angle(angle), new Vector3D(axis));
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: const Vector3D rotatedP0 = P0.transformedBy(rotation, 1);
	final Vector3D rotatedP0 = P0.transformedBy(new MutableMatrix44D(rotation), 1);
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: const MutableMatrix44D traslation = MutableMatrix44D::createTranslationMatrix(P1.sub(rotatedP0));
	final MutableMatrix44D traslation = MutableMatrix44D.createTranslationMatrix(P1.sub(new Vector3D(rotatedP0)));
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: return traslation.multiply(rotation);
	return traslation.multiply(new MutableMatrix44D(rotation));
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector3D getNorth() const
  public final Vector3D getNorth()
  {
	return Vector3D.upZ();
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void applyCameraConstrainers(const Camera* previousCamera, Camera* nextCamera) const
  public final void applyCameraConstrainers(Camera previousCamera, Camera nextCamera)
  {
  
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Geodetic3D getDefaultCameraPosition(const Sector& rendereSector) const
  public final Geodetic3D getDefaultCameraPosition(Sector rendereSector)
  {
	final Vector3D asw = toCartesian(rendereSector.getSW());
	final Vector3D ane = toCartesian(rendereSector.getNE());
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: const double height = asw.sub(ane).length() * 1.9;
	final double height = asw.sub(new Vector3D(ane)).length() * 1.9;

	return new Geodetic3D(rendereSector._center, height);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const String getType() const
  public final String getType()
  {
	return "Spherical";
  }

}
