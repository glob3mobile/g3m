package org.glob3.mobile.generated; 
public class MapBooBuilder_TerrainTouchListener implements TerrainTouchListener
{
  private MapBooBuilder _mapBooBuilder;

  public MapBooBuilder_TerrainTouchListener(MapBooBuilder mapBooBuilder)
  {
     _mapBooBuilder = mapBooBuilder;
  }

  public final boolean onTerrainTouch(G3MEventContext ec, Camera camera, Geodetic3D position, Tile tile)
  {
    return _mapBooBuilder.onTerrainTouch(ec, camera, position, tile);
  }
}