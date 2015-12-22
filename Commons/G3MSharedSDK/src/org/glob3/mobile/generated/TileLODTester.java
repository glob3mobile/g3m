package org.glob3.mobile.generated; 
public abstract class TileLODTester
{


  public TileLODTester()
  {
  }

  public void dispose()
  {
  }

  public abstract boolean meetsRenderCriteria(int testerLevel, Tile tile, G3MRenderContext rc);

  public abstract boolean isVisible(int testerLevel, Tile tile, G3MRenderContext rc);

  public abstract void onTileHasChangedMesh(int testerLevel, Tile tile);
}