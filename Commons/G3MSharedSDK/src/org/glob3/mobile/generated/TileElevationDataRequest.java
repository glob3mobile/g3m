package org.glob3.mobile.generated; 
public class TileElevationDataRequest
{
  private Tile _tile;
  private long _requestID;
  private final Vector2I _resolution;
  private ElevationDataProvider _provider;

  private TileElevationDataRequestListener _listener;

  public TileElevationDataRequest(Tile tile, Vector2I resolution, ElevationDataProvider provider)
  {
     _tile = tile;
     _resolution = resolution;
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
      _tile.setElevationData(elevationData, _tile._level);
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
    _requestID = _provider.requestElevationData(_tile._sector, _resolution, _listener, true);
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
