//
//  DEMGrid.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 10/5/16.
//
//

#ifndef DEMGrid_hpp
#define DEMGrid_hpp

#include "RCObject.hpp"

#include "Sector.hpp"
#include "Vector2I.hpp"

class Projection;


class DEMGrid : public RCObject {
protected:
  const Sector     _sector;
#ifdef C_CODE
  const Vector2I   _extent;
#endif
#ifdef JAVA_CODE
  protected final Vector2I _extent;
#endif
  const Geodetic2D _resolution;

  DEMGrid(const Sector&   sector,
          const Vector2I& extent);

  virtual ~DEMGrid();

public:

  const Sector getSector() const;

  const Vector2I getExtent() const;

  const Geodetic2D getResolution() const;

  virtual const Projection* getProjection() const = 0;

  virtual double getElevation(int x, int y) const = 0;
  
};

#endif
