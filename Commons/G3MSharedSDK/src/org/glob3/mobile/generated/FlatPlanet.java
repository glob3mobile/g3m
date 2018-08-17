package org.glob3.mobile.generated;//
//  FlatPlanet.cpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo on 10/07/13.
//
//

//
//  FlatPlanet.hpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo on 10/07/13.
//
//




public class FlatPlanet extends Planet
{
  private final Vector2D _size = new Vector2D();
  private final Vector3D _radii = new Vector3D();

  private MutableVector3D _origin = new MutableVector3D();
  private MutableVector3D _initialPoint = new MutableVector3D();
  private MutableVector3D _lastFinalPoint = new MutableVector3D();
  private boolean _validSingleDrag;
  private MutableVector3D _lastDirection = new MutableVector3D();

  private MutableVector3D _centerRay = new MutableVector3D();
  private MutableVector3D _initialPoint0 = new MutableVector3D();
  private MutableVector3D _initialPoint1 = new MutableVector3D();
  private double _distanceBetweenInitialPoints;
  private MutableVector3D _centerPoint = new MutableVector3D();




  public static Planet createEarth()
  {
	return new FlatPlanet(new Vector2D(4 *6378137.0, 2 *6378137.0));
  }

  public FlatPlanet(Vector2D size)
  {
	  _size = new Vector2D(size);
	  _radii = new Vector3D(size._x, size._y, 0);
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
	return new Vector3D(0, 0, 1);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector3D geodeticSurfaceNormal(const Vector3D& position) const
  public final Vector3D geodeticSurfaceNormal(Vector3D position)
  {
	return new Vector3D(0, 0, 1);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector3D geodeticSurfaceNormal(const MutableVector3D& position) const
  public final Vector3D geodeticSurfaceNormal(MutableVector3D position)
  {
	return new Vector3D(0, 0, 1);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void geodeticSurfaceNormal(const Angle& latitude, const Angle& longitude, MutableVector3D& result) const
  public final void geodeticSurfaceNormal(Angle latitude, Angle longitude, tangible.RefObject<MutableVector3D> result)
  {
	result.argvalue.set(0, 0, 1);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector3D geodeticSurfaceNormal(const Angle& latitude, const Angle& longitude) const
  public final Vector3D geodeticSurfaceNormal(Angle latitude, Angle longitude)
  {
	return new Vector3D(0, 0, 1);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector3D geodeticSurfaceNormal(const Geodetic3D& geodetic) const
  public final Vector3D geodeticSurfaceNormal(Geodetic3D geodetic)
  {
	return new Vector3D(0, 0, 1);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector3D geodeticSurfaceNormal(const Geodetic2D& geodetic) const
  public final Vector3D geodeticSurfaceNormal(Geodetic2D geodetic)
  {
	return new Vector3D(0, 0, 1);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: java.util.ArrayList<double> intersectionsDistances(double originX, double originY, double originZ, double directionX, double directionY, double directionZ) const
  public final java.util.ArrayList<Double> intersectionsDistances(double originX, double originY, double originZ, double directionX, double directionY, double directionZ)
  {
	java.util.ArrayList<Double> intersections = new java.util.ArrayList<Double>();
  
	// compute intersection with plane
	if (directionZ == 0)
		return intersections;
	final double t = -originZ / directionZ;
	final double x = originX + t * directionX;
	final double halfWidth = 0.5 * _size._x;
	if (x < -halfWidth || x > halfWidth)
		return intersections;
	final double y = originY + t * directionY;
	final double halfHeight = 0.5 * _size._y;
	if (y < -halfHeight || y > halfHeight)
		return intersections;
	intersections.add(t);
	return intersections;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector3D toCartesian(const Angle& latitude, const Angle& longitude, const double height) const
  public final Vector3D toCartesian(Angle latitude, Angle longitude, double height)
  {
	final double x = longitude._degrees * _size._x / 360.0;
	final double y = latitude._degrees * _size._y / 180.0;
	return new Vector3D(x, y, height);
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
	final double x = longitude._degrees * _size._x / 360.0;
	final double y = latitude._degrees * _size._y / 180.0;
	result.argvalue.set(x, y, height);
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
	toCartesian(geodetic._latitude, geodetic._longitude, 0.0, result);
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
	final double longitude = position._x * 360.0 / _size._x;
	final double latitude = position._y * 180.0 / _size._y;
	return (new Geodetic2D(Angle.fromDegrees(latitude), Angle.fromDegrees(longitude)));
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Geodetic3D toGeodetic3D(const Vector3D& position) const
  public final Geodetic3D toGeodetic3D(Vector3D position)
  {
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: return Geodetic3D(toGeodetic2D(position), position._z);
	return new Geodetic3D(toGeodetic2D(new Vector3D(position)), position._z);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector3D scaleToGeodeticSurface(const Vector3D& position) const
  public final Vector3D scaleToGeodeticSurface(Vector3D position)
  {
	return new Vector3D(position._x, position._y, 0);
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
//ORIGINAL LINE: Geodetic2D getMidPoint (const Geodetic2D& P0, const Geodetic2D& P1) const
  public final Geodetic2D getMidPoint (Geodetic2D P0, Geodetic2D P1)
  {
	return new Geodetic2D(P0._latitude.add(P1._latitude).times(0.5), P0._longitude.add(P1._longitude).times(0.5));
  }



  // compute distance from two points
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: double computePreciseLatLonDistance(const Geodetic2D& g1, const Geodetic2D& g2) const
  public final double computePreciseLatLonDistance(Geodetic2D g1, Geodetic2D g2)
  {
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: return toCartesian(g1).sub(toCartesian(g2)).length();
	return toCartesian(new Geodetic2D(g1)).sub(toCartesian(new Geodetic2D(g2))).length();
  }


  // compute distance from two points
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: double computeFastLatLonDistance(const Geodetic2D& g1, const Geodetic2D& g2) const
  public final double computeFastLatLonDistance(Geodetic2D g1, Geodetic2D g2)
  {
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: return computePreciseLatLonDistance(g1, g2);
	return computePreciseLatLonDistance(new Geodetic2D(g1), new Geodetic2D(g2));
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: MutableMatrix44D createGeodeticTransformMatrix(const Geodetic3D& position) const
  public final MutableMatrix44D createGeodeticTransformMatrix(Geodetic3D position)
  {
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: return MutableMatrix44D::createTranslationMatrix(toCartesian(position));
	return MutableMatrix44D.createTranslationMatrix(toCartesian(new Geodetic3D(position)));
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean isFlat() const
  public final boolean isFlat()
  {
	  return true;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void beginSingleDrag(const Vector3D& origin, const Vector3D& initialRay) const
  public final void beginSingleDrag(Vector3D origin, Vector3D initialRay)
  {
	_origin = origin.asMutableVector3D();
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: _initialPoint = Plane::intersectionXYPlaneWithRay(origin, initialRay).asMutableVector3D();
	_initialPoint = Plane.intersectionXYPlaneWithRay(new Vector3D(origin), new Vector3D(initialRay)).asMutableVector3D();
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy assignment (rather than a reference assignment) - this should be verified and a 'copyFrom' method should be created if it does not yet exist:
//ORIGINAL LINE: _lastFinalPoint = _initialPoint;
	_lastFinalPoint.copyFrom(_initialPoint);
	_validSingleDrag = false;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: MutableMatrix44D singleDrag(const Vector3D& finalRay) const
  public final MutableMatrix44D singleDrag(Vector3D finalRay)
  {
	// test if initialPoint is valid
	if (_initialPoint.isNan())
		return MutableMatrix44D.invalid();
  
	// compute final point
	final Vector3D origin = _origin.asVector3D();
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: MutableVector3D finalPoint = Plane::intersectionXYPlaneWithRay(origin, finalRay).asMutableVector3D();
	MutableVector3D finalPoint = Plane.intersectionXYPlaneWithRay(new Vector3D(origin), new Vector3D(finalRay)).asMutableVector3D();
	if (finalPoint.isNan())
		return MutableMatrix44D.invalid();
  
	// save params for possible inertial animations
	_validSingleDrag = true;
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: _lastDirection = _lastFinalPoint.sub(finalPoint);
	_lastDirection = _lastFinalPoint.sub(new MutableVector3D(finalPoint));
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy assignment (rather than a reference assignment) - this should be verified and a 'copyFrom' method should be created if it does not yet exist:
//ORIGINAL LINE: _lastFinalPoint = finalPoint;
	_lastFinalPoint.copyFrom(finalPoint);
  
	// return rotation matrix
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: return MutableMatrix44D::createTranslationMatrix(_initialPoint.sub(finalPoint).asVector3D());
	return MutableMatrix44D.createTranslationMatrix(_initialPoint.sub(new MutableVector3D(finalPoint)).asVector3D());
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Effect* createEffectFromLastSingleDrag() const
  public final Effect createEffectFromLastSingleDrag()
  {
	if (!_validSingleDrag)
		return null;
	return new SingleTranslationEffect(_lastDirection.asVector3D());
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void beginDoubleDrag(const Vector3D& origin, const Vector3D& centerRay, const Vector3D& initialRay0, const Vector3D& initialRay1) const
  public final void beginDoubleDrag(Vector3D origin, Vector3D centerRay, Vector3D initialRay0, Vector3D initialRay1)
  {
	_origin = origin.asMutableVector3D();
	_centerRay = centerRay.asMutableVector3D();
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: _initialPoint0 = Plane::intersectionXYPlaneWithRay(origin, initialRay0).asMutableVector3D();
	_initialPoint0 = Plane.intersectionXYPlaneWithRay(new Vector3D(origin), new Vector3D(initialRay0)).asMutableVector3D();
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: _initialPoint1 = Plane::intersectionXYPlaneWithRay(origin, initialRay1).asMutableVector3D();
	_initialPoint1 = Plane.intersectionXYPlaneWithRay(new Vector3D(origin), new Vector3D(initialRay1)).asMutableVector3D();
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: _distanceBetweenInitialPoints = _initialPoint0.sub(_initialPoint1).length();
	_distanceBetweenInitialPoints = _initialPoint0.sub(new MutableVector3D(_initialPoint1)).length();
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: _centerPoint = Plane::intersectionXYPlaneWithRay(origin, centerRay).asMutableVector3D();
	_centerPoint = Plane.intersectionXYPlaneWithRay(new Vector3D(origin), new Vector3D(centerRay)).asMutableVector3D();
  
	// middle point in 3D
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: _initialPoint = _initialPoint0.add(_initialPoint1).times(0.5);
	_initialPoint = _initialPoint0.add(new MutableVector3D(_initialPoint1)).times(0.5);
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
  
	// compute distance to translate camera
	double d0 = _distanceBetweenInitialPoints;
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: const Vector3D r1 = finalRay0;
	final Vector3D r1 = new Vector3D(finalRay0);
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: const Vector3D r2 = finalRay1;
	final Vector3D r2 = new Vector3D(finalRay1);
	double k = ((r1._x/r1._z - r2._x/r2._z) * (r1._x/r1._z - r2._x/r2._z) + (r1._y/r1._z - r2._y/r2._z) * (r1._y/r1._z - r2._y/r2._z));
	double zc = _origin.z();
	double uz = _centerRay.z();
	double t2 = (d0 / mu.sqrt(k) - zc) / uz;
  
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
  
	// drag from initialPoint to centerPoint and move the camera forward
	{
	  MutableVector3D delta = _initialPoint.sub((_centerPoint));
	  delta = delta.add(viewDirection.times(t2));
	  MutableMatrix44D translation = MutableMatrix44D.createTranslationMatrix(delta.asVector3D());
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: positionCamera = positionCamera.transformedBy(translation, 1.0);
	  positionCamera = positionCamera.transformedBy(new MutableMatrix44D(translation), 1.0);
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: matrix.copyValueOfMultiplication(translation, matrix);
	  matrix.copyValueOfMultiplication(new MutableMatrix44D(translation), new MutableMatrix44D(matrix));
	}
  
	// compute 3D point of view center
	Vector3D centerPoint2 = Plane.intersectionXYPlaneWithRay(positionCamera.asVector3D(), viewDirection.asVector3D());
  
	// compute middle point in 3D
	Vector3D P0 = Plane.intersectionXYPlaneWithRay(positionCamera.asVector3D(), ray0.asVector3D());
	Vector3D P1 = Plane.intersectionXYPlaneWithRay(positionCamera.asVector3D(), ray1.asVector3D());
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: Vector3D finalPoint = P0.add(P1).times(0.5);
	Vector3D finalPoint = P0.add(new Vector3D(P1)).times(0.5);
  
	// drag globe from centerPoint to finalPoint
	{
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: MutableMatrix44D translation = MutableMatrix44D::createTranslationMatrix(centerPoint2.sub(finalPoint));
	  MutableMatrix44D translation = MutableMatrix44D.createTranslationMatrix(centerPoint2.sub(new Vector3D(finalPoint)));
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: positionCamera = positionCamera.transformedBy(translation, 1.0);
	  positionCamera = positionCamera.transformedBy(new MutableMatrix44D(translation), 1.0);
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: matrix.copyValueOfMultiplication(translation, matrix);
	  matrix.copyValueOfMultiplication(new MutableMatrix44D(translation), new MutableMatrix44D(matrix));
	}
  
	// camera rotation
	{
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: Vector3D normal = geodeticSurfaceNormal(centerPoint2);
	  Vector3D normal = geodeticSurfaceNormal(new Vector3D(centerPoint2));
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: Vector3D v0 = _initialPoint0.asVector3D().sub(centerPoint2).projectionInPlane(normal);
	  Vector3D v0 = _initialPoint0.asVector3D().sub(new Vector3D(centerPoint2)).projectionInPlane(new Vector3D(normal));
	  Vector3D p0 = Plane.intersectionXYPlaneWithRay(positionCamera.asVector3D(), ray0.asVector3D());
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
//ORIGINAL LINE: const Vector3D initialPoint = Plane::intersectionXYPlaneWithRay(origin, tapRay);
	final Vector3D initialPoint = Plane.intersectionXYPlaneWithRay(new Vector3D(origin), new Vector3D(tapRay));
	if (initialPoint.isNan())
		return null;
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: const Vector3D centerPoint = Plane::intersectionXYPlaneWithRay(origin, centerRay);
	final Vector3D centerPoint = Plane.intersectionXYPlaneWithRay(new Vector3D(origin), new Vector3D(centerRay));
  
	// create effect
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: return new DoubleTapTranslationEffect(TimeInterval::fromSeconds(0.75), initialPoint.sub(centerPoint), toGeodetic3D(origin)._height *0.6);
	return new DoubleTapTranslationEffect(TimeInterval.fromSeconds(0.75), initialPoint.sub(new Vector3D(centerPoint)), toGeodetic3D(new Vector3D(origin))._height *0.6);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: double distanceToHorizon(const Vector3D& position) const
  public final double distanceToHorizon(Vector3D position)
  {
	double xCorner = 0.5 * _size._x;
	if (position._x > 0)
		xCorner *= -1;
	double yCorner = 0.5 * _size._y;
	if (position._y > 0)
		yCorner *= -1;
	final Vector3D fartherCorner = new Vector3D(xCorner, yCorner, 0.0);
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: return position.sub(fartherCorner).length();
	return position.sub(new Vector3D(fartherCorner)).length();
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
//ORIGINAL LINE: return MutableMatrix44D::createTranslationMatrix(P1.sub(P0));
	return MutableMatrix44D.createTranslationMatrix(P1.sub(new Vector3D(P0)));
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector3D getNorth() const
  public final Vector3D getNorth()
  {
	return Vector3D.upY();
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
	return "Flat";
  }

}
