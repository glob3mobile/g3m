package org.glob3.mobile.generated; 
public class MapBooOLDBuilder_ChangeSceneTask extends GTask
{
  private MapBooOLDBuilder _builder;
  private final String _sceneId;

  public MapBooOLDBuilder_ChangeSceneTask(MapBooOLDBuilder builder, String sceneId)
  {
     _builder = builder;
     _sceneId = sceneId;
  }

  public final void run(G3MContext context)
  {
    _builder.rawChangeScene(_sceneId);
  }
}