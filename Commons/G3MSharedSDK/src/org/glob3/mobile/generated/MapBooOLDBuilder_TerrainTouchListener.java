package org.glob3.mobile.generated; 
public class MapBooOLDBuilder_TerrainTouchListener implements TerrainTouchListener
{
  private MapBooOLDBuilder _mapBooBuilder;

  public MapBooOLDBuilder_TerrainTouchListener(MapBooOLDBuilder mapBooBuilder)
  {
     _mapBooBuilder = mapBooBuilder;
  }

  public void dispose()
  {

  }

  public final boolean onTerrainTouch(G3MEventContext ec, Vector2F pixel, Camera camera, Geodetic3D position, Tile tile)
  {
    return _mapBooBuilder.onTerrainTouch(ec, pixel, camera, position, tile);
  }
}