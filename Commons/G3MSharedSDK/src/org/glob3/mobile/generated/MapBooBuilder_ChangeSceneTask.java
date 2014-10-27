package org.glob3.mobile.generated; 
public class MapBooBuilder_ChangeSceneTask extends GTask
{
  private MapBooBuilder _builder;
  private final String _sceneId;

  public MapBooBuilder_ChangeSceneTask(MapBooBuilder builder, String sceneId)
  {
     _builder = builder;
     _sceneId = sceneId;
  }

  public final void run(G3MContext context)
  {
    _builder.rawChangeScene(_sceneId);
  }
}