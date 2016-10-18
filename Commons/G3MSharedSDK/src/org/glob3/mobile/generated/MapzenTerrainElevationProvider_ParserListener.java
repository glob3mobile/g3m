package org.glob3.mobile.generated; 
public class MapzenTerrainElevationProvider_ParserListener extends MapzenTerrariumParser.Listener
{
  private MapzenTerrainElevationProvider _provider;
  private final int _z;
  private final int _x;
  private final int _y;


  public MapzenTerrainElevationProvider_ParserListener(MapzenTerrainElevationProvider provider, int z, int x, int y)
  {
     _provider = provider;
     _z = z;
     _x = x;
     _y = y;
    _provider._retain();
  }

  public void dispose()
  {
    _provider._release();
    super.dispose();
  }

  public final void onGrid(FloatBufferTerrainElevationGrid grid)
  {
    _provider.onGrid(_z, _x, _y, grid);
  }

}