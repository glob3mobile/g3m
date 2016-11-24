//
//  WGS84Projetion.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/14/16.
//
//

#ifndef WGS84Projetion_hpp
#define WGS84Projetion_hpp

#include "Projection.hpp"

class WGS84Projetion : public Projection {
private:
  static WGS84Projetion* INSTANCE;

  WGS84Projetion();

protected:
  virtual ~WGS84Projetion();

public:

  static WGS84Projetion* instance();

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
