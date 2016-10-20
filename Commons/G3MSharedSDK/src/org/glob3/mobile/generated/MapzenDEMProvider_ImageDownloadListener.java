package org.glob3.mobile.generated; 
public class MapzenDEMProvider_ImageDownloadListener extends IImageDownloadListener
{
  private final G3MContext _context;
  private MapzenDEMProvider _provider;
  private final int _z;
  private final int _x;
  private final int _y;
  private final Sector _sector ;
  private final double _deltaHeight;

  public MapzenDEMProvider_ImageDownloadListener(G3MContext context, MapzenDEMProvider provider, int z, int x, int y, Sector sector, double deltaHeight)
  {
     _context = context;
     _provider = provider;
     _z = z;
     _x = x;
     _y = y;
     _sector = new Sector(sector);
     _deltaHeight = deltaHeight;
    _provider._retain();
  }

  public void dispose()
  {
    _provider._release();
    super.dispose();
  }

  public final void onDownload(URL url, IImage image, boolean expired)
  {
    MapzenTerrariumParser.parse(_context, image, _sector, _deltaHeight, new MapzenDEMProvider_ParserListener(_provider, _z, _x, _y), true);

    // synchronous
    // FloatBufferDEMGrid* grid = MapzenTerrariumParser::parse(image, _sector, _deltaHeight);
    // _provider->onGrid(_z, _x, _y,
    //                   grid);
  }

  public final void onError(URL url)
  {
    _provider.onDownloadError(_z, _x, _y);
  }

  public final void onCancel(URL url)
  {
    // do nothing
  }

  public final void onCanceledDownload(URL url, IImage image, boolean expired)
  {
    // do nothing
  }

}