package org.glob3.mobile.generated; 
public class VisibleSectorListenerEntry
{
  private VisibleSectorListener _listener;
  private final long _stabilizationIntervalInMS;

  private Sector _lastSector;
  private long _whenNotifyInMS;

  private ITimer _timer;

  private ITimer getTimer()
  {
    if (_timer == null)
    {
      _timer = IFactory.instance().createTimer();
    }
    return _timer;
  }


  public VisibleSectorListenerEntry(VisibleSectorListener listener, TimeInterval stabilizationInterval)
  {
     _listener = listener;
     _stabilizationIntervalInMS = stabilizationInterval._milliseconds;
     _lastSector = null;
     _timer = null;
     _whenNotifyInMS = 0;

  }

  public final void notifyListener(Sector visibleSector, G3MRenderContext rc)
  {
    _listener.onVisibleSectorChange(_lastSector, rc.getCurrentCamera().getGeodeticPosition());
  }

  public final void tryToNotifyListener(Sector visibleSector, G3MRenderContext rc)
  {
    if (_stabilizationIntervalInMS == 0)
    {
      if ((_lastSector == null) || (!_lastSector.isEquals(visibleSector)))
      {
        if (_lastSector != null)
           _lastSector.dispose();
        _lastSector = new Sector(visibleSector);

        notifyListener(visibleSector, rc);
      }
    }
    else
    {
      final long now = getTimer().now()._milliseconds;

      if ((_lastSector == null) || (!_lastSector.isEquals(visibleSector)))
      {
        if (_lastSector != null)
           _lastSector.dispose();
        _lastSector = new Sector(visibleSector);
        _whenNotifyInMS = now + _stabilizationIntervalInMS;
      }

      if (_whenNotifyInMS != 0)
      {
        if (now >= _whenNotifyInMS)
        {
          notifyListener(visibleSector, rc);

          _whenNotifyInMS = 0;
        }
      }
    }

  }

  public void dispose()
  {
    if (_listener != null)
       _listener.dispose();
    if (_timer != null)
       _timer.dispose();
    if (_lastSector != null)
       _lastSector.dispose();
  }
}