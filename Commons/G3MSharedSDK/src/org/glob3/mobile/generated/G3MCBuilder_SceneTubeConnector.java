package org.glob3.mobile.generated; 
public class G3MCBuilder_SceneTubeConnector extends GInitializationTask
{
  private G3MCBuilder _builder;

  public G3MCBuilder_SceneTubeConnector(G3MCBuilder builder)
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