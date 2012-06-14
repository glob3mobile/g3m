//
//  TileRenderer.cpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 12/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#include <iostream>

#include "TileRenderer.hpp"


TileRenderer::TileRenderer(int resolution):
_resolution(resolution)
{
}


TileRenderer::~TileRenderer()
{
  if (!initialTiles.empty()) Tile::deleteIndices();
}


void TileRenderer::initialize(const InitializationContext* ic)
{
  Tile::createIndices(_resolution, true);
  int initialDiv = 2;
  double angle = 180 / initialDiv;
  for (int j=0; j<initialDiv; j++) for (int i=0; i<initialDiv*2; i++) {
    Sector bbox(Angle::fromDegrees(-90+j*angle), Angle::fromDegrees(-180+i*angle), 
                Angle::fromDegrees(-90+(j+1)*angle), Angle::fromDegrees(-180+(i+1)*angle));
    Tile *tile = new Tile(bbox);
    tile->createVertices(ic->getPlanet());
    initialTiles.push_back(tile);
  }
}  


bool TileRenderer::onTouchEvent(const TouchEvent* touchEvent){
  return false;
}



int TileRenderer::render(const RenderContext* rc)
{
  for (int n=0; n<initialTiles.size(); n++) initialTiles[n]->render(rc);
  
  return 9999;
}


