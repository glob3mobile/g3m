package org.glob3.mobile.generated;
//
//  LayerBuilder.cpp
//  G3M
//
//  Created by Mari Luz Mateo on 21/12/12.
//
//

//
//  LayerBuilder.hpp
//  G3M
//
//  Created by Mari Luz Mateo on 21/12/12.
//
//


//class Layer;
//class LayerSet;

public class LayerBuilder
{

  public static Layer createOSMLayer()
  {
    final String urlTemplate = "https://tile.openstreetmap.org/{level}/{x}/{y}.png";
    final Sector dataSector = Sector.FULL_SPHERE;
    final boolean isTransparent = false;
    final int firstLevel = 2;
    final int maxLevel = 18;
    final TimeInterval timeToCache = TimeInterval.fromDays(30);
    final boolean readExpired = true;
    final float transparency = 1F;
    final LayerCondition condition = null;
  
    final java.util.ArrayList<Info> layerInfo = new java.util.ArrayList<Info>();
    layerInfo.add(new Info("Map data by OpenStreetMap contributors (ODbL)"));
  
    return URLTemplateLayer.newMercator(urlTemplate, dataSector, isTransparent, firstLevel, maxLevel, timeToCache, readExpired, transparency, condition, layerInfo);
  }

  public static Layer createSentinel2CloudlessLayer()
  {
    final String urlTemplate = "https://tiles.maps.eox.at/wmts/1.0.0/s2cloudless-2020_3857/default/g/{level}/{y}/{x}.jpg";
    final Sector dataSector = Sector.FULL_SPHERE;
    final boolean isTransparent = false;
    final int firstLevel = 2;
    final int maxLevel = 17; // deepest level served by EOX
    final TimeInterval timeToCache = TimeInterval.fromDays(30);
    final boolean readExpired = true;
    final float transparency = 1F;
    final LayerCondition condition = null;
  
    final java.util.ArrayList<Info> layerInfo = new java.util.ArrayList<Info>();
    layerInfo.add(new Info("Sentinel-2 cloudless by EOX IT Services GmbH (Contains modified Copernicus Sentinel data 2020)"));
  
    return URLTemplateLayer.newMercator(urlTemplate, dataSector, isTransparent, firstLevel, maxLevel, timeToCache, readExpired, transparency, condition, layerInfo);
  }

  public static Layer createBlueMarbleLayer()
  {
    final String urlTemplate = "https://tiles.maps.eox.at/wmts/1.0.0/bluemarble_3857/default/g/{level}/{y}/{x}.jpg";
    final Sector dataSector = Sector.FULL_SPHERE;
    final boolean isTransparent = false;
    final int firstLevel = 2;
    final int maxLevel = 8; // deepest level served by EOX
    final TimeInterval timeToCache = TimeInterval.fromDays(30);
    final boolean readExpired = true;
    final float transparency = 1F;
    final LayerCondition condition = null;
  
    final java.util.ArrayList<Info> layerInfo = new java.util.ArrayList<Info>();
    layerInfo.add(new Info("Blue Marble Next Generation by NASA, rendered by EOX IT Services GmbH"));
  
    return URLTemplateLayer.newMercator(urlTemplate, dataSector, isTransparent, firstLevel, maxLevel, timeToCache, readExpired, transparency, condition, layerInfo);
  }

  public static LayerSet createDefault()
  {
    LayerSet layerSet = new LayerSet();
  
    layerSet.addLayer(createOSMLayer());
  
    return layerSet;
  }

}