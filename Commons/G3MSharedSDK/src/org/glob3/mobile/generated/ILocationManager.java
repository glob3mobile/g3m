package org.glob3.mobile.generated; 
public abstract class ILocationManager
{


  protected static final java.util.ArrayList<LocationChangedListener> _listeners = new java.util.ArrayList<LocationChangedListener>();


///#ifdef C_CODE
//  std::vector<const LocationChangedListener*>* _listeners = new std::vector<const LocationChangedListener*>();
///#else
//  protected final java.util.ArrayList<LocationChangedListener> _listeners = java.util.ArrayList<LocationChangedListener>();
///#endif



  public void dispose()
  {
    final int size = _listeners.size();
    for (int i = 0; i < size; i++)
    {
      if (_listeners.get(i) != null)
         _listeners.get(i).dispose();
    }
  }

  public abstract String getProvider();

  public abstract boolean serviceIsEneabled();

  public abstract boolean isAuthorized();

  public abstract void start(long minTime, double minDistance, Activity_Type activityType);

  public abstract void stop();

  public abstract Geodetic2D getLocation();

  public final void addLocationChangedListener(LocationChangedListener locationChangedListener)
  {
    _listeners.add(locationChangedListener);
  }


}