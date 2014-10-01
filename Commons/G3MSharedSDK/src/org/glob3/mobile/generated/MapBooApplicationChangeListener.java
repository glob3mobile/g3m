<<<<<<< HEAD
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
//class Storage;
//class IDownloader;
//class IThreadUtils;
//class Planet;
//class ICameraConstrainer;
//class CameraRenderer;
//class Renderer;
//class ProtoRenderer;
//class GInitializationTask;
//class PeriodicalTask;
//class Layer;
//class LayerSet;
//class GPUProgramManager;
//class JSONBaseObject;
//class JSONObject;
//class JSONString;
//class JSONArray;
//class TimeInterval;
//class MapQuestLayer;
//class BingMapsLayer;
//class CartoDBLayer;
//class MapBoxLayer;
//class WMSLayer;
//class G3MContext;
//class IWebSocket;
//class MapBoo_Scene;
//class ErrorRenderer;
//class SceneLighting;
//class G3MEventContext;
//class Camera;
//class Tile;
//class MarksRenderer;
//class MapBooBuilder;
//class Vector2I;
//class URLTemplateLayer;
//class Sector;



public abstract class MapBooApplicationChangeListener
{
  public void dispose()
  {
  }

  public abstract void onNameChanged(G3MContext context, String name);

  public abstract void onWebsiteChanged(G3MContext context, String website);

  public abstract void onEMailChanged(G3MContext context, String eMail);

  public abstract void onAboutChanged(G3MContext context, String about);

  public abstract void onIconChanged(G3MContext context, String icon);

  public abstract void onSceneChanged(G3MContext context, MapBoo_Scene scene);

  public abstract void onScenesChanged(G3MContext context, java.util.ArrayList<MapBoo_Scene> scenes);

  public abstract void onCurrentSceneChanged(G3MContext context, String sceneId, MapBoo_Scene scene);

  public abstract void onWebSocketOpen(G3MContext context);

  public abstract void onWebSocketClose(G3MContext context);

  public abstract void onTerrainTouch(MapBooBuilder builder, G3MEventContext ec, Vector2I pixel, Camera camera, Geodetic3D position, Tile tile);

=======
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
//class ProtoRenderer;
//class GInitializationTask;
//class PeriodicalTask;
//class Layer;
//class LayerSet;
//class GPUProgramManager;
//class JSONBaseObject;
//class JSONObject;
//class JSONString;
//class JSONArray;
//class TimeInterval;
//class MapQuestLayer;
//class BingMapsLayer;
//class CartoDBLayer;
//class MapBoxLayer;
//class WMSLayer;
//class G3MContext;
//class IWebSocket;
//class MapBoo_Scene;
//class ErrorRenderer;
//class SceneLighting;
//class G3MEventContext;
//class Camera;
//class Tile;
//class MarksRenderer;
//class MapBooBuilder;
//class Vector2I;
//class URLTemplateLayer;
//class Sector;
//class IByteBuffer;
//class IBufferDownloadListener;




public abstract class MapBooApplicationChangeListener
{
  public void dispose()
  {
  }

  public abstract void onNameChanged(G3MContext context, String name);

  public abstract void onWebsiteChanged(G3MContext context, String website);

  public abstract void onEMailChanged(G3MContext context, String eMail);

  public abstract void onAboutChanged(G3MContext context, String about);

  public abstract void onIconChanged(G3MContext context, String icon);

  public abstract void onSceneChanged(G3MContext context, MapBoo_Scene scene);

  public abstract void onScenesChanged(G3MContext context, java.util.ArrayList<MapBoo_Scene> scenes);

  public abstract void onCurrentSceneChanged(G3MContext context, String sceneId, MapBoo_Scene scene);

  public abstract void onWebSocketOpen(G3MContext context);

  public abstract void onWebSocketClose(G3MContext context);

  public abstract void onTerrainTouch(MapBooBuilder builder, G3MEventContext ec, Vector2I pixel, Camera camera, Geodetic3D position, Tile tile);

  public abstract void onFeatureInfoReceived(IByteBuffer buffer);

>>>>>>> point-cloud
}