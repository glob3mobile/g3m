package org.glob3.mobile.generated; 
//
//  Sector.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 22/06/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

//
//  Sector.h
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 12/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//





public class Sector
{

  private final Geodetic2D _lower ;
  private final Geodetic2D _upper ;

  private final Angle _deltaLatitude ;
  private final Angle _deltaLongitude ;




//  static Sector fromLowerAndUpper(const Geodetic2D& lower,
//                           const Geodetic2D& upper){
//    
//    double upLat = upper.latitude().degrees();
//    while (lower.latitude().degrees() > upLat){
//      upLat += 360.0;
//    }
//    
//    double upLon = upper.longitude().degrees();
//    while (lower.latitude().degrees() > upLon){
//      upLon += 360.0;
//    }
//    
//    Geodetic2D upper2 = Geodetic2D::fromDegrees(upLat, upLon);
//    
//    return Sector(lower, upper2);
//  }

  public Sector(Geodetic2D lower, Geodetic2D upper)
  {
	  _lower = new Geodetic2D(lower);
	  _upper = new Geodetic2D(upper);
	  _deltaLatitude = new Angle(upper.latitude().sub(lower.latitude()));
	  _deltaLongitude = new Angle(upper.longitude().sub(lower.longitude()));
  }


  public Sector(Sector s)
  {
	  _lower = new Geodetic2D(s._lower);
	  _upper = new Geodetic2D(s._upper);
	  _deltaLatitude = new Angle(s._deltaLatitude);
	  _deltaLongitude = new Angle(s._deltaLongitude);
  }

  public static Sector fromDegrees(double minLat, double minLon, double maxLat, double maxLon)
  {
	Geodetic2D lower = new Geodetic2D(Angle.fromDegrees(minLat), Angle.fromDegrees(minLon));
	Geodetic2D upper = new Geodetic2D(Angle.fromDegrees(maxLat), Angle.fromDegrees(maxLon));
	Sector s = new Sector(lower, upper);
	return s;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector2D getScaleFactor(const Sector& s) const
  public final Vector2D getScaleFactor(Sector s)
  {
	double u = _deltaLatitude.div(s._deltaLatitude);
	double v = _deltaLongitude.div(s._deltaLongitude);
	Vector2D scale = new Vector2D(u,v);
	return scale;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector2D getTranslationFactor(const Sector& s) const
  public final Vector2D getTranslationFactor(Sector s)
  {
	double diff = _deltaLongitude.div(s._deltaLongitude);
	Vector2D uv = s.getUVCoordinates(_lower);

	Vector2D trans = new Vector2D(uv.x(), uv.y()- diff);
	return trans;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean fullContains(const Sector &s) const
  public final boolean fullContains(Sector s)
  {
	return contains(s.upper()) && contains(s.lower());
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Sector intersection(const Sector& s) const
  public final Sector intersection(Sector s)
  {
	final Angle lowLat = Angle.getMax(lower().latitude(), s.lower().latitude());
	final Angle lowLon = Angle.getMax(lower().longitude(), s.lower().longitude());
	final Geodetic2D low = new Geodetic2D(lowLat, lowLon);
  
	final Angle upLat = Angle.getMin(lower().latitude(), s.lower().latitude());
	final Angle upLon = Angle.getMin(lower().longitude(), s.lower().longitude());
	final Geodetic2D up = new Geodetic2D(upLat, upLon);
  
	return new Sector(low, up);
  }

  public static Sector fullSphere()
  {
	return new Sector(new Geodetic2D(Angle.fromDegrees(-90), Angle.fromDegrees(-180)), new Geodetic2D(Angle.fromDegrees(90), Angle.fromDegrees(180)));
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const Geodetic2D lower() const
  public final Geodetic2D lower()
  {
	return _lower;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const Geodetic2D upper() const
  public final Geodetic2D upper()
  {
	return _upper;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean contains(const Geodetic2D &position) const
  public final boolean contains(Geodetic2D position)
  {
	return position.isBetween(_lower, _upper);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean contains(const Geodetic3D& position) const
  public final boolean contains(Geodetic3D position)
  {
	return contains(position.asGeodetic2D());
  }


//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean touchesWith(const Sector &that) const
  public final boolean touchesWith(Sector that)
  {
	// from Real-Time Collision Detection - Christer Ericson
	//   page 79
  
	// Exit with no intersection if separated along an axis
	if (_upper.latitude().lowerThan(that._lower.latitude()) || _lower.latitude().greaterThan(that._upper.latitude()))
	{
	  return false;
	}
	if (_upper.longitude().lowerThan(that._lower.longitude()) || _lower.longitude().greaterThan(that._upper.longitude()))
	{
	  return false;
	}
  
	// Overlapping on all axes means Sectors are intersecting
	return true;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Angle getDeltaLatitude() const
  public final Angle getDeltaLatitude()
  {
	return _deltaLatitude;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Angle getDeltaLongitude() const
  public final Angle getDeltaLongitude()
  {
	return _deltaLongitude;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Geodetic2D getSW() const
  public final Geodetic2D getSW()
  {
	return _lower;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Geodetic2D getNE() const
  public final Geodetic2D getNE()
  {
	return _upper;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Geodetic2D getNW() const
  public final Geodetic2D getNW()
  {
	return new Geodetic2D(_upper.latitude(), _lower.longitude());
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Geodetic2D getSE() const
  public final Geodetic2D getSE()
  {
	return new Geodetic2D(_lower.latitude(), _upper.longitude());
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Geodetic2D getCenter() const
  public final Geodetic2D getCenter()
  {
	return new Geodetic2D(Angle.midAngle(_lower.latitude(), _upper.latitude()), Angle.midAngle(_lower.longitude(), _upper.longitude()));
  }

  // (u,v) are similar to texture coordinates inside the Sector
  // (u,v)=(0,0) in NW point, and (1,1) in SE point

  // (u,v) are similar to texture coordinates inside the Sector
  // (u,v)=(0,0) in NW point, and (1,1) in SE point
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Geodetic2D getInnerPoint(double u, double v) const
  public final Geodetic2D getInnerPoint(double u, double v)
  {
	final Angle lat = Angle.lerp(_lower.latitude(), _upper.latitude(), (float)(1-v));
	final Angle lon = Angle.lerp(_lower.longitude(), _upper.longitude(), (float) u);
	return new Geodetic2D(lat, lon);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector2D getUVCoordinates(const Geodetic2D& point) const
  public final Vector2D getUVCoordinates(Geodetic2D point)
  {
	return getUVCoordinates(point.latitude(), point.longitude());
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector2D getUVCoordinates(const Angle& latitude, const Angle& longitude) const
  public final Vector2D getUVCoordinates(Angle latitude, Angle longitude)
  {
	final double u = longitude.sub(_lower.longitude()).div(getDeltaLongitude());
	final double v = _upper.latitude().sub(latitude).div(getDeltaLatitude());
	return new Vector2D(u, v);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean isBackOriented(const RenderContext *rc) const
  public final boolean isBackOriented(RenderContext rc)
  {
	final Camera camera = rc.getCamera();
	final Planet planet = rc.getPlanet();
  
	// compute sector point nearest to centerPoint
	final Geodetic2D center = camera.getCenterOfView().asGeodetic2D();
	final Geodetic2D point = getClosestPoint(center);
  
	// compute angle between normals
	final Vector3D normal = planet.geodeticSurfaceNormal(point);
	final Vector3D view = camera.getViewDirection().times(-1);
	final double dot = normal.dot(view);
	return (dot < 0) ? true : false;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Geodetic2D getClosestPoint(const Geodetic2D& pos) const
  public final Geodetic2D getClosestPoint(Geodetic2D pos)
  {
	final Angle lat = pos.latitude().nearestAngleInInterval(_lower.latitude(), _upper.latitude());
	final Angle lon = pos.longitude().nearestAngleInInterval(_lower.longitude(), _upper.longitude());
	return new Geodetic2D(lat, lon);
  }

}