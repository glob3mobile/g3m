package org.glob3.mobile.generated; 
public abstract class TileLODTesterResponder extends TileLODTester
{

  private TileLODTester _nextTesterRightLoD;
  private TileLODTester _nextTesterWrongLoD;
  private TileLODTester _nextTesterVisible;
  private TileLODTester _nextTesterNotVisible;


  protected abstract boolean _meetsRenderCriteria(int testerLevel, Tile tile, G3MRenderContext rc);

  protected abstract boolean _isVisible(int testerLevel, Tile tile, G3MRenderContext rc);

  protected void _onTileHasChangedMesh(int testerLevel, Tile tile)
  {
  }


  public TileLODTesterResponder(TileLODTester nextTesterRightLoD, TileLODTester nextTesterWrongLoD, TileLODTester nextTesterVisible, TileLODTester nextTesterNotVisible)
  {
     _nextTesterRightLoD = nextTesterRightLoD;
     _nextTesterWrongLoD = nextTesterWrongLoD;
     _nextTesterVisible = nextTesterVisible;
     _nextTesterNotVisible = nextTesterNotVisible;

  }

  public void dispose()
  {
    if (_nextTesterNotVisible != null)
       _nextTesterNotVisible.dispose();
    if (_nextTesterRightLoD != null)
       _nextTesterRightLoD.dispose();
    if (_nextTesterVisible != null)
       _nextTesterVisible.dispose();
    if (_nextTesterWrongLoD != null)
       _nextTesterWrongLoD.dispose();
  }

  public final boolean meetsRenderCriteria(int testerLevel, Tile tile, G3MRenderContext rc)
  {
  
    //Right LOD
    if (_meetsRenderCriteria(testerLevel, tile, rc))
    {
      if (_nextTesterRightLoD != null)
      {
        return _nextTesterRightLoD.meetsRenderCriteria(testerLevel + 1, tile, rc);
      }
      return true;
    }
  
    //Wrong LOD
    if (_nextTesterWrongLoD != null)
    {
      return _nextTesterWrongLoD.meetsRenderCriteria(testerLevel + 1, tile, rc);
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

  public void onTileHasChangedMesh(int testerLevel, Tile tile)
  {
    _onTileHasChangedMesh(testerLevel, tile);
    if (_nextTesterNotVisible != null)
      _nextTesterNotVisible.onTileHasChangedMesh(testerLevel+1, tile);
    if (_nextTesterVisible != null)
      _nextTesterVisible.onTileHasChangedMesh(testerLevel+1, tile);
    if (_nextTesterRightLoD != null)
      _nextTesterRightLoD.onTileHasChangedMesh(testerLevel+1, tile);
    if (_nextTesterWrongLoD != null)
      _nextTesterWrongLoD.onTileHasChangedMesh(testerLevel+1, tile);
  }
}