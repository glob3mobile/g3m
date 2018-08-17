package org.glob3.mobile.generated;import java.util.*;

//
//  Sector.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 22/06/12.
//

//
//  Sector.hpp
//  G3MiOSSDK
//
//  Created by Agustin Trujillo Pino on 12/06/12.
//


//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class G3MRenderContext;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Planet;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class ICanvas;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class GEORasterProjection;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class GEORasterSymbol;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Color;


public class Sector
{



	// this lazy value represent the half diagonal of the sector, measured in radians
	// it's stored in double instead of Angle class to optimize performance in android
	// this value is only used in the method Sector::isBackOriented
	private double _deltaRadiusInRadians;

	private Vector3D _normalizedCartesianCenter;


	//Special instances
	public static final Sector FULL_SPHERE = Sector.fromDegrees(-90, -180, 90, 180);
	public static final Sector NAN_SECTOR = Sector.fromDegrees(NAND, NAND, NAND, NAND);


	public final Geodetic2D _lower = new Geodetic2D();
	public final Geodetic2D _upper = new Geodetic2D();

	public final Geodetic2D _center = new Geodetic2D();

	public final Angle _deltaLatitude = new Angle();
	public final Angle _deltaLongitude = new Angle();


	public void dispose()
	{
	  if (_normalizedCartesianCenter != null)
		  _normalizedCartesianCenter.dispose();
	}


	//C++ TO JAVA CONVERTER TODO TASK: The #define macro NAND was defined in alternate ways and cannot be replaced in-line:
    
	public Sector(Geodetic2D lower, Geodetic2D upper)
	{
		_lower = new Geodetic2D(lower);
		_upper = new Geodetic2D(upper);
		_deltaLatitude = new Angle(upper._latitude.sub(lower._latitude));
		_deltaLongitude = new Angle(upper._longitude.sub(lower._longitude));
		_center = new Geodetic2D(Angle.midAngle(lower._latitude, upper._latitude), Angle.midAngle(lower._longitude, upper._longitude));
		_deltaRadiusInRadians = -1.0;
		_normalizedCartesianCenter = null;
	//    if (_deltaLatitude._degrees == 0) {
	//        printf("NO AREA");
	//    }
	}

	public Sector(Sector sector)
	{
		_lower = new Geodetic2D(sector._lower);
		_upper = new Geodetic2D(sector._upper);
		_deltaLatitude = new Angle(sector._deltaLatitude);
		_deltaLongitude = new Angle(sector._deltaLongitude);
		_center = new Geodetic2D(sector._center);
		_deltaRadiusInRadians = sector._deltaRadiusInRadians;
		if (sector._normalizedCartesianCenter == null)
		{
			_normalizedCartesianCenter = null;
		}
		else
		{
			final Vector3D normalizedCartesianCenter = sector._normalizedCartesianCenter;
			_normalizedCartesianCenter = new Vector3D(normalizedCartesianCenter);
		}
    
	//    if (_deltaLatitude._degrees == 0) {
	//        printf("NO AREA");
	//    }
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean isNan() const
	public final boolean isNan()
	{
		return (_lower._latitude._degrees != _lower._latitude._degrees);
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean hasNoArea() const
	public final boolean hasNoArea()
	{
		return _deltaLatitude._radians == 0 || _deltaLongitude._radians == 0;
	}

	public static Sector fromDegrees(double minLat, double minLon, double maxLat, double maxLon)
	{
		final Geodetic2D lower = new Geodetic2D(Angle.fromDegrees(minLat), Angle.fromDegrees(minLon));
		final Geodetic2D upper = new Geodetic2D(Angle.fromDegrees(maxLat), Angle.fromDegrees(maxLon));

//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: return Sector(lower, upper);
		return new Sector(new Geodetic2D(lower), new Geodetic2D(upper));
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const Vector2D div(const Sector& that) const
	public final Vector2D div(Sector that)
	{
	  final double scaleX = _deltaLongitude.div(that._deltaLongitude);
	  final double scaleY = _deltaLatitude.div(that._deltaLatitude);
	  return new Vector2D(scaleX, scaleY);
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean fullContains(const Sector& that) const
	public final boolean fullContains(Sector that)
	{
	  //return contains(that._upper) && contains(that._lower);
	  return (contains(that._upper._latitude, that._upper._longitude) && contains(that._lower._latitude, that._lower._longitude));
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Sector intersection(const Sector& that) const
	public final Sector intersection(Sector that)
	{
	  final Angle lowLat = Angle.max(_lower._latitude, that._lower._latitude);
	  final Angle lowLon = Angle.max(_lower._longitude, that._lower._longitude);
    
	  final Angle upLat = Angle.min(_upper._latitude, that._upper._latitude);
	  final Angle upLon = Angle.min(_upper._longitude, that._upper._longitude);
    
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: if (lowLat.lowerThan(upLat) && lowLon.lowerThan(upLon))
	  if (lowLat.lowerThan(new Angle(upLat)) && lowLon.lowerThan(new Angle(upLon)))
	  {
		final Geodetic2D low = new Geodetic2D(lowLat, lowLon);
		final Geodetic2D up = new Geodetic2D(upLat, upLon);
    
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: return Sector(low, up);
		return new Sector(new Geodetic2D(low), new Geodetic2D(up));
	  }
    
	  return Sector.fromDegrees(0, 0, 0, 0); //invalid
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Sector mergedWith(const Sector& that) const
	public final Sector mergedWith(Sector that)
	{
	  final Angle lowLat = Angle.min(_lower._latitude, that._lower._latitude);
	  final Angle lowLon = Angle.min(_lower._longitude, that._lower._longitude);
	  final Geodetic2D low = new Geodetic2D(lowLat, lowLon);
    
	  final Angle upLat = Angle.max(_upper._latitude, that._upper._latitude);
	  final Angle upLon = Angle.max(_upper._longitude, that._upper._longitude);
	  final Geodetic2D up = new Geodetic2D(upLat, upLon);
    
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: return Sector(low, up);
	  return new Sector(new Geodetic2D(low), new Geodetic2D(up));
	}

	public static Sector fullSphere()
	{
	  return FULL_SPHERE;
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean contains(const Angle& latitude, const Angle& longitude) const
	public final boolean contains(Angle latitude, Angle longitude)
	{
	  return (latitude.isBetween(_lower._latitude, _upper._latitude) && longitude.isBetween(_lower._longitude, _upper._longitude));
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean contains(const Geodetic2D& position) const
	public final boolean contains(Geodetic2D position)
	{
		return contains(position._latitude, position._longitude);
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean touchesWith(const Sector &that) const
	public final boolean touchesWith(Sector that)
	{
	  // from Real-Time Collision Detection - Christer Ericson
	  //   page 79
    
	  // Exit with no intersection if separated along an axis
	  //  if (_upper._latitude.lowerThan(that._lower._latitude) ||
	  //      _lower._latitude.greaterThan(that._upper._latitude)) {
	  //    return false;
	  //  }
	  //  if (_upper._longitude.lowerThan(that._lower._longitude) ||
	  //      _lower._longitude.greaterThan(that._upper._longitude)) {
	  //    return false;
	  //  }
	  if ((_upper._latitude._radians < that._lower._latitude._radians) || (_lower._latitude._radians > that._upper._latitude._radians))
	  {
		return false;
	  }
	  if ((_upper._longitude._radians < that._lower._longitude._radians) || (_lower._longitude._radians > that._upper._longitude._radians))
	  {
		return false;
	  }
    
	  // Overlapping on all axes means Sectors are intersecting
	  return true;
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const Geodetic2D getSW() const
	public final Geodetic2D getSW()
	{
		return _lower;
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const Geodetic2D getNE() const
	public final Geodetic2D getNE()
	{
		return _upper;
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const Geodetic2D getNW() const
	public final Geodetic2D getNW()
	{
		return new Geodetic2D(_upper._latitude, _lower._longitude);
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const Geodetic2D getSE() const
	public final Geodetic2D getSE()
	{
		return new Geodetic2D(_lower._latitude, _upper._longitude);
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const Geodetic2D getCenter() const
	public final Geodetic2D getCenter()
	{
		return _center;
	}

	// (u,v) are similar to texture coordinates inside the Sector
	// (u,v)=(0,0) in NW point, and (1,1) in SE point

	// (u,v) are similar to texture coordinates inside the Sector
	// (u,v)=(0,0) in NW point, and (1,1) in SE point
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const Geodetic2D getInnerPoint(double u, double v) const
	public final Geodetic2D getInnerPoint(double u, double v)
	{
	  return new Geodetic2D(Angle.linearInterpolation(_lower._latitude, _upper._latitude, 1.0 - v), Angle.linearInterpolation(_lower._longitude, _upper._longitude, u));
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const Angle getInnerPointLongitude(double u) const
	public final Angle getInnerPointLongitude(double u)
	{
	  return Angle.linearInterpolation(_lower._longitude, _upper._longitude, u);
	}
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const Angle getInnerPointLatitude(double v) const
	public final Angle getInnerPointLatitude(double v)
	{
	  return Angle.linearInterpolation(_lower._latitude, _upper._latitude, 1.0 - v);
	}


//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const Vector2D getUVCoordinates(const Geodetic2D& point) const
	public final Vector2D getUVCoordinates(Geodetic2D point)
	{
		return getUVCoordinates(point._latitude, point._longitude);
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector2D getUVCoordinates(const Angle& latitude, const Angle& longitude) const
	public final Vector2D getUVCoordinates(Angle latitude, Angle longitude)
	{
		return new Vector2D((longitude._radians - _lower._longitude._radians) / _deltaLongitude._radians, (_upper._latitude._radians - latitude._radians) / _deltaLatitude._radians);
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector2F getUVCoordinatesF(const Angle& latitude, const Angle& longitude) const
	public final Vector2F getUVCoordinatesF(Angle latitude, Angle longitude)
	{
		return new Vector2F((float)((longitude._radians - _lower._longitude._radians) / _deltaLongitude._radians), (float)((_upper._latitude._radians - latitude._radians) / _deltaLatitude._radians));
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: double getUCoordinate(const Angle& longitude) const
	public final double getUCoordinate(Angle longitude)
	{
		return (longitude._radians - _lower._longitude._radians) / _deltaLongitude._radians;
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: double getVCoordinate(const Angle& latitude) const
	public final double getVCoordinate(Angle latitude)
	{
		return (_upper._latitude._radians - latitude._radians) / _deltaLatitude._radians;
	}


//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean isBackOriented(const G3MRenderContext* rc, double minHeight, const Planet* planet, const Vector3D& cameraNormalizedPosition, double cameraAngle2HorizonInRadians) const
	public final boolean isBackOriented(G3MRenderContext rc, double minHeight, Planet planet, Vector3D cameraNormalizedPosition, double cameraAngle2HorizonInRadians)
	{
	  //  const Camera* camera = rc->getCurrentCamera();
	  //  const Planet* planet = rc->getPlanet();
	  //
	  //  const double dot = camera->getNormalizedPosition().dot(getNormalizedCartesianCenter(planet));
	  //  const double angleInRadians = IMathUtils::instance()->acos(dot);
	  //
	  //  return ( (angleInRadians - getDeltaRadiusInRadians()) > camera->getAngle2HorizonInRadians() );
    
	  if (planet.isFlat())
		  return false;
    
	  final double dot = cameraNormalizedPosition.dot(getNormalizedCartesianCenter(planet));
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
	  final double angleInRadians = IMathUtils.instance().acos(dot);
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	  final double angleInRadians = java.lang.Math.acos(dot);
//#endif
    
	  return ((angleInRadians - getDeltaRadiusInRadians()) > cameraAngle2HorizonInRadians);
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const Geodetic2D clamp(const Geodetic2D& position) const
	public final Geodetic2D clamp(Geodetic2D position)
	{
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: if (contains(position))
	  if (contains(new Geodetic2D(position)))
	  {
		return position;
	  }
    
	  double latitudeInDegrees = position._latitude._degrees;
	  double longitudeInDegrees = position._longitude._degrees;
    
	  final double upperLatitudeInDegrees = _upper._latitude._degrees;
	  if (latitudeInDegrees > upperLatitudeInDegrees)
	  {
		latitudeInDegrees = upperLatitudeInDegrees;
	  }
    
	  final double upperLongitudeInDegrees = _upper._longitude._degrees;
	  if (longitudeInDegrees > upperLongitudeInDegrees)
	  {
		longitudeInDegrees = upperLongitudeInDegrees;
	  }
    
	  final double lowerLatitudeInDegrees = _lower._latitude._degrees;
	  if (latitudeInDegrees < lowerLatitudeInDegrees)
	  {
		latitudeInDegrees = lowerLatitudeInDegrees;
	  }
    
	  final double lowerLongitudeInDegrees = _lower._longitude._degrees;
	  if (longitudeInDegrees < lowerLongitudeInDegrees)
	  {
		longitudeInDegrees = lowerLongitudeInDegrees;
	  }
    
	  return Geodetic2D.fromDegrees(latitudeInDegrees, longitudeInDegrees);
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const Geodetic2D clamp(const Angle& latitude, const Angle& longitude) const
	public final Geodetic2D clamp(Angle latitude, Angle longitude)
	{
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: if (contains(latitude, longitude))
	  if (contains(new Angle(latitude), new Angle(longitude)))
	  {
		return new Geodetic2D(latitude, longitude);
	  }
    
	  double latitudeInDegrees = latitude._degrees;
	  double longitudeInDegrees = longitude._degrees;
    
	  final double upperLatitudeInDegrees = _upper._latitude._degrees;
	  if (latitudeInDegrees > upperLatitudeInDegrees)
	  {
		latitudeInDegrees = upperLatitudeInDegrees;
	  }
    
	  final double upperLongitudeInDegrees = _upper._longitude._degrees;
	  if (longitudeInDegrees > upperLongitudeInDegrees)
	  {
		longitudeInDegrees = upperLongitudeInDegrees;
	  }
    
	  final double lowerLatitudeInDegrees = _lower._latitude._degrees;
	  if (latitudeInDegrees < lowerLatitudeInDegrees)
	  {
		latitudeInDegrees = lowerLatitudeInDegrees;
	  }
    
	  final double lowerLongitudeInDegrees = _lower._longitude._degrees;
	  if (longitudeInDegrees < lowerLongitudeInDegrees)
	  {
		longitudeInDegrees = lowerLongitudeInDegrees;
	  }
    
	  return Geodetic2D.fromDegrees(latitudeInDegrees, longitudeInDegrees);
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const Angle clampLatitude(const Angle& latitude) const
	public final Angle clampLatitude(Angle latitude)
	{
	  return latitude.clampedTo(_lower._latitude, _upper._latitude);
	}
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const Angle clampLongitude(const Angle& longitude) const
	public final Angle clampLongitude(Angle longitude)
	{
	  return longitude.clampedTo(_lower._longitude, _upper._longitude);
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const String id() const
	public final String id()
	{
	  IStringBuilder isb = IStringBuilder.newStringBuilder();
	  isb.addString("Sector|");
	  isb.addDouble(_lower._latitude._degrees);
	  isb.addString("|");
	  isb.addDouble(_lower._longitude._degrees);
	  isb.addString("|");
	  isb.addDouble(_upper._latitude._degrees);
	  isb.addString("|");
	  isb.addDouble(_upper._longitude._degrees);
	  isb.addString("|");
	  final String s = isb.getString();
	  if (isb != null)
		  isb.dispose();
	  return s;
	}
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const String description() const
	public final String description()
	{
	  IStringBuilder isb = IStringBuilder.newStringBuilder();
	  isb.addString("(Sector ");
	  isb.addString(_lower.description());
	  isb.addString(" - ");
	  isb.addString(_upper.description());
	  isb.addString(")");
	  final String s = isb.getString();
	  if (isb != null)
		  isb.dispose();
	  return s;
	}
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	public final Override public String toString()
	{
		return description();
	}
//#endif

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Sector* shrinkedByPercentP(float percent) const
	public final Sector shrinkedByPercentP(float percent)
	{
		final Angle deltaLatitude = _deltaLatitude.times(percent).div(2);
		final Angle deltaLongitude = _deltaLongitude.times(percent).div(2);

		final Geodetic2D delta = new Geodetic2D(deltaLatitude, deltaLongitude);

//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: return new Sector(_lower.add(delta), _upper.sub(delta));
		return new Sector(_lower.add(new Geodetic2D(delta)), _upper.sub(new Geodetic2D(delta)));
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Sector shrinkedByPercent(float percent) const
	public final Sector shrinkedByPercent(float percent)
	{
		final Angle deltaLatitude = _deltaLatitude.times(percent).div(2);
		final Angle deltaLongitude = _deltaLongitude.times(percent).div(2);

		final Geodetic2D delta = new Geodetic2D(deltaLatitude, deltaLongitude);

//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: return Sector(_lower.add(delta), _upper.sub(delta));
		return new Sector(_lower.add(new Geodetic2D(delta)), _upper.sub(new Geodetic2D(delta)));
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean isEquals(const Sector& that) const
	public final boolean isEquals(Sector that)
	{
		return _lower.isEquals(that._lower) && _upper.isEquals(that._upper);
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean touchesNorthPole() const
	public final boolean touchesNorthPole()
	{
		return (_upper._latitude._degrees >= 89.9);
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean touchesSouthPole() const
	public final boolean touchesSouthPole()
	{
		return (_lower._latitude._degrees <= -89.9);
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Angle getNorth() const
	public final Angle getNorth()
	{
		return _upper._latitude;
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Angle getSouth() const
	public final Angle getSouth()
	{
		return _lower._latitude;
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Angle getEast() const
	public final Angle getEast()
	{
		return _upper._longitude;
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Angle getWest() const
	public final Angle getWest()
	{
		return _lower._longitude;
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void rasterize(ICanvas* canvas, const GEORasterProjection* projection) const
	public final void rasterize(ICanvas canvas, GEORasterProjection projection)
	{
    
	  final Vector2F l = projection.project(_lower);
	  final Vector2F u = projection.project(_upper);
    
	  final float left = l._x;
	  final float top = l._y;
	  final float width = u._x - left;
	  final float height = u._y - top;
    
	  canvas.strokeRectangle(left, top, width, height);
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean touchesPoles() const
	public final boolean touchesPoles()
	{
		return ((_upper._latitude._degrees >= 89.9) || (_lower._latitude._degrees <= -89.9));
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: double getDeltaRadiusInRadians() const
	public final double getDeltaRadiusInRadians()
	{
		if (_deltaRadiusInRadians < 0)
		{
			_deltaRadiusInRadians = IMathUtils.instance().sqrt((_deltaLatitude._radians * _deltaLatitude._radians) + (_deltaLongitude._radians * _deltaLongitude._radians)) * 0.5;
		}
		return _deltaRadiusInRadians;
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const Vector3D getNormalizedCartesianCenter(const Planet* planet) const
	public final Vector3D getNormalizedCartesianCenter(Planet planet)
	{
	  if (_normalizedCartesianCenter == null)
	  {
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: _normalizedCartesianCenter = new Vector3D(planet->toCartesian(_center).normalized());
		_normalizedCartesianCenter = new Vector3D(planet.toCartesian(new Geodetic2D(_center)).normalized());
	  }
	  return _normalizedCartesianCenter;
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const double getAngularAreaInSquaredDegrees() const
	public final double getAngularAreaInSquaredDegrees()
	{
		return _deltaLatitude._degrees * _deltaLongitude._degrees;
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const GEORasterSymbol* createGEOSymbol(const Color& c) const
	public final GEORasterSymbol createGEOSymbol(Color c)
	{
    
	  java.util.ArrayList<Geodetic2D> coordinates = new java.util.ArrayList<Geodetic2D*>();
    
	  coordinates.add(new Geodetic2D(getSW()));
	  coordinates.add(new Geodetic2D(getNW()));
	  coordinates.add(new Geodetic2D(getNE()));
	  coordinates.add(new Geodetic2D(getSE()));
	  coordinates.add(new Geodetic2D(getSW()));
    
	  //    printf("RESTERIZING: %s\n", _sector->description().c_str());
    
	  float[] dashLengths = {};
	  int dashCount = 0;
    
	  GEO2DLineRasterStyle ls = new GEO2DLineRasterStyle(c, (float)1.0, StrokeCap.CAP_ROUND, StrokeJoin.JOIN_ROUND, 1, dashLengths, dashCount, 0); //const int dashPhase) : - const int dashCount, - float dashLengths[], - const float miterLimit, - const StrokeJoin join, -  const StrokeCap cap, - const float width, - const Color& color,
    
    
	  final GEO2DCoordinatesData coordinatesData = new GEO2DCoordinatesData(coordinates);
	  final GEOLineRasterSymbol result = new GEOLineRasterSymbol(coordinatesData, ls);
	  coordinatesData._release();
	  return result;
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Geodetic2D getClosesInnerPoint(const Geodetic2D& g) const
	public final Geodetic2D getClosesInnerPoint(Geodetic2D g)
	{
	  double lat = g._latitude._degrees;
	  double lon = g._longitude._degrees;
    
	  if (lat > _upper._latitude._degrees)
	  {
		lat = _upper._latitude._degrees;
	  }
	  else
	  {
		if (lat < _lower._latitude._degrees)
		{
		  lat = _lower._latitude._degrees;
		}
	  }
    
	  if (lon > _upper._longitude._degrees)
	  {
		lon = _upper._longitude._degrees;
	  }
	  else
	  {
		if (lon < _lower._longitude._degrees)
		{
		  lon = _lower._longitude._degrees;
		}
	  }
    
	  return Geodetic2D.fromDegrees(lat, lon);
	}


//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	public final Override public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = (prime * result) + ((_lower == null) ? 0 : _lower.hashCode());
		result = (prime * result) + ((_upper == null) ? 0 : _upper.hashCode());
		return result;
	}


	public Override public boolean equals(final Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		if (obj == null)
		{
			return false;
		}
		if (getClass() != obj.getClass())
		{
			return false;
		}
		final Sector other = (Sector) obj;
		if (_lower == null)
		{
			if (other._lower != null)
			{
				return false;
			}
		}
		else if (!_lower.equals(other._lower))
		{
			return false;
		}
		if (_upper == null)
		{
			if (other._upper != null)
			{
				return false;
			}
		}
		else if (!_upper.equals(other._upper))
		{
			return false;
		}
		return true;
	}
//#endif

}
