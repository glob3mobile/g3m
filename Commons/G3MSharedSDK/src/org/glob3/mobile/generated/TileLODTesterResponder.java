package org.glob3.mobile.generated; 
//
//  TileLODTesterResponder.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/21/16.
//
//

//
//  TileLODTesterResponder.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/21/16.
//
//



public abstract class TileLODTesterResponder extends TileLODTester
{

  private TileLODTester _nextTesterRightLOD;
  private TileLODTester _nextTesterWrongLOD;


  protected abstract boolean _meetsRenderCriteria(Tile tile, G3MRenderContext rc, TilesRenderParameters tilesRenderParameters, ITimer lastSplitTimer, double texWidthSquared, double texHeightSquared, long nowInMS);

  protected void _onTileHasChangedMesh(Tile tile)
  {
  }

  protected abstract void _onLayerTilesRenderParametersChanged(LayerTilesRenderParameters ltrp);


  public TileLODTesterResponder(TileLODTester nextTesterRightLOD, TileLODTester nextTesterWrongLOD)
  {
     _nextTesterRightLOD = nextTesterRightLOD;
     _nextTesterWrongLOD = nextTesterWrongLOD;
  }

  public void dispose()
  {
    if (_nextTesterRightLOD != null)
       _nextTesterRightLOD.dispose();
    if (_nextTesterWrongLOD != null)
       _nextTesterWrongLOD.dispose();
    super.dispose();
  }

  public final boolean meetsRenderCriteria(Tile tile, G3MRenderContext rc, TilesRenderParameters tilesRenderParameters, ITimer lastSplitTimer, double texWidthSquared, double texHeightSquared, long nowInMS)
  {
    //Right LOD
    if (_meetsRenderCriteria(tile, rc, tilesRenderParameters, lastSplitTimer, texWidthSquared, texHeightSquared, nowInMS))
    {
      if (_nextTesterRightLOD != null)
      {
        return _nextTesterRightLOD.meetsRenderCriteria(tile, rc, tilesRenderParameters, lastSplitTimer, texWidthSquared, texHeightSquared, nowInMS);
      }
      return true;
    }
  
    //Wrong LOD
    if (_nextTesterWrongLOD != null)
    {
      return _nextTesterWrongLOD.meetsRenderCriteria(tile, rc, tilesRenderParameters, lastSplitTimer, texWidthSquared, texHeightSquared, nowInMS);
    }
  
    return false;
  }

  public final void onTileHasChangedMesh(Tile tile)
  {
    _onTileHasChangedMesh(tile);
    if (_nextTesterRightLOD != null)
    {
      _nextTesterRightLOD.onTileHasChangedMesh(tile);
    }
    if (_nextTesterWrongLOD != null)
    {
      _nextTesterWrongLOD.onTileHasChangedMesh(tile);
    }
  }

  public final void onLayerTilesRenderParametersChanged(LayerTilesRenderParameters ltrp)
  {
    _onLayerTilesRenderParametersChanged(ltrp);
    if (_nextTesterRightLOD != null)
    {
      _nextTesterRightLOD.onLayerTilesRenderParametersChanged(ltrp);
    }
    if (_nextTesterWrongLOD != null)
    {
      _nextTesterWrongLOD.onLayerTilesRenderParametersChanged(ltrp);
    }
  }
}