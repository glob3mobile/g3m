package org.glob3.mobile.generated; 
public class MapBooBuilder_TubeWatchdogPeriodicalTask extends GTask
{
  private MapBooBuilder _builder;

  public MapBooBuilder_TubeWatchdogPeriodicalTask(MapBooBuilder builder)
  {
     _builder = builder;
  }

  public final void run(G3MContext context)
  {
    if (!_builder.isSceneTubeOpen())
    {
      _builder.openSceneTube(context);
    }
  }

}