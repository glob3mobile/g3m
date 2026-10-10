package org.glob3.mobile.generated;
public class ReplaceTileLODTesterTask extends GTask
{
  private PlanetRenderer _planetRenderer;
  private TileLODTester _tileLODTester;

  public ReplaceTileLODTesterTask(PlanetRenderer planetRenderer, TileLODTester tileLODTester)
  {
     _planetRenderer = planetRenderer;
     _tileLODTester = tileLODTester;
  }

  public final void run(G3MContext context)
  {
    _planetRenderer.replaceTileLODTester(_tileLODTester);
  }
}