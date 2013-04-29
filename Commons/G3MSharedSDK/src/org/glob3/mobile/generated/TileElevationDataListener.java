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




public class TileElevationDataListener implements IElevationDataListener
{
  private Tile _tile;
  private long _requestID;
  private Vector2I _resolution = new Vector2I();
  private ElevationDataProvider _provider;
  private boolean _isFinished;
  private boolean _deletingWhenFinished;

  public TileElevationDataListener(Tile tile, Vector2I resolution, ElevationDataProvider provider)
  {
     _tile = tile;
     _resolution = new Vector2I(resolution);
     _provider = provider;
     _requestID = -1;
     _isFinished = false;
     _deletingWhenFinished = false;

  }

  public void dispose()
  {
  }

  public final void onData(Sector sector, Vector2I resolution, ElevationData elevationData)
  {

    if (_tile != null)
    {
      _tile.setElevationData(elevationData, _tile.getLevel());
    }

    _isFinished = true;

    if (_deletingWhenFinished)
    {
      this = null;
    }
  }

  public final void onError(Sector sector, Vector2I resolution)
  {
    _isFinished = true;
    if (_deletingWhenFinished)
    {
      this = null;
    }
  }

  public final void sendRequest()
  {
    _requestID = _provider.requestElevationData(_tile.getSector(), _resolution, this, false);
  }

  public final void cancelRequest()
  {
    _tile = null;
    if (_requestID != -1 && !_isFinished)
    {
      _provider.cancelRequest(_requestID);
    }
    _deletingWhenFinished = true;
    if (_isFinished)
    {
      this = null;
    }
  }
}