package org.glob3.mobile.generated; 
public class G3MCBuilder_WebSocketConnector extends GInitializationTask
{
  private G3MCBuilder _builder;

  public G3MCBuilder_WebSocketConnector(G3MCBuilder builder)
  {
     _builder = builder;
  }

  public final void run(G3MContext context)
  {
    final boolean autodeleteListener = true;
    final boolean autodeleteWebSocket = true;

    context.getFactory().createWebSocket(_builder.createTubeSceneURL(), new G3MCBuilder_TubeSceneListener(_builder), autodeleteListener, autodeleteWebSocket);
  }

  public final boolean isDone(G3MContext context)
  {
    return true;
  }
}