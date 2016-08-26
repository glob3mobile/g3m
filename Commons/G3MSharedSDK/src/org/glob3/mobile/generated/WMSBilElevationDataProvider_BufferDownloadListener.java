package org.glob3.mobile.generated; 
public class WMSBilElevationDataProvider_BufferDownloadListener extends IBufferDownloadListener
{
  private final Sector _sector ;
  private final Vector2I _resolution;

  private IElevationDataListener _listener;
  private final boolean _autodeleteListener;

  private final double _deltaHeight;


  public WMSBilElevationDataProvider_BufferDownloadListener(Sector sector, Vector2I resolution, IElevationDataListener listener, boolean autodeleteListener, double deltaHeight)
  {
     _sector = new Sector(sector);
     _resolution = resolution;
     _listener = listener;
     _autodeleteListener = autodeleteListener;
     _deltaHeight = deltaHeight;

  }

  public final void onDownload(URL url, IByteBuffer buffer, boolean expired)
  {
    ShortBufferElevationData elevationData = BilParser.parseBil16(_sector, _resolution, buffer, _deltaHeight);
    if (buffer != null)
       buffer.dispose();

    if (elevationData == null)
    {
      _listener.onError(_sector, _resolution);
    }
    else
    {
      _listener.onData(_sector, _resolution, elevationData);
    }

    if (_autodeleteListener)
    {
      if (_listener != null)
         _listener.dispose();
      _listener = null;
    }
  }

  public final void onError(URL url)
  {
    _listener.onError(_sector, _resolution);
    if (_autodeleteListener)
    {
      if (_listener != null)
         _listener.dispose();
      _listener = null;
    }
  }

  public final void onCancel(URL url)
  {

  }

  public final void onCanceledDownload(URL url, IByteBuffer data, boolean expired)
  {

  }


}