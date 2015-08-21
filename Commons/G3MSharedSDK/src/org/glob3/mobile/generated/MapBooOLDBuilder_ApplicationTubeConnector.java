package org.glob3.mobile.generated; 
public class MapBooOLDBuilder_ApplicationTubeConnector extends GInitializationTask
{
  private MapBooOLDBuilder _builder;

  public MapBooOLDBuilder_ApplicationTubeConnector(MapBooOLDBuilder builder)
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
    return _builder.hasParsedApplication();
  }
}