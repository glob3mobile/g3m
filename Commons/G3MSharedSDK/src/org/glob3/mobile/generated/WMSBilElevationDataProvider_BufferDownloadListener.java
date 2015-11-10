package org.glob3.mobile.generated; 
public class WMSBilElevationDataProvider_BufferDownloadListener extends IBufferDownloadListener
{
  private final Sector _sector ;
  private final int _width;
  private final int _height;

  private IElevationDataListener _listener;
  private final boolean _autodeleteListener;

  private final double _deltaHeight;


  public WMSBilElevationDataProvider_BufferDownloadListener(Sector sector, Vector2I extent, IElevationDataListener listener, boolean autodeleteListener, double deltaHeight)
  {
     _sector = new Sector(sector);
     _width = extent._x;
     _height = extent._y;
     _listener = listener;
     _autodeleteListener = autodeleteListener;
     _deltaHeight = deltaHeight;

  }

//  static std::vector<std::string> _urls;

  public final void onDownload(URL url, IByteBuffer buffer, boolean expired)
  {
    final Vector2I resolution = new Vector2I(_width, _height);

    /*
     //DEBUGGING CODE
#warning at work
    if (_sector.contains(Geodetic2D::fromDegrees(28.271842, -16.642497))){
      _urls.push_back(url._path);
      
      printf("ARRIVED BILs\n");
      for (int i = 0; i < _urls.size(); i++) {
        printf("%s\n", _urls[i].c_str());
      }
    }
     */

    ShortBufferElevationData elevationData = BilParser.parseBil16(_sector, resolution, buffer, _deltaHeight);
    if (buffer != null)
       buffer.dispose();

    if (elevationData == null)
    {
      _listener.onError(_sector, resolution);
    }
    else
    {
      _listener.onData(_sector, resolution, elevationData);
      elevationData._release();
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
    final Vector2I resolution = new Vector2I(_width, _height);

    _listener.onError(_sector, resolution);
    if (_autodeleteListener)
    {
      if (_listener != null)
         _listener.dispose();
      _listener = null;
    }
  }

  public final void onCancel(URL url)
  {
    if (_listener != null)
    {
      final Vector2I resolution = new Vector2I(_width, _height);
      _listener.onCancel(_sector, resolution);
      if (_autodeleteListener)
      {
        if (_listener != null)
           _listener.dispose();
        _listener = null;
      }
    }
  }

  public final void onCanceledDownload(URL url, IByteBuffer data, boolean expired)
  {
    if (_autodeleteListener)
    {
      if (_listener != null)
         _listener.dispose();
      _listener = null;
    }
  }


}