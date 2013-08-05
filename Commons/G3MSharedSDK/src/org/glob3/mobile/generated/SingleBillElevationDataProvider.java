package org.glob3.mobile.generated; 
public class SingleBillElevationDataProvider extends ElevationDataProvider
{


  private long _currentRequestID;
  private java.util.HashMap<Long, SingleBillElevationDataProvider_Request> _requestsQueue = new java.util.HashMap<Long, SingleBillElevationDataProvider_Request>();


  private ElevationData _elevationData;
  private boolean _elevationDataResolved;
  private final URL _bilUrl;
  private final Sector _sector ;
  private final int _extentWidth;
  private final int _extentHeight;

  private void drainQueue()
  {
    if (!_elevationDataResolved)
    {
      ILogger.instance().logError("Trying to drain queue of requests without data.");
      return;
    }
  
    for (final Long key : _requestsQueue.keySet()) {
      final SingleBillElevationDataProvider_Request r = _requestsQueue.get(key);
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
    _requestsQueue.put(_currentRequestID, new SingleBillElevationDataProvider_Request(sector, extent, listener, autodeleteListener));
    return _currentRequestID;
  }

  private void removeQueueRequest(long requestId)
  {
    _requestsQueue.remove(requestId);
  }


  public SingleBillElevationDataProvider(URL bilUrl, Sector sector, Vector2I extent)
  {
     _bilUrl = bilUrl;
     _sector = new Sector(sector);
     _extentWidth = extent._x;
     _extentHeight = extent._y;
     _elevationData = null;
     _elevationDataResolved = false;
     _currentRequestID = 0;
  
  }

  public final boolean isReadyToRender(G3MRenderContext rc)
  {
    return (_elevationDataResolved);
  }

  public final void initialize(G3MContext context)
  {
    if (!_elevationDataResolved)
    {
      context.getDownloader().requestBuffer(_bilUrl, 2000000000, TimeInterval.fromDays(30), true, new SingleBillElevationDataProvider_BufferDownloadListener(this, _sector, _extentWidth, _extentHeight), true);
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
      ElevationData elevationData = new SubviewElevationData(_elevationData, sector, extent);
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
      ILogger.instance().logError("Can't download Elevation-Data from %s", _bilUrl.getPath());
    }
  
    drainQueue();
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

  //  ElevationData* createSubviewOfElevationData(ElevationData* elevationData,
  //                                              const Sector& sector,
  //                                              const Vector2I& extent};

}