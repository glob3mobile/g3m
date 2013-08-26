package org.glob3.mobile.generated; 
public class MapBooBuilder_ChangeSceneTask extends GTask
{
  private MapBooBuilder _builder;
  private final int _sceneIndex;

  public MapBooBuilder_ChangeSceneTask(MapBooBuilder builder, int sceneIndex)
  {
     _builder = builder;
     _sceneIndex = sceneIndex;
  }

  public final void run(G3MContext context)
  {
    _builder.rawChangeScene(_sceneIndex);
  }
}