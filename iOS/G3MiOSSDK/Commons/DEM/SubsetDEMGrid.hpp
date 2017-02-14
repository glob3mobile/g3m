//
//  SubsetDEMGrid.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/17/16.
//
//

#ifndef SubsetDEMGrid_hpp
#define SubsetDEMGrid_hpp

#include "DecoratorDEMGrid.hpp"


class SubsetDEMGrid : public DecoratorDEMGrid {
private:
  const int _offsetX;
  const int _offsetY;

  SubsetDEMGrid(const DEMGrid*  grid,
                const Sector&   sector,
                const Vector2I& extent,
                const int       offsetX,
                const int       offsetY);

protected:

  ~SubsetDEMGrid();

public:

  static SubsetDEMGrid* create(const DEMGrid* grid,
                               const Sector&  sector);

  double getElevation(int x, int y) const;

};

#endif
