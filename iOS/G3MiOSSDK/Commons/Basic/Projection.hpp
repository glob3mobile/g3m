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
class Geodetic2D;
class Vector2D;
class MutableVector2D;


class Projection : public RCObject {
private:

protected:

  Projection();

  virtual ~Projection();

public:

  virtual const std::string getEPSG() const = 0;


  virtual double getU(const Angle& longitude) const = 0;
  virtual double getV(const Angle& latitude) const = 0;

  virtual Vector2D getUV(const Angle& latitude,
                         const Angle& longitude) const;
  virtual Vector2D getUV(const Geodetic2D& position) const;

  virtual void getUV(const Angle& latitude,
                     const Angle& longitude,
                     MutableVector2D& result) const;
  virtual void getUV(const Geodetic2D& position,
                     MutableVector2D& result) const;

};

#endif
