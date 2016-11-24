//
//  WebMercatorProjection.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/14/16.
//
//

#ifndef WebMercatorProjection_hpp
#define WebMercatorProjection_hpp

#include "Projection.hpp"

class WebMercatorProjection : public Projection {
private:
  static WebMercatorProjection* INSTANCE;

  static const double _upperLimitRadians;
  static const double _lowerLimitRadians;

  //  static const double _upperLimitDegrees;
  //  static const double _lowerLimitDegrees;

  WebMercatorProjection();

protected:
  virtual ~WebMercatorProjection();

public:

  static WebMercatorProjection* instance();

  const std::string getEPSG() const;

  double getU(const Angle& longitude) const;
  double getV(const Angle& latitude) const;

  const Angle getInnerPointLongitude(double u) const;
  const Angle getInnerPointLatitude(double v) const;

  const Angle getInnerPointLongitude(const Sector& sector,
                                     double u) const;
  const Angle getInnerPointLatitude(const Sector& sector,
                                    double v) const;

};

#endif
