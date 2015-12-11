package org.glob3.mobile.generated; 
public abstract class ElevationDataProvider
{

  protected ChangedListener _changedListener;

  protected boolean _enabled;


  public ElevationDataProvider()
  {
     _changedListener = null;
     _enabled = true;
  }

  public void dispose()
  {

  }

  public abstract boolean isReadyToRender(G3MRenderContext rc);

  public abstract void initialize(G3MContext context);

  public abstract long requestElevationData(Sector sector, Vector2I extent, long requestPriority, IElevationDataListener listener, boolean autodeleteListener);

  public long requestElevationData(Sector sector, int level, int row, int column, Vector2I extent, long requestPriority, IElevationDataListener listener, boolean autodeleteListener){
	  int warning_method_madeby_chano__default_executes_normal_one__override_this_to_get_a_LRC_based_request;
	  return requestElevationData(sector,extent,requestPriority,listener,autodeleteListener);
  }
  
  public abstract void cancelRequest(long requestId);

  public abstract java.util.ArrayList<Sector> getSectors();

  public final boolean containsSector(Sector sector)
  {
  
    final java.util.ArrayList<Sector> ss = getSectors();
    for (int i = 0; i < ss.size(); i++)
    {
      if (ss.get(i).touchesWith(sector))
      {
        return true;
      }
    }
    return false;
  
  }

  public abstract Vector2I getMinResolution();

  public final void setChangedListener(ChangedListener changedListener)
  {
    _changedListener = changedListener;
  }

  public final void onChanged()
  {
    if (_changedListener != null)
    {
      _changedListener.changed();
    }
  }

  public final void setEnabled(boolean enabled)
  {
    if (_enabled != enabled)
    {
      _enabled = enabled;
      onChanged();
    }
  }

  public final boolean isEnabled()
  {
    return _enabled;
  }

}