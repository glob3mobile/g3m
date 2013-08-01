package org.glob3.mobile.generated; 
public class RecreateTilesTask extends GTask
{
  private PlanetRenderer _planetRenderer;
  public RecreateTilesTask(PlanetRenderer planetRenderer)
  {
     _planetRenderer = planetRenderer;
  }

  public final void run(G3MContext context)
  {
    _planetRenderer.recreateTiles();
  }
}