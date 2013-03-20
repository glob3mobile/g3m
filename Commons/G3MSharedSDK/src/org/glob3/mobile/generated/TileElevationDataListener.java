package org.glob3.mobile.generated; 
public class TileElevationDataListener implements IElevationDataListener
{
  private Tile _tile;
  private MeshHolder _meshHolder;
  private final TileTessellator _tessellator;
  private Planet _planet; // REMOVED FINAL WORD BY CONVERSOR RULE
  private final Vector2I _tileMeshResolution;
  private final boolean _renderDebug;
//  const float            _verticalExaggeration;
  public TileElevationDataListener(Tile tile, MeshHolder meshHolder, TileTessellator tessellator, Planet planet, Vector2I tileMeshResolution, boolean renderDebug)
//                            float verticalExaggeration,
//  _verticalExaggeration(verticalExaggeration),
  {
     _tile = tile;
     _meshHolder = meshHolder;
     _tessellator = tessellator;
     _planet = planet;
     _tileMeshResolution = tileMeshResolution;
     _renderDebug = renderDebug;

  }

  public void dispose()
  {
  }

  public final void onData(Sector sector, Vector2I resolution, ElevationData elevationData)
  {
    _tile.onElevationData(elevationData, _meshHolder, _tessellator, _planet, _tileMeshResolution, _renderDebug);
                           //_verticalExaggeration,
  }

  public final void onError(Sector sector, Vector2I resolution)
  {

  }
}