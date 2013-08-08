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
//void MapBooBuilder::rawChangeApplication(const std::string& applicationId) {
//  if (applicationId.compare(_applicationId) != 0) {
//    resetApplication(applicationId);
//    
//    resetG3MWidget();
//    
//    if (_applicationListener != NULL) {
//      _applicationListener->onApplicationChanged(applicationId);
//    }
//    
//    if (_sceneTubeWebSocket != NULL) {
//      _sceneTubeWebSocket->close();
//    }
//  }
//}
