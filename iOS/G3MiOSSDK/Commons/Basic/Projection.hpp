//
//  Projection.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/14/16.
//
//

#ifndef Projection_hpp
#define Projection_hpp

#include "RCObject.hpp"

#include <string>

class Angle;


class Projection : public RCObject {
private:

protected:

  Projection();

  virtual ~Projection();

public:

  virtual const std::string getEPSG() const = 0;

  virtual double getU(const Angle& longitude) const = 0;
  virtual double getV(const Angle& latitude) const = 0;

};

#endif
