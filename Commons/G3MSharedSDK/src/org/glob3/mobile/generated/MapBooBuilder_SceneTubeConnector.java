package org.glob3.mobile.generated; 
public class MapBooBuilder_SceneTubeConnector extends GInitializationTask
{
  private MapBooBuilder _builder;

  public MapBooBuilder_SceneTubeConnector(MapBooBuilder builder)
  {
     _builder = builder;
  }

  public final void run(G3MContext context)
  {
    _builder.openSceneTube(context);
  }

  public final boolean isDone(G3MContext context)
  {
    return true;
  }
}