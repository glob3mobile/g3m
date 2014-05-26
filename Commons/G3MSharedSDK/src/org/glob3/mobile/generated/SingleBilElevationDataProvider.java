package org.glob3.mobile.generated; 
public class SingleBilElevationDataProvider extends ElevationDataProvider
{


  private long _currentRequestID;
  private java.util.HashMap<Long, SingleBilElevationDataProvider_Request> _requestsQueue = new java.util.HashMap<Long, SingleBilElevationDataProvider_Request>();


  private ElevationData _elevationData;
  private boolean _elevationDataResolved;
  private final URL _bilUrl;
  private final Sector _sector ;
  private final int _extentWidth;
  private final int _extentHeight;

  private final double _deltaHeight;

  private void drainQueue()
  {
    if (!_elevationDataResolved)
    {
      ILogger.instance().logError("Trying to drain queue of requests without data.");
      return;
    }
  
    for (final Long key : _requestsQueue.keySet()) {
      final SingleBilElevationDataProvider_Request r = _requestsQueue.get(key);
      requestElevationData(r._sector, r._extent, r._listener, r._autodeleteListener);
      if (r != null) {
        r.dispose();
      }
    }
    _requestsQueue.clear();
  }

  private long queueRequest(Sector sector, Vector2I extent, IElevationDataListener listener, boolean autodeleteListener)
  {
    _currentRequestID++;
    _requestsQueue.put(_currentRequestID, new SingleBilElevationDataProvider_Request(sector, extent, listener, autodeleteListener));
    return _currentRequestID;
  }

  private void removeQueueRequest(long requestId)
  {
    _requestsQueue.remove(requestId);
  }

  private IDownloader _downloader;
  private long _requestToDownloaderID;
  private SingleBilElevationDataProvider_BufferDownloadListener _listener;

  public SingleBilElevationDataProvider(URL bilUrl, Sector sector, Vector2I extent)
  {
     this(bilUrl, sector, extent, 0);
  }
  public SingleBilElevationDataProvider(URL bilUrl, Sector sector, Vector2I extent, double deltaHeight)
  {
     _bilUrl = bilUrl;
     _sector = new Sector(sector);
     _extentWidth = extent._x;
     _extentHeight = extent._y;
     _deltaHeight = deltaHeight;
     _elevationData = null;
     _elevationDataResolved = false;
     _currentRequestID = 0;
     _downloader = null;
     _requestToDownloaderID = -1;
     _listener = null;
  
  }

  public void dispose()
  {
    if (_elevationData != null)
       _elevationData.dispose();
  
    if (_downloader != null && _requestToDownloaderID > -1)
    {
      _downloader.cancelRequest(_requestToDownloaderID);
    }
  
    if (_listener != null)
    {
      _listener.notifyProviderHasBeenDeleted();
      _listener = null;
    }
  }

  public final boolean isReadyToRender(G3MRenderContext rc)
  {
    return (_elevationDataResolved);
  }

  public final void initialize(G3MContext context)
  {
    if (!_elevationDataResolved || _listener != null)
    {
      _downloader = context.getDownloader();
  
      _listener = new SingleBilElevationDataProvider_BufferDownloadListener(this, _sector, _extentWidth, _extentHeight, _deltaHeight);
  
      _requestToDownloaderID = _downloader.requestBuffer(_bilUrl, 2000000000, TimeInterval.fromDays(30), true, _listener, true);
    }
  }

  public final long requestElevationData(Sector sector, Vector2I extent, IElevationDataListener listener, boolean autodeleteListener)
  {
    if (!_elevationDataResolved)
    {
      return queueRequest(sector, extent, listener, autodeleteListener);
    }
  
    if (_elevationData == null)
    {
      listener.onError(sector, extent);
    }
    else
    {
      //int _DGD_working_on_terrain;
      ElevationData elevationData = new InterpolatedSubviewElevationData(_elevationData, sector, extent);
      listener.onData(sector, extent, elevationData);
    }
  
    if (autodeleteListener)
    {
      if (listener != null)
         listener.dispose();
    }
  
    return -1;
  }

  public final void cancelRequest(long requestId)
  {
    if (requestId >= 0)
    {
      removeQueueRequest(requestId);
    }
  }


  public final void onElevationData(ElevationData elevationData)
  {
    _elevationData = elevationData;
    _elevationDataResolved = true;
    if (_elevationData == null)
    {
      ILogger.instance().logError("Can't download Elevation-Data from %s", _bilUrl._path);
    }
  
    drainQueue();
  
    _listener = null; //The listener will be autodeleted
  }

  public final java.util.ArrayList<Sector> getSectors()
  {
    final java.util.ArrayList<Sector> sectors = new java.util.ArrayList<Sector>();
    sectors.add(_sector);
    return sectors;
  }

  public final Vector2I getMinResolution()
  {
    return new Vector2I(_extentWidth, _extentHeight);
  }

}