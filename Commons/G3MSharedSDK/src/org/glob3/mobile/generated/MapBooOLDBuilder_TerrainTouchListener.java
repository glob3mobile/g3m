package org.glob3.mobile.generated;import java.util.*;

public class MapBooOLDBuilder_TerrainTouchListener extends TerrainTouchListener
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
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: return _mapBooBuilder->onTerrainTouch(ec, pixel, camera, position, tile);
	return _mapBooBuilder.onTerrainTouch(ec, new Vector2F(pixel), camera, new Geodetic3D(position), tile);
  }
}
