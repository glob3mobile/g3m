package org.glob3.mobile.generated; 
public class G3MCBuilder_ChangeSceneIdTask extends GTask
{
  private G3MCBuilder _builder;
  private final String _sceneId;

  public G3MCBuilder_ChangeSceneIdTask(G3MCBuilder builder, String sceneId)
  {
     _builder = builder;
     _sceneId = sceneId;
  }

  public final void run(G3MContext context)
  {
    _builder.rawChangeScene(_sceneId);
  }
}