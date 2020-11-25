package org.glob3.mobile.generated;
public class SingleBILElevationDataProvider_BufferDownloadListener extends IBufferDownloadListener
{
  private SingleBILElevationDataProvider _singleBILElevationDataProvider;
  private final Sector _sector ;
  private final int _resolutionWidth;
  private final int _resolutionHeight;

  private final double _deltaHeight;

  public SingleBILElevationDataProvider_BufferDownloadListener(SingleBILElevationDataProvider singleBILElevationDataProvider, Sector sector, int resolutionWidth, int resolutionHeight, double deltaHeight)
  {
     _singleBILElevationDataProvider = singleBILElevationDataProvider;
     _sector = new Sector(sector);
     _resolutionWidth = resolutionWidth;
     _resolutionHeight = resolutionHeight;
     _deltaHeight = deltaHeight;

  }

  public final void notifyProviderHasBeenDeleted()
  {
    _singleBILElevationDataProvider = null;
  }

  public final void onDownload(URL url, IByteBuffer buffer, boolean expired)
  {
    if (_singleBILElevationDataProvider != null)
    {
      ShortBufferElevationData elevationData = BILParser.oldParseBIL16(_sector, new Vector2I(_resolutionWidth, _resolutionHeight), buffer, _deltaHeight);

      _singleBILElevationDataProvider.onElevationData(elevationData);
    }
    if (buffer != null)
       buffer.dispose();
  }

  public final void onError(URL url)
  {
    if (_singleBILElevationDataProvider != null)
    {
      _singleBILElevationDataProvider.onElevationData(null);
    }
  }

  public final void onCancel(URL url)
  {
    ILogger.instance().logInfo("SingleBILElevationDataProvider download petition was canceled.");
    if (_singleBILElevationDataProvider != null)
    {
      _singleBILElevationDataProvider.onElevationData(null);
    }
  }

  public final void onCanceledDownload(URL url, IByteBuffer data, boolean expired)
  {
  }
}