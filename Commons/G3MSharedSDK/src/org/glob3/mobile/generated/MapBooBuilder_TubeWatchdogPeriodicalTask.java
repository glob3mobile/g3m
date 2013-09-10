package org.glob3.mobile.generated; 
public class MapBooBuilder_TubeWatchdogPeriodicalTask extends GTask
{
  private MapBooBuilder _builder;
  private boolean _firstRun;

  public MapBooBuilder_TubeWatchdogPeriodicalTask(MapBooBuilder builder)
  {
     _builder = builder;
     _firstRun = true;
  }

  public final void run(G3MContext context)
  {
    if (_firstRun)
    {
      _firstRun = false;
    }
    else
    {
      if (!_builder.isApplicationTubeOpen())
      {
        _builder.openApplicationTube(context);
      }
    }
  }

}