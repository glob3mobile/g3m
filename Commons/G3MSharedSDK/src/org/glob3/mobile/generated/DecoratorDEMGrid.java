package org.glob3.mobile.generated;
//
//  DecoratorDEMGrid.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 11/17/16.
//
//

//
//  DecoratorDEMGrid.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 11/17/16.
//
//




public abstract class DecoratorDEMGrid extends DEMGrid
{


  protected final DEMGrid _grid;

  protected DecoratorDEMGrid(DEMGrid grid, Sector sector, Vector2I extent)
  {
     super(sector, extent);
     _grid = grid;
    _grid._retain();
  }

  public void dispose()
  {
    _grid._release();
    super.dispose();
  }


  public final Projection getProjection()
  {
    return _grid.getProjection();
  }

}
