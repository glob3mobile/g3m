package org.glob3.mobile.generated; 
//
//  SingleBillElevationDataProvider.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/21/13.
//
//

//
//  SingleBillElevationDataProvider.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/21/13.
//
//



//class Vector2I;

public class SingleBillElevationDataProvider extends ElevationDataProvider
{

  private static class SingleBillElevationDataProvider_Request
  {
    public final Sector _sector ;
    public final Vector2I _resolution;
    public final IElevationDataListener _listener;
    public final boolean _autodeleteListener;

    public SingleBillElevationDataProvider_Request(Sector sector, Vector2I resolution, IElevationDataListener listener, boolean autodeleteListener)
    {
       _sector = new Sector(sector);
       _resolution = new Vector2I(resolution);
       _listener = listener;
       _autodeleteListener = autodeleteListener;
    }
  }

  private long _currentRequestID;
  private java.util.HashMap<Long, SingleBillElevationDataProvider_Request> _requests = new java.util.HashMap<Long, SingleBillElevationDataProvider_Request>();


  private ElevationData _elevationData;
  private boolean _elevationDataResolved;
  private final URL _bilUrl;
  private final Sector _sector ;
  private final int _resolutionWidth;
  private final int _resolutionHeight;
  private final double _noDataValue;

  private void drainQueue()
  {
    if (!_elevationDataResolved)
    {
      ILogger.instance().logError("Trying to drain queue of requests without data.");
      return;
    }
  
    java.util.Iterator<Long, SingleBillElevationDataProvider_Request> it = _requests.iterator();
    for (; it.hasNext();)
    {
      SingleBillElevationDataProvider_Request r = it.next().getValue();
      requestElevationData(r._sector, r._resolution, r._listener, r._autodeleteListener);
      if (r != null)
         r.dispose();
    }
    _requests.clear();
  }

  private long queueRequest(Sector sector, Vector2I resolution, IElevationDataListener listener, boolean autodeleteListener)
  {
    _currentRequestID++;
    _requests.put(_currentRequestID, new SingleBillElevationDataProvider_Request(sector, resolution, listener, autodeleteListener));
    return _currentRequestID;
  }

  private void removeQueueRequest(long requestId)
  {
  
    java.util.Iterator<Long, SingleBillElevationDataProvider_Request> it = _requests.indexOf(requestId);
    if (it.hasNext())
    {
      it.next().getValue() = null;
      _requests.remove(it);
    }
  }


  public SingleBillElevationDataProvider(URL bilUrl, Sector sector, Vector2I resolution, double noDataValue)
  {
     _bilUrl = bilUrl;
     _sector = new Sector(sector);
     _resolutionWidth = resolution._x;
     _resolutionHeight = resolution._y;
     _noDataValue = noDataValue;
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
      context.getDownloader().requestBuffer(_bilUrl, 2000000000, TimeInterval.fromDays(30), true, new SingleBillElevationDataProvider_BufferDownloadListener(this, _sector, _resolutionWidth, _resolutionHeight, _noDataValue), true);
    }
  }

  public final long requestElevationData(Sector sector, Vector2I resolution, IElevationDataListener listener, boolean autodeleteListener)
  {
    if (!_elevationDataResolved)
    {
      return queueRequest(sector, resolution, listener, autodeleteListener);
    }
  
    if (_elevationData == null)
    {
      listener.onError(sector, resolution);
    }
    else
    {
      int _DGD_working_on_terrain;
      final boolean useDecimation = false;
      ElevationData elevationData = new SubviewElevationData(_elevationData, false, sector, resolution, useDecimation);
      listener.onData(sector, resolution, elevationData);
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
    return new Vector2I(_resolutionWidth,_resolutionHeight);
  }

  public final ElevationData createSubviewOfElevationData(ElevationData elevationData, Sector sector, Vector2I resolution)
  {
    return new SubviewElevationData(elevationData, false, sector, resolution, false);
  }

}