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
  private TileLODTester _nextTesterVisible;
  private TileLODTester _nextTesterNotVisible;


  protected abstract boolean _meetsRenderCriteria(Tile tile, G3MRenderContext rc);

  protected abstract boolean _isVisible(Tile tile, G3MRenderContext rc);

  protected void _onTileHasChangedMesh(Tile tile)
  {
  }

  protected abstract void _onLayerTilesRenderParametersChanged(LayerTilesRenderParameters ltrp);


  public TileLODTesterResponder(TileLODTester nextTesterRightLOD, TileLODTester nextTesterWrongLOD, TileLODTester nextTesterVisible, TileLODTester nextTesterNotVisible)
  {
     _nextTesterRightLOD = nextTesterRightLOD;
     _nextTesterWrongLOD = nextTesterWrongLOD;
     _nextTesterVisible = nextTesterVisible;
     _nextTesterNotVisible = nextTesterNotVisible;

  }

  public void dispose()
  {
    if (_nextTesterNotVisible != null)
       _nextTesterNotVisible.dispose();
    if (_nextTesterRightLOD != null)
       _nextTesterRightLOD.dispose();
    if (_nextTesterVisible != null)
       _nextTesterVisible.dispose();
    if (_nextTesterWrongLOD != null)
       _nextTesterWrongLOD.dispose();
  }

  public final boolean meetsRenderCriteria(Tile tile, G3MRenderContext rc)
  {
  
    //Right LOD
    if (_meetsRenderCriteria(tile, rc))
    {
      if (_nextTesterRightLOD != null)
      {
        return _nextTesterRightLOD.meetsRenderCriteria(tile, rc);
      }
      return true;
    }
  
    //Wrong LOD
    if (_nextTesterWrongLOD != null)
    {
      return _nextTesterWrongLOD.meetsRenderCriteria(tile, rc);
    }
  
    return false;
  }

  public final boolean isVisible(Tile tile, G3MRenderContext rc)
  {
    //Visible
    if (_isVisible(tile, rc))
    {
      if (_nextTesterVisible != null)
      {
        return _nextTesterVisible.isVisible(tile, rc);
      }
      return true;
    }
  
    //Not visible
    if (_nextTesterNotVisible != null)
    {
      return _nextTesterNotVisible.isVisible(tile, rc);
    }
  
    return false;
  }

  public final void onTileHasChangedMesh(Tile tile)
  {
    _onTileHasChangedMesh(tile);
    if (_nextTesterNotVisible != null)
      _nextTesterNotVisible.onTileHasChangedMesh(tile);
    if (_nextTesterVisible != null)
      _nextTesterVisible.onTileHasChangedMesh(tile);
    if (_nextTesterRightLOD != null)
      _nextTesterRightLOD.onTileHasChangedMesh(tile);
    if (_nextTesterWrongLOD != null)
      _nextTesterWrongLOD.onTileHasChangedMesh(tile);
  }

  public final void onLayerTilesRenderParametersChanged(LayerTilesRenderParameters ltrp)
  {
    _onLayerTilesRenderParametersChanged(ltrp);
    if (_nextTesterNotVisible != null)
      _nextTesterNotVisible.onLayerTilesRenderParametersChanged(ltrp);
    if (_nextTesterVisible != null)
      _nextTesterVisible.onLayerTilesRenderParametersChanged(ltrp);
    if (_nextTesterRightLOD != null)
      _nextTesterRightLOD.onLayerTilesRenderParametersChanged(ltrp);
    if (_nextTesterWrongLOD != null)
      _nextTesterWrongLOD.onLayerTilesRenderParametersChanged(ltrp);
  }
}