package org.glob3.mobile.generated; 
public abstract class ILocationManager
{



  protected java.util.ArrayList<LocationChangedListener> _listeners = new java.util.ArrayList<LocationChangedListener>();




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

  public final void notifyLocationChanged()
  {
    if (_listeners != null)
    {
      final int size = _listeners.size();
      for (int i = 0; i < size; i++)
      {
        _listeners.get(i).onLocationChanged(getLocation());
      }
    }
  }

  public final void addLocationChangedListener(LocationChangedListener locationChangedListener)
  {
    _listeners.add(locationChangedListener);
  }


}