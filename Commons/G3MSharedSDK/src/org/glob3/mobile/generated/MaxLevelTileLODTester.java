package org.glob3.mobile.generated;//
//  MaxLevelTileLODTester.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 4/12/15.
//
//

//
//  MaxLevelTileLODTester.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 4/12/15.
//
//




public class MaxLevelTileLODTester extends TileLODTester
{
  private int _maxLevel;
  private int _maxLevelForPoles;



  public MaxLevelTileLODTester()
  {
     _maxLevel = -1;
     _maxLevelForPoles = -1;
  }

  public void dispose()
  {
    super.dispose();
  }

  public final boolean meetsRenderCriteria(G3MRenderContext rc, PlanetRenderContext prc, Tile tile)
  {
  
    if (_maxLevel < 0)
    {
      return true;
    }
  
    if (tile._level >= _maxLevel)
    {
      return true;
    }
  
    if ((tile._level >= _maxLevelForPoles) && (tile._sector.touchesPoles()))
    {
      return true;
    }
  
    return false;
  }

  public final void onTileHasChangedMesh(Tile tile)
  {

  }

  public final void onLayerTilesRenderParametersChanged(LayerTilesRenderParameters ltrp)
  {
    if (ltrp == null)
    {
      _maxLevel = -1;
      _maxLevelForPoles = -1;
    }
    else
    {
      _maxLevel = ltrp._maxLevel;
      _maxLevelForPoles = ltrp._maxLevelForPoles;
    }
  }

  public final void renderStarted()
  {

  }

}
