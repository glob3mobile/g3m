//
//  Geodetic3D.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 31/05/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

#ifndef G3MiOSSDK_Geodetic3D_hpp
#define G3MiOSSDK_Geodetic3D_hpp

#include "Angle.hpp"
#include "Geodetic2D.hpp"


/**
 * Class to represent a position in the globe by latitude, longitud and altitude.
 */
class Geodetic3D {
private:
  const Angle _latitude;
  const Angle _longitude;
  const double _height;
  
public:
  
  ~Geodetic3D(){}
  
  static Geodetic3D nan() {
    return Geodetic3D(Angle::nan(), Angle::nan(), 0);
  }
  
  bool isNan() const{
    return _latitude.isNan() || _longitude.isNan();
  }
  
  static Geodetic3D zero() {
    return Geodetic3D(Angle::zero(), Angle::zero(), 0);
  }
  
  static Geodetic3D fromDegrees(double lat, double lon, double height) {
    return Geodetic3D(Angle::fromDegrees(lat), Angle::fromDegrees(lon), height);
  }
  
  static Geodetic3D linearInterpolation(const Geodetic3D& from,
                                        const Geodetic3D& to,
                                        double alpha){
    return Geodetic3D(Angle::linearInterpolation(from.latitude(),  to.latitude(),  alpha),
                      Angle::linearInterpolation(from.longitude(), to.longitude(), alpha),
                      IMathUtils::instance()->linearInterpolation(from.height(), to.height(), alpha)
                      //((1.0 - alpha) * from.height()) + (alpha * to.height())
                      );
  }
  
  Geodetic3D(const Angle& latitude,
             const Angle& longitude,
             const double height): _latitude(latitude), _longitude(longitude), _height(height) {
  }
  
  Geodetic3D(const Geodetic2D& g2,
             const double height): _latitude(g2.latitude()), _longitude(g2.longitude()), _height(height) {
  }
  
  Geodetic3D(const Geodetic3D& g): _latitude(g._latitude), _longitude(g._longitude), _height(g._height) {
  }
  
  const Angle latitude() const {
    return _latitude;
  }
  
  const Angle longitude() const {
    return _longitude;
  }
  
  double height() const {
    return _height;
  }
  
  Geodetic2D asGeodetic2D() const {
    return Geodetic2D(_latitude, _longitude);
  }
  
  const std::string description() const;
  
  
  Geodetic3D add(const Geodetic3D& that) const {
    return Geodetic3D(_latitude.add(that._latitude),
                      _longitude.add(that._longitude),
                      _height + that._height);
  }
  
  Geodetic3D sub(const Geodetic3D& that) const {
    return Geodetic3D(_latitude.sub(that._latitude),
                      _longitude.sub(that._longitude),
                      _height - that._height);
  }
  
  Geodetic3D times(const double magnitude) const {
    return Geodetic3D(_latitude.times(magnitude),
                      _longitude.times(magnitude),
                      _height * magnitude);
  }
  
  Geodetic3D div(const double magnitude) const {
    return Geodetic3D(_latitude.div(magnitude),
                      _longitude.div(magnitude),
                      _height / magnitude);
  }
  
  /**
   * Given a start point, initial bearing, and distance, this will calculate the destination point and final bearing travelling along a (shortest distance) great
   * circle arc.
   * See http://www.movable-type.co.uk/scripts/latlong.html
   *
   */
  
  static Geodetic3D calculateRhumbLineDestination(const Geodetic3D& position,
                                                  const double distance,
                                                  const double R,
                                                  Angle bearing) {
    
    int _ERROR_IN_ANGLE_CONSTRUCTOR;
    //bearing = bearing.normalized();
    
    const double angularDistance = distance / (R + position.height());
    
    const double dLat = angularDistance * bearing.cosinus();
    
    const double nLatRadians = position.latitude().radians() + dLat;
    const Angle aLat = Angle::fromRadians(nLatRadians);
    
    const double tg1 = IMathUtils::instance()->tan((IMathUtils::instance()->pi() / 4) + (aLat.radians() / 2));
    const double tg2 = IMathUtils::instance()->tan((IMathUtils::instance()->pi() / 4) + position.latitude().radians());
    
    const double stretchedLatitudeDifference = IMathUtils::instance()->log(tg1 / tg2);
    ILogger::instance()->logWarning("stretchedLatitudeDifference: %", stretchedLatitudeDifference);
    
    double q;
    if ((dLat != 0) && !IMathUtils::instance()->isNan(stretchedLatitudeDifference)) {
      q = dLat / stretchedLatitudeDifference;
    }
    else {
      q = position.latitude().cosinus();
    }
    
    const double dLon = (angularDistance * bearing.sinus()) / q;
    //TODO: this
    //// check for some daft bugger going past the pole, normalise latitude if so
    //if (Math.abs(lat2) > Math.PI/2) lat2 = lat2>0 ? Math.PI-lat2 : -Math.PI-lat2;
    
    const double a = position.longitude().radians() + dLon + IMathUtils::instance()->pi();
    const double nLonRadians = IMathUtils::instance()->fmod(a,(2 * IMathUtils::instance()->pi()))- IMathUtils::instance()->pi();
    
    const Angle aLon = Angle::fromRadians(nLonRadians);
    
    
    const Geodetic3D destination =  Geodetic3D(aLat, aLon, position.height());
    //Only for debug
    ILogger::instance()->logWarning("Initial Position: " + position.description());
    ILogger::instance()->logWarning("Destination Position: " + destination.description());
    
    Angle angle = Geodetic2D::bearing(destination.asGeodetic2D(), position.asGeodetic2D());
    
    ILogger::instance()->logWarning("Angle between obj and behin position: " + angle.description() + ". Diference with heading: "
                                    + angle.sub(bearing).description());
    
    return destination;
  }
  
  
  static Geodetic3D calculateInverseRhumbLineDestination(const Geodetic3D& position,
                                                         const double distance,
                                                         const double R,
                                                         const Angle bearing) {
    return calculateRhumbLineDestination(position, distance, R, bearing.add(Angle::fromDegrees(180)));
  }
  
  
};



#endif
