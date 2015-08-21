package org.glob3.mobile.generated; 
public class MapBooOLDBuilder_TubeWatchdogPeriodicalTask extends GTask
{
  private MapBooOLDBuilder _builder;
  private boolean _firstRun;

  public MapBooOLDBuilder_TubeWatchdogPeriodicalTask(MapBooOLDBuilder builder)
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
        _builder.pollApplicationDataFromServer(context);

        _builder.openApplicationTube(context);
      }
    }
  }

}