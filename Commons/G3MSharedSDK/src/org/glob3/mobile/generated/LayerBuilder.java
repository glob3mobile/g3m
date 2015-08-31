package org.glob3.mobile.generated; 
//
//  LayerBuilder.cpp
//  G3MiOSSDK
//
//  Created by Mari Luz Mateo on 21/12/12.
//
//

//
//  LayerBuilder.hpp
//  G3MiOSSDK
//
//  Created by Mari Luz Mateo on 21/12/12.
//
//



public class LayerBuilder
{
  public static LayerSet createDefault()
  {
    LayerSet layerSet = new LayerSet();
    layerSet.addLayer(MapQuestLayer.newOSM(TimeInterval.fromDays(30)));
    return layerSet;
  }
  public static LayerSet createDefaultSatelliteImagery()
  {
    LayerSet layerSet = new LayerSet();
  
    WMSLayer blueMarble = new WMSLayer("bmng200405", new URL("http://www.nasa.network.com/wms?", false), WMSServerVersion.WMS_1_1_0, Sector.fullSphere(), "image/jpeg", "EPSG:4326", "", false, new LevelTileCondition(0, 6), TimeInterval.fromDays(30), true);
    layerSet.addLayer(blueMarble);
  
    WMSLayer i3Landsat = new WMSLayer("esat", new URL("http://data.worldwind.arc.nasa.gov/wms?", false), WMSServerVersion.WMS_1_1_0, Sector.fullSphere(), "image/jpeg", "EPSG:4326", "", false, new LevelTileCondition(7, 10), TimeInterval.fromDays(30), true);
    layerSet.addLayer(i3Landsat);
  
    WMSLayer bing = new WMSLayer("ve", new URL("http://worldwind27.arc.nasa.gov/wms/virtualearth?", false), WMSServerVersion.WMS_1_1_0, Sector.fullSphere(), "image/jpeg", "EPSG:4326", "", false, new LevelTileCondition(11, 1000), TimeInterval.fromDays(30), true);
    layerSet.addLayer(bing);
  
    return layerSet;
  }

  /**
   * Returns an array with the names of the layers that make up the default layerSet
   *
   * @return layersNames: std::vector<std::string>
   */
  public static java.util.ArrayList<String> getDefaultLayersNames()
  {
    java.util.ArrayList<String> layersNames = new java.util.ArrayList<String>();
    layersNames.add("bmng200405");
    layersNames.add("esat");
    layersNames.add("ve");
  
    return layersNames;
  }
  public static WMSLayer createBingLayer(boolean enabled)
  {
    WMSLayer bing = new WMSLayer("ve", new URL("http://worldwind27.arc.nasa.gov/wms/virtualearth?", false), WMSServerVersion.WMS_1_1_0, Sector.fullSphere(), "image/jpeg", "EPSG:4326", "", false, null, TimeInterval.fromDays(30), true);
    bing.setEnable(enabled);
  
    return bing;
  }
  public static WMSLayer createOSMLayer(boolean enabled)
  {
    WMSLayer osm = new WMSLayer("osm_auto:all", new URL("http://129.206.228.72/cached/osm", false), WMSServerVersion.WMS_1_1_0, Sector.fullSphere(), "image/jpeg", "EPSG:4326", "", false, null, TimeInterval.fromDays(30), true);
                                 // Sector::fromDegrees(-85.05, -180.0, 85.05, 180.0),
    osm.setEnable(enabled);
  
    return osm;
  }
  public static WMSLayer createPNOALayer(boolean enabled)
  {
    WMSLayer pnoa = new WMSLayer("PNOA", new URL("http://www.idee.es/wms/PNOA/PNOA", false), WMSServerVersion.WMS_1_1_0, Sector.fromDegrees(21, -18, 45, 6), "image/png", "EPSG:4326", "", true, null, TimeInterval.fromDays(30), true);
    pnoa.setEnable(enabled);
  
    return pnoa;
  }
  public static WMSLayer createBlueMarbleLayer(boolean enabled)
  {
    WMSLayer blueMarble = new WMSLayer("bmng200405", new URL("http://www.nasa.network.com/wms?", false), WMSServerVersion.WMS_1_1_0, Sector.fullSphere(), "image/jpeg", "EPSG:4326", "", false, new LevelTileCondition(0, 6), TimeInterval.fromDays(30), true);
    blueMarble.setEnable(enabled);
  
    return blueMarble;
  }
  public static WMSLayer createI3LandSatLayer(boolean enabled)
  {
    WMSLayer i3Landsat = new WMSLayer("esat", new URL("http://data.worldwind.arc.nasa.gov/wms?", false), WMSServerVersion.WMS_1_1_0, Sector.fullSphere(), "image/jpeg", "EPSG:4326", "", false, new LevelTileCondition(7, 100), TimeInterval.fromDays(30), true);
    i3Landsat.setEnable(enabled);
  
    return i3Landsat;
  }
  public static WMSLayer createPoliticalLayer(boolean enabled)
  {
    WMSLayer political = new WMSLayer("topp:cia", new URL("http://worldwind22.arc.nasa.gov/geoserver/wms?", false), WMSServerVersion.WMS_1_1_0, Sector.fullSphere(), "image/png", "EPSG:4326", "countryboundaries", true, null, TimeInterval.fromDays(30), true);
    political.setEnable(enabled);
  
    return political;
  }
}