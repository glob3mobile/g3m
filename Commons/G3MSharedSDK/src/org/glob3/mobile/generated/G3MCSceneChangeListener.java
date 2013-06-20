package org.glob3.mobile.generated; 
//
//  G3MCBuilder.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 5/25/13.
//
//

//
//  G3MCBuilder.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 5/25/13.
//
//



//class GL;
//class G3MWidget;
//class TileRenderer;
//class IStorage;
//class IDownloader;
//class IThreadUtils;
//class Planet;
//class ICameraConstrainer;
//class CameraRenderer;
//class Renderer;
//class GInitializationTask;
//class PeriodicalTask;
//class Layer;
//class LayerSet;
//class G3MCSceneDescription;
//class Color;
//class JSONBaseObject;
//class JSONObject;
//class TimeInterval;
//class MapQuestLayer;
//class BingMapsLayer;
//class CartoDBLayer;
//class MapBoxLayer;
//class WMSLayer;
//class G3MContext;
//class IWebSocket;




public abstract class G3MCSceneChangeListener
{
  public void dispose()
  {

  }

  public abstract void onSceneChanged(String sceneId);

  public abstract void onBaseLayerChanged(Layer baseLayer);

  public abstract void onOverlayLayerChanged(Layer overlayLayer);

  public abstract void onUserChanged(String user);

  public abstract void onNameChanged(String name);

  public abstract void onDescriptionChanged(String description);

  public abstract void onBackgroundColorChanged(Color backgroundColor);

}