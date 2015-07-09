package org.glob3.mobile.generated; 
public class NASAElevationDataProvider extends ElevationDataProvider
{

  private WMSBilElevationDataProvider _provider;

  public NASAElevationDataProvider()
  {
    _provider = new WMSBilElevationDataProvider(new URL("http://data.worldwind.arc.nasa.gov/elev"), "srtm30", Sector.FULL_SPHERE, 0);
  }

  public final boolean isReadyToRender(G3MRenderContext rc)
  {
    return _provider.isReadyToRender(rc);
  }

  public final void initialize(G3MContext context)
  {
    _provider.initialize(context);
  }

  public long requestElevationData(Sector sector, Vector2I extent, IElevationDataListener listener, boolean autodeleteListener)
  {
  
    NASAElevationDataProviderListener list = new NASAElevationDataProviderListener(listener, autodeleteListener, sector, extent);
  
    int factor = 4;
  
    Sector sector2 = sector.shrinkedByPercent(-factor);
    Vector2I extent2 = new Vector2I(extent._x * factor, extent._y * factor);
  
    return _provider.requestElevationData(sector2, extent2, list, true);
  
    //return _provider->requestElevationData(sector, extent, listener, autodeleteListener);
  
  }

  public void cancelRequest(long requestId)
  {
    _provider.cancelRequest(requestId);
  }

  public java.util.ArrayList<Sector> getSectors()
  {
    return _provider.getSectors();
  }

  public Vector2I getMinResolution()
  {
    return _provider.getMinResolution();
  }

}