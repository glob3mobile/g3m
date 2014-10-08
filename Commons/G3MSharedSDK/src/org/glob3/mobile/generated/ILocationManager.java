package org.glob3.mobile.generated; 
public abstract class ILocationManager
{



//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#warning with this converter add static
//  std::vector<const LocationChangedListener*>* _listeners = new std::vector<const LocationChangedListener*>();


    protected final java.util.ArrayList<LocationChangedListener> _listeners = java.util.<LocationChangedListener>ArrayList();



  public void dispose()
  {
    final int size = _listeners.size();
    for (int i = 0; i < size; i++)
    {
      _listeners.at(i) = null;
    }
  }

  public abstract String getProvider();

  public abstract boolean serviceIsEneabled();

  public abstract boolean isAuthorized();

  public abstract void start(long minTime, double minDistance, Activity_Type activityType);

  public abstract void stop();

  public abstract Geodetic2D getLocation();

  public final void notifyLocationChanged(Geodetic2D newLocation)
  {
    final int size = _listeners.size();
    for (int i = 0; i < size; i++)
    {
      _listeners.at(i).onLocationChanged(newLocation);
    }
  }

  public final void addLocationChangedListener(LocationChangedListener locationChangedListener)
  {
    _listeners.push_back(locationChangedListener);
  }


}