package org.glob3.mobile.generated;
public class MapzenDEMProvider_ParserListener extends MapzenTerrariumParser.Listener
{
  private MapzenDEMProvider _provider;
  private final int _z;
  private final int _x;
  private final int _y;

  public MapzenDEMProvider_ParserListener(MapzenDEMProvider provider, int z, int x, int y)
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

  public final void onGrid(FloatBufferDEMGrid grid)
  {
    _provider.onGrid(_z, _x, _y, grid);
  }
}
