package org.glob3.mobile.generated;import java.util.*;

//
//  MapBooOLDBuilder.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 5/25/13.
//
//

//
//  MapBooOLDBuilder.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 5/25/13.
//
//



//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class GL;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class G3MWidget;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class PlanetRenderer;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IStorage;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IDownloader;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IThreadUtils;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Planet;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class ICameraConstrainer;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class CameraRenderer;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Renderer;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class ProtoRenderer;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class GInitializationTask;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class PeriodicalTask;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Layer;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class LayerSet;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class GPUProgramManager;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class JSONBaseObject;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class JSONObject;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class JSONString;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class JSONArray;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class TimeInterval;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class MapQuestLayer;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class BingMapsLayer;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class CartoDBLayer;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class MapBoxLayer;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class WMSLayer;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class G3MContext;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IWebSocket;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class MapBooOLD_Scene;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class ErrorRenderer;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class SceneLighting;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class G3MEventContext;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Camera;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Tile;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class MarksRenderer;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class MapBooOLDBuilder;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Vector2I;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class URLTemplateLayer;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Sector;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IByteBuffer;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IBufferDownloadListener;

//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class TileVisibilityTester;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class TileLODTester;



public abstract class MapBooOLDApplicationChangeListener
{
  public void dispose()
  {
  }

  public abstract void onNameChanged(G3MContext context, String name);

  public abstract void onWebsiteChanged(G3MContext context, String website);

  public abstract void onEMailChanged(G3MContext context, String eMail);

  public abstract void onAboutChanged(G3MContext context, String about);

  public abstract void onIconChanged(G3MContext context, String icon);

  public abstract void onSceneChanged(G3MContext context, MapBooOLD_Scene scene);

  public abstract void onScenesChanged(G3MContext context, java.util.ArrayList<MapBooOLD_Scene> scenes);

  public abstract void onCurrentSceneChanged(G3MContext context, String sceneId, MapBooOLD_Scene scene);

  public abstract void onWebSocketOpen(G3MContext context);

  public abstract void onWebSocketClose(G3MContext context);

  public abstract void onTerrainTouch(MapBooOLDBuilder builder, G3MEventContext ec, Vector2F pixel, Camera camera, Geodetic3D position, Tile tile);

  public abstract void onFeatureInfoReceived(IByteBuffer buffer);

}
