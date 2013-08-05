package org.glob3.mobile.generated; 
public class G3MCBuilder_TubeWatchdogPeriodicalTask extends GTask
{
  private G3MCBuilder _builder;

  public G3MCBuilder_TubeWatchdogPeriodicalTask(G3MCBuilder builder)
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