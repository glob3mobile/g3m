//
//  DecoratorDEMGrid.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/17/16.
//
//

#ifndef DecoratorDEMGrid_hpp
#define DecoratorDEMGrid_hpp

#include "DEMGrid.hpp"


class DecoratorDEMGrid : public DEMGrid {

protected:

  const DEMGrid* _grid;

  DecoratorDEMGrid(const DEMGrid*  grid,
                   const Sector&   sector,
                   const Vector2I& extent);

  virtual ~DecoratorDEMGrid();

public:

  const Projection* getProjection() const;
  
};

#endif
