package org.glob3.mobile.generated; 
public abstract class TileLODTesterResponder extends TileLODTester
{

  private TileLODTester _nextTesterRightLOD;
  private TileLODTester _nextTesterWrongLOD;
  private TileLODTester _nextTesterVisible;
  private TileLODTester _nextTesterNotVisible;


  protected abstract boolean _meetsRenderCriteria(int testerLevel, Tile tile, G3MRenderContext rc);

  protected abstract boolean _isVisible(int testerLevel, Tile tile, G3MRenderContext rc);

  protected void _onTileHasChangedMesh(int testerLevel, Tile tile)
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

  public final boolean meetsRenderCriteria(int testerLevel, Tile tile, G3MRenderContext rc)
  {
  
    //Right LOD
    if (_meetsRenderCriteria(testerLevel, tile, rc))
    {
      if (_nextTesterRightLOD != null)
      {
        return _nextTesterRightLOD.meetsRenderCriteria(testerLevel + 1, tile, rc);
      }
      return true;
    }
  
    //Wrong LOD
    if (_nextTesterWrongLOD != null)
    {
      return _nextTesterWrongLOD.meetsRenderCriteria(testerLevel + 1, tile, rc);
    }
    return false;
  }

  public final boolean isVisible(int testerLevel, Tile tile, G3MRenderContext rc)
  {
  
    //Visible
    if (_isVisible(testerLevel, tile, rc))
    {
      if (_nextTesterVisible != null)
      {
        return _nextTesterVisible.isVisible(testerLevel+1, tile, rc);
      }
      return true;
    }
  
    //Not visible
    if (_nextTesterNotVisible != null)
    {
      return _nextTesterNotVisible.isVisible(testerLevel+1, tile, rc);
    }
    return false;
  
  }

  public final void onTileHasChangedMesh(int testerLevel, Tile tile)
  {
    _onTileHasChangedMesh(testerLevel, tile);
    if (_nextTesterNotVisible != null)
      _nextTesterNotVisible.onTileHasChangedMesh(testerLevel+1, tile);
    if (_nextTesterVisible != null)
      _nextTesterVisible.onTileHasChangedMesh(testerLevel+1, tile);
    if (_nextTesterRightLOD != null)
      _nextTesterRightLOD.onTileHasChangedMesh(testerLevel+1, tile);
    if (_nextTesterWrongLOD != null)
      _nextTesterWrongLOD.onTileHasChangedMesh(testerLevel+1, tile);
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