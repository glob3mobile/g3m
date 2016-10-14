package org.glob3.mobile.generated; 
public class MapzenTerrainElevationProvider_ImageDownloadListener extends IImageDownloadListener
{
  private MapzenTerrainElevationProvider _mapzenTerrainElevationProvider;

  public MapzenTerrainElevationProvider_ImageDownloadListener(MapzenTerrainElevationProvider mapzenTerrainElevationProvider)
  {
     _mapzenTerrainElevationProvider = mapzenTerrainElevationProvider;
    _mapzenTerrainElevationProvider._retain();
  }

  public void dispose()
  {
    _mapzenTerrainElevationProvider._release();
    super.dispose();
  }

  public final void onDownload(URL url, IImage image, boolean expired)
  {
///#error Diego at work!
  }

  public final void onError(URL url)
  {
///#error Diego at work!
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