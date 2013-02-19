package org.glob3.mobile.generated; 
public class TileElevationDataListener implements IElevationDataListener
{
  private Tile _tile;
  private MeshHolder _meshHolder;
  private final TileTessellator _tessellator;
  private Planet _planet; // REMOVED FINAL WORD BY CONVERSOR RULE
  private final boolean _renderDebug;
  private final float _verticalExaggeration;

  public TileElevationDataListener(Tile tile, MeshHolder meshHolder, TileTessellator tessellator, Planet planet, float verticalExaggeration, boolean renderDebug)
  {
     _tile = tile;
     _meshHolder = meshHolder;
     _tessellator = tessellator;
     _planet = planet;
     _verticalExaggeration = verticalExaggeration;
     _renderDebug = renderDebug;

  }

  public void dispose()
  {
  }

  public final void onData(Sector sector, Vector2I resolution, ElevationData elevationData)
  {
    _tile.onElevationData(elevationData, _verticalExaggeration, _meshHolder, _tessellator, _planet, _renderDebug);
  }

  public final void onError(Sector sector, Vector2I resolution)
  {

  }
}