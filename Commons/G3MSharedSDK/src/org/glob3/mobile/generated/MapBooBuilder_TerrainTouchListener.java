package org.glob3.mobile.generated; 
public class MapBooBuilder_TerrainTouchListener implements TerrainTouchListener
{
  private MapBooBuilder _mapBooBuilder;

  public MapBooBuilder_TerrainTouchListener(MapBooBuilder mapBooBuilder)
  {
     _mapBooBuilder = mapBooBuilder;
  }

  public void dispose()
  {

  }

  public final boolean onTerrainTouch(G3MEventContext ec, Vector2I pixel, Camera camera, Geodetic3D position, Tile tile)
  {
    return _mapBooBuilder.onTerrainTouch(ec, pixel, camera, position, tile);
  }
}