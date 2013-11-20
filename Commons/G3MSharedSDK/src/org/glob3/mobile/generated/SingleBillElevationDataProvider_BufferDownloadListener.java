package org.glob3.mobile.generated; 
public class SingleBillElevationDataProvider_BufferDownloadListener extends IBufferDownloadListener
{
  private SingleBillElevationDataProvider _singleBillElevationDataProvider;
  private final Sector _sector ;
  private final int _resolutionWidth;
  private final int _resolutionHeight;

  private final double _deltaHeight;

  public SingleBillElevationDataProvider_BufferDownloadListener(SingleBillElevationDataProvider singleBillElevationDataProvider, Sector sector, int resolutionWidth, int resolutionHeight, double deltaHeight)
  {
     _singleBillElevationDataProvider = singleBillElevationDataProvider;
     _sector = new Sector(sector);
     _resolutionWidth = resolutionWidth;
     _resolutionHeight = resolutionHeight;
     _deltaHeight = deltaHeight;

  }

  public final void notifyProviderHasBeenDeleted()
  {
    _singleBillElevationDataProvider = null;
  }

  public final void onDownload(URL url, IByteBuffer buffer, boolean expired)
  {
    final Vector2I resolution = new Vector2I(_resolutionWidth, _resolutionHeight);

    ShortBufferElevationData elevationData = BilParser.parseBil16(_sector, resolution, buffer, _deltaHeight);

    if (buffer != null)
       buffer.dispose();

    if (_singleBillElevationDataProvider != null)
    {
      _singleBillElevationDataProvider.onElevationData(elevationData);
    }
  }

  public final void onError(URL url)
  {
    if (_singleBillElevationDataProvider != null)
    {
      _singleBillElevationDataProvider.onElevationData(null);
    }
  }

  public final void onCancel(URL url)
  {
    ILogger.instance().logInfo("SingleBillElevationDataProvider download petition was canceled.");
    if (_singleBillElevationDataProvider != null)
    {
      _singleBillElevationDataProvider.onElevationData(null);
    }
  }

  public final void onCanceledDownload(URL url, IByteBuffer data, boolean expired)
  {
  }
}