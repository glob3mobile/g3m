//
//  DecimatedDEMGrid.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/18/16.
//
//

#ifndef DecimatedDEMGrid_hpp
#define DecimatedDEMGrid_hpp

#include "DecoratorDEMGrid.hpp"

class DecimatedDEMGrid : public DecoratorDEMGrid {
private:

  DecimatedDEMGrid(const DEMGrid*  grid,
                   const Sector&   sector,
                   const Vector2I& extent);

  double getElevationBoxAt(double x0, double y0,
                           double x1, double y1) const;

public:
  static DecimatedDEMGrid* create(const DEMGrid*  grid,
                                  const Vector2S& extent);

  double getElevation(int x, int y) const;

};

#endif
