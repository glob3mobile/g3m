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
    final String urlTemplate = "https://[abc].tile.openstreetmap.org/{level}/{x}/{y}.png";
    final Sector dataSector = Sector.FULL_SPHERE;
    final boolean isTransparent = false;
    final int firstLevel = 2;
    final int maxLevel = 18;
    final TimeInterval timeToCache = TimeInterval.fromDays(30);
    final boolean readExpired = true;
  
    return URLTemplateLayer.newMercator(urlTemplate, dataSector, isTransparent, firstLevel, maxLevel, timeToCache, readExpired);
  }

  public static LayerSet createDefault()
  {
    LayerSet layerSet = new LayerSet();
  
    layerSet.addLayer(createOSMLayer());
  
    return layerSet;
  }

}