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

  public final void sendRequest(G3MRenderContext rc, PlanetRenderContext prc)
  {
    _listener = new TileElevationDataRequestListener(this);
    _requestID = _provider.requestElevationData(_tile._sector, _resolution, _tile._level, _tile._row, _tile._column, _listener, true);
    if (_requestID < -1)
    {
      //A requestID lower than -1 is defined to represent a tile which won't have elevationData due to the pyramid being shorter than needed.
      //That case, we will try to get ElevData from ancestor and define it as the one needed in the level.
      long maxLevel = - (_requestID);
      Tile theLastAncestor = null;
      Tile theAncestor = _tile.getParent();
      while (theAncestor != null)
      {
        if (theAncestor._level == maxLevel)
        {
          theLastAncestor = theAncestor;
          break;
        }
        theAncestor = theAncestor.getParent();
      }
      if (theLastAncestor != null)
      {
        if (theLastAncestor.getElevationData() == null)
        {
          //Ensure lastAncestor to have an ElevData.
          theLastAncestor.initializeElevationData(rc, prc);
        }
        if (theLastAncestor.getElevationData() != null)
        {
          ElevationData subView = _tile.createElevationDataSubviewFromAncestor(theLastAncestor);
          _tile.setElevationData(subView, _tile._level);
        }
      }
      _requestID = -1;
    }
  }

  public final void cancelRequest()
  {
    if (_listener != null)
    {
      _listener._request = null;
      if (_requestID > -1)
      {
        _provider.cancelRequest(_requestID);
      }
    }
  }

}
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#pragma mark TileElevationDataRequest

//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#pragma mark TileElevationDataRequestListener
