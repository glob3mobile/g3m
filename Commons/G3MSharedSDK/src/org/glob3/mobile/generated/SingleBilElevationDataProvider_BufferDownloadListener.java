package org.glob3.mobile.generated; 
public class SingleBilElevationDataProvider_BufferDownloadListener extends IBufferDownloadListener
{
  private SingleBilElevationDataProvider _singleBillElevationDataProvider;
  private final Sector _sector ;
  private final int _resolutionWidth;
  private final int _resolutionHeight;

  private final double _deltaHeight;

  public SingleBilElevationDataProvider_BufferDownloadListener(SingleBilElevationDataProvider singleBillElevationDataProvider, Sector sector, int resolutionWidth, int resolutionHeight, double deltaHeight)
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
    if (_singleBillElevationDataProvider != null)
    {
      ShortBufferElevationData elevationData = BilParser.parseBil16(_sector, new Vector2I(_resolutionWidth, _resolutionHeight), buffer, _deltaHeight);

      _singleBillElevationDataProvider.onElevationData(elevationData);
    }
    if (buffer != null)
       buffer.dispose();
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
    ILogger.instance().logInfo("SingleBilElevationDataProvider download petition was canceled.");
    if (_singleBillElevationDataProvider != null)
    {
      _singleBillElevationDataProvider.onElevationData(null);
    }
  }

  public final void onCanceledDownload(URL url, IByteBuffer data, boolean expired)
  {
  }
}