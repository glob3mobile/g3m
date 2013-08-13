package org.glob3.mobile.generated; 
//
//  MapBooBuilder.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 5/25/13.
//
//

//
//  MapBooBuilder.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 5/25/13.
//
//



//class GL;
//class G3MWidget;
//class PlanetRenderer;
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
//class GPUProgramManager;
//class JSONBaseObject;
//class JSONObject;
//class JSONString;
//class TimeInterval;
//class MapQuestLayer;
//class BingMapsLayer;
//class CartoDBLayer;
//class MapBoxLayer;
//class WMSLayer;
//class G3MContext;
//class IWebSocket;
//class MapBoo_Scene;




public abstract class MapBooApplicationChangeListener extends Disposable
{
  public void dispose()
  {
    JAVA_POST_DISPOSE
  }

  public abstract void onNameChanged(G3MContext context, String name);

  public abstract void onIconChanged(G3MContext context, String icon);

  public abstract void onScenesChanged(G3MContext context, java.util.ArrayList<MapBoo_Scene> scenes);

  // virtual void onWarningsChanged() = 0;

  public abstract void onSceneChanged(G3MContext context, int sceneIndex);
}