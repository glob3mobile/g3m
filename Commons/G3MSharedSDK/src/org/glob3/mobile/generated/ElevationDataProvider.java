package org.glob3.mobile.generated; 
public abstract class ElevationDataProvider
{

  public void dispose()
  {

  }

  public abstract void initialize(G3MContext context);

  public abstract long requestElevationData(Sector sector, Vector2I resolution, IElevationDataListener listener, boolean autodeleteListener);

  public abstract void cancelRequest(long requestId);

}