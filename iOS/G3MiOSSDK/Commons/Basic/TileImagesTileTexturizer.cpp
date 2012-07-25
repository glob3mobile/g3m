//
//  TileImagesTileTexturizer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 12/07/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

#include "TileImagesTileTexturizer.hpp"

#include "Context.hpp"
#include "TextureMapping.hpp"
#include "TexturedMesh.hpp"
#include "TilePetitions.hpp"
#include "WMSLayer.hpp"
#include "TileTessellator.hpp"
#include "ITimer.hpp"


TilePetitions* TileImagesTileTexturizer::createTilePetitions(const Tile* tile) {  
  std::vector<Petition*> pet = _layerSet->createTilePetitions(*tile, 
                                                              _parameters->_tileTextureWidth, 
                                                              _parameters->_tileTextureHeight);
  
  TilePetitions* tp = new TilePetitions(tile->getLevel(), tile->getRow(), 
                                        tile->getColumn(), tile->getSector(), 
                                        pet);
  return tp;
}

std::vector<MutableVector2D> TileImagesTileTexturizer::getTextureCoordinates(const TileTessellator* tessellator) const {
  if (_texCoordsCache == NULL) {
    _texCoordsCache = tessellator->createUnitTextCoords();
  }
  
  return *_texCoordsCache;
}

void TileImagesTileTexturizer::translateAndScaleFallBackTex(Tile* tile,
                                                            Tile* fallbackTile, 
                                                            TextureMapping* tmap) const {
  const Sector tileSector         = tile->getSector();
  const Sector fallbackTileSector = fallbackTile->getSector();
  
  tmap->setTranslationAndScale(tileSector.getTranslationFactor(fallbackTileSector),
                               tileSector.getScaleFactor(fallbackTileSector));
}

Mesh* TileImagesTileTexturizer::getNewTextureMesh(Tile* tile,
                                                  const TileTessellator* tessellator,
                                                  Mesh* tessellatorMesh) {
  //THE TEXTURE HAS BEEN LOADED???
  int texID = getTexture(tile);
  if (texID > -1){
    tile->setTextureSolved(true);
    TextureMapping * tMap = new TextureMapping(texID, getTextureCoordinates(tessellator));
    return new TexturedMesh(tessellatorMesh, false, tMap, true);
  } else{
    return NULL;
  }
}

Mesh* TileImagesTileTexturizer::getFallBackTexturedMesh(Tile* tile,
                                                        const TileTessellator* tessellator,
                                                        Mesh* tessellatorMesh) {
  int texID = -1;
  Tile* fbTile = tile->getParent();
  while (fbTile != NULL && texID < 0) {
    
    TilePetitions* tp = createTilePetitions(fbTile);
    tp->requestToCache(*_downloader);
    if (tp->allFinished()){
      tp->createTexture(_texHandler, _factory, 
                        _parameters->_tileTextureWidth, _parameters->_tileTextureHeight);
      texID = tp->getTexID();
    }
    
    //We do no store the petitions
    delete tp;
    
    if (texID > -1) break;
    fbTile = fbTile->getParent();       //TRYING TO CREATE FALLBACK TEXTURE FROM ANTECESOR
  }
  
  //CREATING MESH
  if (texID > -1) {
    TextureMapping * tMap = new TextureMapping(texID, getTextureCoordinates(tessellator));
    translateAndScaleFallBackTex(tile, fbTile, tMap);
    return new TexturedMesh(tessellatorMesh, false, tMap, true);
  }
  
  return NULL;
}

void TileImagesTileTexturizer::registerNewRequest(Tile *tile){
  
  if (getRegisteredTilePetitions(tile) == NULL){ //NOT YET REQUESTED
    
    int priority = tile->getLevel(); //DOWNLOAD PRIORITY SET TO TILE LEVEL
    TilePetitions *tp = createTilePetitions(tile);
    _tilePetitions.push_back(tp); //STORED
    tp->requestToNet(*_downloader, priority);
  }
}

Mesh* TileImagesTileTexturizer::texturize(const RenderContext* rc,
                                          Tile* tile,
                                          const TileTessellator* tessellator,
                                          Mesh* tessellatorMesh,
                                          Mesh* previousMesh,
                                          ITimer* timer) {
  //STORING CONTEXT
  _factory    = rc->getFactory();
  _texHandler = rc->getTexturesHandler();
  _downloader = rc->getDownloader();
  
  //printf("TP SIZE: %lu\n", _tilePetitions.size());
  
  if (timer != NULL) {
    int __TODO_tune_TEXTURIZER_render_budget;
    if ( timer->elapsedTime().milliseconds() > 20 ) {
      return getFallBackTexturedMesh(tile, tessellator, tessellatorMesh);
    }
  }
  
  Mesh* mesh = getNewTextureMesh(tile, tessellator, tessellatorMesh);
  if (mesh == NULL){
    //REGISTERING PETITION AND SENDING TO THE NET IF NEEDED
    registerNewRequest(tile);
    mesh = getNewTextureMesh(tile, tessellator, tessellatorMesh);
  }
  
  //If we can't get a new TexturedMesh we try to get a FallBack Mesh
  if (mesh == NULL){
    mesh = getFallBackTexturedMesh(tile, tessellator, tessellatorMesh);
  }
  
  //If a new mesh has been produced we delete the previous one
  if (mesh != NULL && previousMesh != NULL) {
    delete previousMesh;
  }
  
  return mesh;
}

void TileImagesTileTexturizer::tileToBeDeleted(Tile* tile) {
  
  TilePetitions* tp = getRegisteredTilePetitions(tile);
  
  if (tp != NULL){
    if (tp->getTexID() > -1){
      _texHandler->takeTexture(tp->getTexID());
    }
    
    tp->cancelPetitions(*_downloader);
    
    removeRegisteredTilePetitions(tile);
  }
  
  
}

bool TileImagesTileTexturizer::tileMeetsRenderCriteria(Tile* tile) {
  // TODO: compare tile->level with maxLevel in WMS-layer definition
  return false;
}

void TileImagesTileTexturizer::justCreatedTopTile(Tile *tile) {
  registerNewRequest(tile);
}

bool TileImagesTileTexturizer::isReadyToRender(const RenderContext *rc) {
  int __TODO_check_for_level_0_loaded;
  
  return true;
}

Rectangle TileImagesTileTexturizer::getImageRectangleInTexture(const Sector& wholeSector, 
                                                               const Sector& imageSector,
                                                               int texWidth, int texHeight) const
{
  Vector2D pos = wholeSector.getUVCoordinates(imageSector.lower().latitude(), imageSector.lower().longitude());
  
  double width = wholeSector.getDeltaLongitude().degrees() / imageSector.getDeltaLongitude().degrees();
  double height = wholeSector.getDeltaLatitude().degrees() / imageSector.getDeltaLatitude().degrees();
  
  
  
  Rectangle r(pos.x() * texWidth, pos.y() * texHeight, width * texWidth, height * texHeight);
  return r;
}

TilePetitions* TileImagesTileTexturizer::getRegisteredTilePetitions(Tile* tile) const
{
  TilePetitions* tp = NULL;
  for (int i = 0; i < _tilePetitions.size(); i++) {
    tp = _tilePetitions[i];
    if (tile->getLevel() == tp->getLevel() &&
        tile->getRow() == tp->getRow() &&
        tile->getColumn() == tp->getColumn()){
      return tp;
    }
  }
  return NULL;
}

void TileImagesTileTexturizer::removeRegisteredTilePetitions(Tile* tile)
{
  TilePetitions* tp = NULL;
  for (int i = 0; i < _tilePetitions.size(); i++) {
    tp = _tilePetitions[i];
    if (tile->getLevel() == tp->getLevel() &&
        tile->getRow() == tp->getRow() &&
        tile->getColumn() == tp->getColumn()){
      _tilePetitions.erase(_tilePetitions.begin() + i);
      delete tp;
      break;
    }
  }
}

int TileImagesTileTexturizer::getTexture(Tile* tile)
{
  TilePetitions* tp = getRegisteredTilePetitions(tile);
  
  if (tp!= NULL) {
    
    if (tp->getTexID() > -1){
      return tp->getTexID();
    } else{
      if (tp->allFinished()){
        tp->createTexture(_texHandler, _factory, 
                          _parameters->_tileTextureWidth, _parameters->_tileTextureHeight);
        return tp->getTexID();
      }
    }
  }
  
  return -1;
}
