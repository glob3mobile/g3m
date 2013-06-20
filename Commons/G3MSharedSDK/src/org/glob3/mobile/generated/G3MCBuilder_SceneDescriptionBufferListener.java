package org.glob3.mobile.generated; 
public class G3MCBuilder_SceneDescriptionBufferListener extends IBufferDownloadListener
{
  private G3MCBuilder _builder;

  public G3MCBuilder_SceneDescriptionBufferListener(G3MCBuilder builder)
  {
     _builder = builder;
  }


  public final void onDownload(URL url, IByteBuffer buffer, boolean expired)
  {

    _builder.parseSceneDescription(buffer.getAsString(), url);
    if (buffer != null)
       buffer.dispose();

//    const JSONBaseObject* jsonBaseObject = IJSONParser::instance()->parse(buffer, true);
//    
//    if (jsonBaseObject == NULL) {
//      ILogger::instance()->logError("Can't parse SceneJSON from %s",
//                                    url.getPath().c_str());
//    }
//    else {
//      const JSONObject* jsonObject = jsonBaseObject->asObject();
//      if (jsonObject == NULL) {
//        ILogger::instance()->logError("Invalid SceneJSON (1)");
//      }
//      else {
//        const JSONString* error = jsonObject->getAsString("error");
//        if (error == NULL) {
//          const int timestamp = (int) jsonObject->getAsNumber("ts", 0);
//          
//          if (_builder->getSceneTimestamp() != timestamp) {
//            const JSONString* jsonUser = jsonObject->getAsString("user");
//            if (jsonUser != NULL) {
//              _builder->setSceneUser(jsonUser->value());
//            }
//
//            //id
//            
//            const JSONString* jsonName = jsonObject->getAsString("name");
//            if (jsonName != NULL) {
//              _builder->setSceneName(jsonName->value());
//            }
//
//            const JSONString* jsonDescription = jsonObject->getAsString("description");
//            if (jsonDescription != NULL) {
//              _builder->setSceneDescription(jsonDescription->value());
//            }
//
//            const JSONString* jsonBGColor = jsonObject->getAsString("bgColor");
//            if (jsonBGColor != NULL) {
//              const Color* bgColor = Color::parse(jsonBGColor->value());
//              if (bgColor == NULL) {
//                ILogger::instance()->logError("Invalid format in attribute 'bgColor' (%s)",
//                                              jsonBGColor->value().c_str());
//              }
//              else {
//                _builder->setSceneBackgroundColor(*bgColor);
//                delete bgColor;
//              }
//            }
//
//            const JSONBaseObject* jsonBaseLayer = jsonObject->get("baseLayer");
//            if (jsonBaseLayer != NULL) {
//              _builder->setSceneBaseLayer( parseLayer(jsonBaseLayer) );
//            }
//            
//            const JSONBaseObject* jsonOverlayLayer = jsonObject->get("overlayLayer");
//            if (jsonOverlayLayer != NULL) {
//              _builder->setSceneOverlayLayer( parseLayer(jsonOverlayLayer) );
//            }
//
//            //tags
//            
//            _builder->setSceneTimestamp(timestamp);
//          }
//        }
//        else {
//          ILogger::instance()->logError("Server Error: %s",
//                                        error->value().c_str());
//        }
//      }
//      
//      delete jsonBaseObject;
//    }
//
//    delete buffer;

  }

  public final void onError(URL url)
  {
    ILogger.instance().logError("Can't download SceneJSON from %s", url.getPath());
  }

  public final void onCancel(URL url)
  {
    // do nothing
  }

  public final void onCanceledDownload(URL url, IByteBuffer buffer, boolean expired)
  {
    // do nothing
  }

}