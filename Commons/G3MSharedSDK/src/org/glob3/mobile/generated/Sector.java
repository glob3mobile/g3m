package org.glob3.mobile.generated; 
//
//  Sector.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 22/06/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

//
//  Sector.hpp
//  G3MiOSSDK
//
//  Created by Agustin Trujillo Pino on 12/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//


//class G3MRenderContext;
//class Planet;
//class ICanvas;
//class GEORasterProjection;
//class GEORasterSymbol;
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
    public static final Sector NAN_SECTOR = Sector.fromDegrees(java.lang.Double.NaN, java.lang.Double.NaN, java.lang.Double.NaN, java.lang.Double.NaN);


    public final Geodetic2D _lower ;
    public final Geodetic2D _upper ;

    public final Geodetic2D _center ;

    public final Angle _deltaLatitude ;
    public final Angle _deltaLongitude ;


    public void dispose()
    {
      if (_normalizedCartesianCenter != null)
         _normalizedCartesianCenter.dispose();
    }

    public Sector(Geodetic2D lower, Geodetic2D upper)
    {
       _lower = new Geodetic2D(lower);
       _upper = new Geodetic2D(upper);
       _deltaLatitude = new Angle(upper._latitude.sub(lower._latitude));
       _deltaLongitude = new Angle(upper._longitude.sub(lower._longitude));
       _center = new Geodetic2D(Angle.midAngle(lower._latitude, upper._latitude), Angle.midAngle(lower._longitude, upper._longitude));
       _deltaRadiusInRadians = -1.0;
       _normalizedCartesianCenter = null;
    //    if (_deltaLatitude._degrees == 0){
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
    
    //    if (_deltaLatitude._degrees == 0){
    //        printf("NO AREA");
    //    }
    }

    public final boolean isNan()
    {
        return (_lower._latitude._degrees != _lower._latitude._degrees);
    }

    public final boolean hasNoArea()
    {
        return _deltaLatitude._radians == 0 || _deltaLongitude._radians == 0;
    }

    public static Sector fromDegrees(double minLat, double minLon, double maxLat, double maxLon)
    {
        final Geodetic2D lower = new Geodetic2D(Angle.fromDegrees(minLat), Angle.fromDegrees(minLon));
        final Geodetic2D upper = new Geodetic2D(Angle.fromDegrees(maxLat), Angle.fromDegrees(maxLon));

        return new Sector(lower, upper);
    }

    public final Vector2D div(Sector that)
    {
      final double scaleX = _deltaLongitude.div(that._deltaLongitude);
      final double scaleY = _deltaLatitude.div(that._deltaLatitude);
      return new Vector2D(scaleX, scaleY);
    }

    public final boolean fullContains(Sector that)
    {
      //return contains(that._upper) && contains(that._lower);
      return (contains(that._upper._latitude, that._upper._longitude) && contains(that._lower._latitude, that._lower._longitude));
    }

    public final Sector intersection(Sector that)
    {
      final Angle lowLat = Angle.max(_lower._latitude, that._lower._latitude);
      final Angle lowLon = Angle.max(_lower._longitude, that._lower._longitude);
    
      final Angle upLat = Angle.min(_upper._latitude, that._upper._latitude);
      final Angle upLon = Angle.min(_upper._longitude, that._upper._longitude);
    
      if (lowLat.lowerThan(upLat) && lowLon.lowerThan(upLon))
      {
        final Geodetic2D low = new Geodetic2D(lowLat, lowLon);
        final Geodetic2D up = new Geodetic2D(upLat, upLon);
    
        return new Sector(low, up);
      }
    
      return Sector.fromDegrees(0, 0, 0, 0); //invalid
    }

    public final Sector mergedWith(Sector that)
    {
      final Angle lowLat = Angle.min(_lower._latitude, that._lower._latitude);
      final Angle lowLon = Angle.min(_lower._longitude, that._lower._longitude);
      final Geodetic2D low = new Geodetic2D(lowLat, lowLon);
    
      final Angle upLat = Angle.max(_upper._latitude, that._upper._latitude);
      final Angle upLon = Angle.max(_upper._longitude, that._upper._longitude);
      final Geodetic2D up = new Geodetic2D(upLat, upLon);
    
      return new Sector(low, up);
    }

    public static Sector fullSphere()
    {
      return FULL_SPHERE;
    }

    public final boolean contains(Angle latitude, Angle longitude)
    {
      return (latitude.isBetween(_lower._latitude, _upper._latitude) && longitude.isBetween(_lower._longitude, _upper._longitude));
    }

    public final boolean contains(Geodetic2D position)
    {
        return contains(position._latitude, position._longitude);
    }

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

    public final Geodetic2D getSW()
    {
        return _lower;
    }

    public final Geodetic2D getNE()
    {
        return _upper;
    }

    public final Geodetic2D getNW()
    {
        return new Geodetic2D(_upper._latitude, _lower._longitude);
    }

    public final Geodetic2D getSE()
    {
        return new Geodetic2D(_lower._latitude, _upper._longitude);
    }

    public final Geodetic2D getCenter()
    {
        return _center;
    }

    // (u,v) are similar to texture coordinates inside the Sector
    // (u,v)=(0,0) in NW point, and (1,1) in SE point

    // (u,v) are similar to texture coordinates inside the Sector
    // (u,v)=(0,0) in NW point, and (1,1) in SE point
    public final Geodetic2D getInnerPoint(double u, double v)
    {
      return new Geodetic2D(Angle.linearInterpolation(_lower._latitude, _upper._latitude, 1.0 - v), Angle.linearInterpolation(_lower._longitude, _upper._longitude, u));
    }

    public final Angle getInnerPointLongitude(double u)
    {
      return Angle.linearInterpolation(_lower._longitude, _upper._longitude, u);
    }
    public final Angle getInnerPointLatitude(double v)
    {
      return Angle.linearInterpolation(_lower._latitude, _upper._latitude, 1.0 - v);
    }


    public final Vector2D getUVCoordinates(Geodetic2D point)
    {
        return getUVCoordinates(point._latitude, point._longitude);
    }

    public final Vector2D getUVCoordinates(Angle latitude, Angle longitude)
    {
        return new Vector2D((longitude._radians - _lower._longitude._radians) / _deltaLongitude._radians, (_upper._latitude._radians - latitude._radians) / _deltaLatitude._radians);
    }

    public final Vector2F getUVCoordinatesF(Angle latitude, Angle longitude)
    {
        return new Vector2F((float)((longitude._radians - _lower._longitude._radians) / _deltaLongitude._radians), (float)((_upper._latitude._radians - latitude._radians) / _deltaLatitude._radians));
    }

    public final double getUCoordinate(Angle longitude)
    {
        return (longitude._radians - _lower._longitude._radians) / _deltaLongitude._radians;
    }

    public final double getVCoordinate(Angle latitude)
    {
        return (_upper._latitude._radians - latitude._radians) / _deltaLatitude._radians;
    }


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
      final double angleInRadians = java.lang.Math.acos(dot);
    
      return ((angleInRadians - getDeltaRadiusInRadians()) > cameraAngle2HorizonInRadians);
    }

    public final Geodetic2D clamp(Geodetic2D position)
    {
      if (contains(position))
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

    public final Geodetic2D clamp(Angle latitude, Angle longitude)
    {
      if (contains(latitude, longitude))
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

    public final Angle clampLatitude(Angle latitude)
    {
      return latitude.clampedTo(_lower._latitude, _upper._latitude);
    }
    public final Angle clampLongitude(Angle longitude)
    {
      return longitude.clampedTo(_lower._longitude, _upper._longitude);
    }

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
    @Override
    public String toString() {
        return description();
    }

    public final Sector shrinkedByPercentP(float percent)
    {
        final Angle deltaLatitude = _deltaLatitude.times(percent).div(2);
        final Angle deltaLongitude = _deltaLongitude.times(percent).div(2);

        final Geodetic2D delta = new Geodetic2D(deltaLatitude, deltaLongitude);

        return new Sector(_lower.add(delta), _upper.sub(delta));
    }

    public final Sector shrinkedByPercent(float percent)
    {
        final Angle deltaLatitude = _deltaLatitude.times(percent).div(2);
        final Angle deltaLongitude = _deltaLongitude.times(percent).div(2);

        final Geodetic2D delta = new Geodetic2D(deltaLatitude, deltaLongitude);

        return new Sector(_lower.add(delta), _upper.sub(delta));
    }

    public final boolean isEquals(Sector that)
    {
        return _lower.isEquals(that._lower) && _upper.isEquals(that._upper);
    }

    public final boolean touchesNorthPole()
    {
        return (_upper._latitude._degrees >= 89.9);
    }

    public final boolean touchesSouthPole()
    {
        return (_lower._latitude._degrees <= -89.9);
    }

    public final Angle getNorth()
    {
        return _upper._latitude;
    }

    public final Angle getSouth()
    {
        return _lower._latitude;
    }

    public final Angle getEast()
    {
        return _upper._longitude;
    }

    public final Angle getWest()
    {
        return _lower._longitude;
    }

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

    public final boolean touchesPoles()
    {
        return ((_upper._latitude._degrees >= 89.9) || (_lower._latitude._degrees <= -89.9));
    }

    public final double getDeltaRadiusInRadians()
    {
        if (_deltaRadiusInRadians < 0)
        {
            _deltaRadiusInRadians = IMathUtils.instance().sqrt((_deltaLatitude._radians * _deltaLatitude._radians) + (_deltaLongitude._radians * _deltaLongitude._radians)) * 0.5;
        }
        return _deltaRadiusInRadians;
    }

    public final Vector3D getNormalizedCartesianCenter(Planet planet)
    {
      if (_normalizedCartesianCenter == null)
      {
        _normalizedCartesianCenter = new Vector3D(planet.toCartesian(_center).normalized());
      }
      return _normalizedCartesianCenter;
    }

    public final double getAngularAreaInSquaredDegrees()
    {
        return _deltaLatitude._degrees * _deltaLongitude._degrees;
    }

    public final GEORasterSymbol createGEOSymbol(Color c)
    {
    
      java.util.ArrayList<Geodetic2D> coordinates = new java.util.ArrayList<Geodetic2D>();
    
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


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + ((_lower == null) ? 0 : _lower.hashCode());
        result = (prime * result) + ((_upper == null) ? 0 : _upper.hashCode());
        return result;
    }
    
    
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Sector other = (Sector) obj;
        if (_lower == null) {
            if (other._lower != null) {
                return false;
            }
        }
        else if (!_lower.equals(other._lower)) {
            return false;
        }
        if (_upper == null) {
            if (other._upper != null) {
                return false;
            }
        }
        else if (!_upper.equals(other._upper)) {
            return false;
        }
        return true;
    }

}