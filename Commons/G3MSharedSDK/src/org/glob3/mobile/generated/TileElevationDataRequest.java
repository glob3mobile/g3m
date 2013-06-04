package org.glob3.mobile.generated; 
//
//  TileElevationDataListener.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 23/04/13.
//
//

//
//  TileElevationDataListener.h
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 23/04/13.
//
//




public class TileElevationDataRequest
{
  private Tile _tile;
  private long _requestID;
  private Vector2I _resolution = new Vector2I();
  private ElevationDataProvider _provider;


  private static class TileElevationDataRequestListener implements IElevationDataListener
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

  }

  private TileElevationDataRequestListener _listener;

  public TileElevationDataRequest(Tile tile, Vector2I resolution, ElevationDataProvider provider)
  {
     _tile = tile;
     _resolution = new Vector2I(resolution);
     _provider = provider;
     _requestID = -1;
     _listener = null;
  
  }

  public void dispose()
  {
  }

  public final void onData(Sector sector, Vector2I resolution, ElevationData elevationData)
  {
    _listener = null;
    if (_tile != null)
    {
      _tile.setElevationData(elevationData, _tile.getLevel());
    }
  }

  public final void onError(Sector sector, Vector2I resolution)
  {
    _listener = null;
  }

  public final void onCancel(Sector sector, Vector2I resolution)
  {
    _listener = null;
  }

  public final void sendRequest()
  {
    _listener = new TileElevationDataRequestListener(this);
    _requestID = _provider.requestElevationData(_tile.getSector(), _resolution, _listener, true);
  }

  public final void cancelRequest()
  {
    if (_listener != null)
    {
      _listener._request = null;
      _provider.cancelRequest(_requestID);
    }
  }

}
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#pragma mark TileElevationDataRequest

//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#pragma mark TileElevationDataRequestListener