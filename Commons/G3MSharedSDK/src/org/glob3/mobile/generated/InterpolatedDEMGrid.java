package org.glob3.mobile.generated; 
//
//  InterpolatedDEMGrid.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/18/16.
//
//

//
//  InterpolatedDEMGrid.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/18/16.
//
//




public class InterpolatedDEMGrid extends DecoratorDEMGrid
{
  private InterpolatedDEMGrid(DEMGrid grid, Sector sector, Vector2I extent)
  {
     super(grid, sector, extent);
  
  }

  public static InterpolatedDEMGrid create(DEMGrid grid, Vector2S extent)
  {
    return new InterpolatedDEMGrid(grid, grid.getSector(), new Vector2I(extent._x, extent._y));
  }

  public final double getElevationAt(int x, int y)
  {
  ///#error Diego at work!
    throw new RuntimeException("Diego at work!");
  }

}