package org.glob3.mobile.generated; 
public class WMSBillElevationDataProvider_BufferDownloadListener extends IBufferDownloadListener
{
  private final Sector _sector ;
  private final Vector2I _resolution = new Vector2I();
  private IElevationDataListener _listener;
  private final boolean _autodeleteListener;




  public WMSBillElevationDataProvider_BufferDownloadListener(Sector sector, Vector2I resolution, IElevationDataListener listener, boolean autodeleteListener)
  {
     _sector = new Sector(sector);
     _resolution = new Vector2I(resolution);
     _listener = listener;
     _autodeleteListener = autodeleteListener;

  }

  public final void onDownload(URL url, IByteBuffer buffer)
  {
    ElevationData elevationData = BilParser.parseBil16(buffer, _resolution);
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

  public final void onCanceledDownload(URL url, IByteBuffer data)
  {

  }


}