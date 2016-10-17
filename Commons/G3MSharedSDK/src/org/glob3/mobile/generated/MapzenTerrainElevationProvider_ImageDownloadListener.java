package org.glob3.mobile.generated; 
public class MapzenTerrainElevationProvider_ImageDownloadListener extends IImageDownloadListener
{
  private MapzenTerrainElevationProvider _provider;
  private final Sector _sector ;
  private final double _deltaHeight;

  public MapzenTerrainElevationProvider_ImageDownloadListener(MapzenTerrainElevationProvider provider, Sector sector, double deltaHeight)
  {
     _provider = provider;
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

    MutableColor255 pixel = new MutableColor255((byte) 0, (byte) 0, (byte) 0, (byte) 0);

    final int width = image.getWidth();
    final int height = image.getHeight();

    final int bufferSize = width * height;

    float[] buffer = new float[bufferSize];

    for (int x = 0; x < width; x++)
    {
      for (int y = 0; y < height; y++)
      {
        image.getPixel(x, y, pixel);
        final float elevation = ((pixel._red * 256.0f) + pixel._green + (pixel._blue / 256.0f)) - 32768.0f;
        final int index = ((height-1-y) * width) + x;
        buffer[index] = elevation;
      }
    }

    if (image != null)
       image.dispose();

//    _provider->onGrid( new FloatBufferTerrainElevationGrid(_sector,
//                                                           Vector2I(width, height),
//                                                           buffer,
//                                                           bufferSize,
//                                                           _deltaHeight) );
  }

  public final void onError(URL url)
  {
//    _provider->onDownloadError();
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