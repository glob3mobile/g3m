//
//  MapBoo.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 8/21/15.
//
//

#include "MapBoo.hpp"

#include "IG3MBuilder.hpp"
#include "PlanetRendererBuilder.hpp"
#include "ChessboardLayer.hpp"


MapBoo::MapBoo(IG3MBuilder* builder) :
_builder(builder),
_layerSet(NULL)
{
  _layerSet = new LayerSet();
  _layerSet->addLayer( new ChessboardLayer() );

  _builder->getPlanetRendererBuilder()->setLayerSet( _layerSet );

}

MapBoo::~MapBoo() {
  delete _builder;
}

