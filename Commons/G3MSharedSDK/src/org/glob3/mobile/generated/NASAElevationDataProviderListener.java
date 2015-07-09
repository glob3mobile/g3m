package org.glob3.mobile.generated; 
//
//  NASAElevationDataProvider.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 7/7/15.
//
//

//
//  NASAElevationDataProvider.h
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 7/7/15.
//
//



public class NASAElevationDataProviderListener implements IElevationDataListener
{
  private IElevationDataListener _listener;
  private Sector _requestedSector ;
  private Vector2I _requestedExtent = new Vector2I();
  private boolean _autoDelete;

  public NASAElevationDataProviderListener(IElevationDataListener listener, boolean autodelete, Sector requestedSector, Vector2I requestedExtent)
  {
     _listener = listener;
     _autoDelete = autodelete;
     _requestedSector = new Sector(requestedSector);
     _requestedExtent = new Vector2I(requestedExtent);

  }

  public void dispose()
  {
    if (_autoDelete)
    {
      if (_listener != null)
         _listener.dispose();
      _listener = null;
    }
  }

  public void onData(Sector sector, Vector2I extent, ElevationData elevationData)
  {
  
    ElevationData sub = new InterpolatedSubviewElevationData(elevationData, _requestedSector, _requestedExtent);
  
    _listener.onData(sector, _requestedExtent, sub);
    sub._release();
  
    if (_autoDelete)
    {
      if (_listener != null)
         _listener.dispose();
      _listener = null;
    }
  }

  public void onError(Sector sector, Vector2I extent)
  {
  
    _listener.onError(sector, _requestedExtent);
    if (_autoDelete)
    {
      if (_listener != null)
         _listener.dispose();
      _listener = null;
    }
  }

  public void onCancel(Sector sector, Vector2I extent)
  {
  
    _listener.onCancel(sector, _requestedExtent);
    if (_autoDelete)
    {
      if (_listener != null)
         _listener.dispose();
      _listener = null;
    }
  
  }
}