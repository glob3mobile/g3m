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
    _builder.openApplicationTube(context);
  }

  public final boolean isDone(G3MContext context)
  {
    return true;
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
