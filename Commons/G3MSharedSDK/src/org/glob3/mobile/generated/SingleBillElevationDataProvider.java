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



//class IDownloader;
//class Vector2I;

public class SingleBillElevationDataProvider extends ElevationDataProvider
{
  private IDownloader _downloader;

  private ElevationData _elevationData;
  private boolean _elevationDataResolved;
  private final URL _bilUrl = new URL();
  private final Sector _sector ;
  private final int _resolutionWidth;
  private final int _resolutionHeight;


  private void drainQueue()
  {
    int _WORKING;
  }

  private long queueRequest(Sector sector, Vector2I resolution, IElevationDataListener listener, boolean autodeleteListener)
  {
    int _WORKING;
    return -1;
  }

  private void removeQueueRequest(long requestId)
  {
    int _WORKING;
  }


  public SingleBillElevationDataProvider(URL bilUrl, Sector sector, Vector2I resolution)
  {
     _bilUrl = new URL(bilUrl);
     _sector = new Sector(sector);
     _resolutionWidth = resolution._x;
     _resolutionHeight = resolution._y;
     _downloader = null;
     _elevationData = null;
     _elevationDataResolved = false;
  
  }

  public final boolean isReadyToRender(G3MRenderContext rc)
  {
    return (_elevationDataResolved);
  }

  public final void initialize(G3MContext context)
  {
    if (!_elevationDataResolved)
    {
      context.getDownloader().requestBuffer(_bilUrl, 2000000000, TimeInterval.fromDays(30), new SingleBillElevationDataProvider_BufferDownloadListener(this, _sector, _resolutionWidth, _resolutionHeight), true);
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
      ElevationData elevationData = new SubviewElevationData(_elevationData, false, sector, resolution);
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

}