package org.glob3.mobile.generated; 
public class RecreateTilesTask extends GTask
{
  private TileRenderer _tileRenderer;
  public RecreateTilesTask(TileRenderer tileRenderer)
  {
     _tileRenderer = tileRenderer;
  }

  public final void run(G3MContext context)
  {
    _tileRenderer.recreateTiles();
  }
}