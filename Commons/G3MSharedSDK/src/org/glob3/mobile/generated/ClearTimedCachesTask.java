package org.glob3.mobile.generated;
public class ClearTimedCachesTask extends GTask
{
  private PlanetRenderer _planetRenderer;

  public ClearTimedCachesTask(PlanetRenderer planetRenderer)
  {
     _planetRenderer = planetRenderer;
  }

  public final void run(G3MContext context)
  {
    _planetRenderer.clearTimedCaches();
  }
}