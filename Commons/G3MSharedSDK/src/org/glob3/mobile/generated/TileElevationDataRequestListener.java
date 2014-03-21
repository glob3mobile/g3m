package org.glob3.mobile.generated; 
//
//  TileElevationDataListener.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 23/04/13.
//
//

//
//  TileElevationDataListener.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 23/04/13.
//
//




public class TileElevationDataRequestListener implements IElevationDataListener
{
  public TileElevationDataRequest _request;

  public TileElevationDataRequestListener(TileElevationDataRequest request)
  {
     _request = request;
  }

  public final void onData(Sector sector, Vector2I resolution, ElevationData elevationData)
  {
    if (_request != null)
    {
      _request.onData(sector, resolution, elevationData);
    }
  }

  public final void onError(Sector sector, Vector2I resolution)
  {
    if (_request != null)
    {
      _request.onError(sector, resolution);
    }
  }

  public final void onCancel(Sector sector, Vector2I resolution)
  {
    if (_request != null)
    {
      _request.onCancel(sector, resolution);
    }
  }

  public void dispose()
  {
  }

}