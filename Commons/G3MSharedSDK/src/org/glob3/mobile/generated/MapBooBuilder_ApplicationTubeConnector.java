package org.glob3.mobile.generated; 
public class MapBooBuilder_ApplicationTubeConnector extends GInitializationTask
{
  private MapBooBuilder _builder;

  public MapBooBuilder_ApplicationTubeConnector(MapBooBuilder builder)
  {
     _builder = builder;
  }

  public final void run(G3MContext context)
  {
    _builder.setContext(context);
    _builder.openApplicationTube(context);
  }

  public final boolean isDone(G3MContext context)
  {
    return true;
  }
}