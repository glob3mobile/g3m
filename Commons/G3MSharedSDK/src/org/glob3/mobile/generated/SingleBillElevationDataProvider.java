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
  private ElevationData _elevationData;
  private boolean _elevationDataResolved;
  private final URL _bilUrl;
  private final Sector _sector ;
  private final int _resolutionWidth;
  private final int _resolutionHeight;
  private final double _noDataValue;


  private void drainQueue()
  {
    int _DGD_working_on_terrain;
  }

  private long queueRequest(Sector sector, Vector2I resolution, IElevationDataListener listener, boolean autodeleteListener)
  {
    int _DGD_working_on_terrain;
    return -1;
  }

  private void removeQueueRequest(long requestId)
  {
    int _DGD_working_on_terrain;
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
      ElevationData elevationData = new SubviewElevationData(_elevationData, false, sector, resolution, _noDataValue, useDecimation);
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