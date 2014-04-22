package org.glob3.mobile.generated; 
//
//  RasterLayer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 4/22/14.
//
//

//
//  RasterLayer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 4/22/14.
//
//



public abstract class RasterLayer extends Layer
{
  protected RasterLayer(LayerCondition condition, TimeInterval timeToCache, boolean readExpired, LayerTilesRenderParameters parameters, float transparency)
  {
     super(condition, timeToCache, readExpired, parameters, transparency);
  }

}