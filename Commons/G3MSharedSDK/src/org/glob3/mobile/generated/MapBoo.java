package org.glob3.mobile.generated; 
//
//  MapBoo.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 8/21/15.
//
//

//
//  MapBoo.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 8/21/15.
//
//


//class IG3MBuilder;
//class LayerSet;


public class MapBoo
{
  private IG3MBuilder _builder;

  private LayerSet _layerSet;


  public MapBoo(IG3MBuilder builder)
  {
     _builder = builder;
     _layerSet = null;
    _layerSet = new LayerSet();
    _layerSet.addLayer(new ChessboardLayer());
  
    _builder.getPlanetRendererBuilder().setLayerSet(_layerSet);
  
  }

  public void dispose()
  {
    if (_builder != null)
       _builder.dispose();
  }

}