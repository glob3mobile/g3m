package org.glob3.mobile.generated; 
public class SingleBillElevationDataProvider_BufferDownloadListener extends IBufferDownloadListener
{
  private SingleBillElevationDataProvider _singleBillElevationDataProvider;
  private final Sector _sector ;
  private final int _resolutionWidth;
  private final int _resolutionHeight;
  private final double _noDataValue;

  public SingleBillElevationDataProvider_BufferDownloadListener(SingleBillElevationDataProvider singleBillElevationDataProvider, Sector sector, int resolutionWidth, int resolutionHeight, double noDataValue)
  {
     _singleBillElevationDataProvider = singleBillElevationDataProvider;
     _sector = new Sector(sector);
     _resolutionWidth = resolutionWidth;
     _resolutionHeight = resolutionHeight;
     _noDataValue = noDataValue;

  }

  public final void onDownload(URL url, IByteBuffer buffer)
  {
    final Vector2I resolution = new Vector2I(_resolutionWidth, _resolutionHeight);
    ElevationData elevationData = BilParser.parseBil16(_sector, resolution, _noDataValue, buffer);
    if (buffer != null)
       buffer.dispose();

    _singleBillElevationDataProvider.onElevationData(elevationData);
  }

  public final void onError(URL url)
  {
    _singleBillElevationDataProvider.onElevationData(null);
  }

  public final void onCancel(URL url)
  {

  }

  public final void onCanceledDownload(URL url, IByteBuffer data)
  {

  }
}