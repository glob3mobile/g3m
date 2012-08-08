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
#include "TexturesHandler.hpp"


void TileImagesTileTexturizer::initialize(const InitializationContext* ic) {
  
}

TilePetitions* TileImagesTileTexturizer::createTilePetitions(const Tile* tile) {
  std::vector<Petition*> pet = _layerSet->createTilePetitions(_renderContext, *_factory, *tile, 
                                                              _parameters->_tileTextureWidth, 
                                                              _parameters->_tileTextureHeight);

  return new TilePetitions(tile->getLevel(),
                           tile->getRow(),
                           tile->getColumn(),
                           tile->getSector(),
                           pet);
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
                                                  Mesh* tessellatorMesh,
                                                  Mesh* previousMesh) {
  //THE TEXTURE HAS BEEN LOADED???
  TilePetitions* tp = getRegisteredTilePetitions(tile);
  
  if (tp!= NULL) {
    int texID = tp->getTexID();
    if (texID < 0){ //Texture has not been created
      if (tp->allFinished()){
        tp->createTexture(_texHandler, _factory, 
                          _parameters->_tileTextureWidth, _parameters->_tileTextureHeight);
        texID = tp->getTexID();
      }
    }
  
    if (texID > -1) {
      tile->setTextureSolved(true);
    
      //printf("TEXTURIZED %d, %d, %d\n", tile->getLevel(), tile->getRow(), tile->getColumn());
    
      TextureMapping * tMap = new TextureMapping(texID, getTextureCoordinates(tessellator), _texHandler, 
                                                 tp->getPetitionsID(), 
                                                 _parameters->_tileTextureWidth, 
                                                 _parameters->_tileTextureHeight);
      TexturedMesh* texMesh = new TexturedMesh(tessellatorMesh, false, tMap, true);
      delete previousMesh;   //If a new mesh has been produced we delete the previous one
      return texMesh;
    }
  
  }
  
  return NULL;

}

Mesh* TileImagesTileTexturizer::getFallBackTexturedMesh(Tile* tile,
                                                        const TileTessellator* tessellator,
                                                        Mesh* tessellatorMesh,
                                                        Mesh* previousMesh) {
  const TextureMapping* fbTMap = NULL;
  int texID = -1;
  Tile* fbTile = tile->getParent();
  while (fbTile != NULL && texID < 0) {
    
    if (fbTile->isTextureSolved()){
      TexturedMesh* texMesh = (TexturedMesh*) fbTile->getTexturizerMesh();
      if (texMesh != NULL){
        fbTMap = texMesh->getTextureMapping();
        
        texID = _texHandler->getTextureIdIfAvailable(fbTMap->getStringTexID(), 
                                                     fbTMap->getWidth(), fbTMap->getHeight());
        
        break;
      }
    }
    fbTile = fbTile->getParent();       //TRYING TO CREATE FALLBACK TEXTURE FROM ANTECESOR
  }
  
  //CREATING MESH
  if (texID > -1) {
    TextureMapping* tMap = new TextureMapping(texID, getTextureCoordinates(tessellator), _texHandler, fbTMap->getStringTexID(), 
                                              fbTMap->getWidth(), fbTMap->getHeight());
    translateAndScaleFallBackTex(tile, fbTile, tMap);
    TexturedMesh* texMesh = new TexturedMesh(tessellatorMesh, false, tMap, true);
    delete previousMesh;   //If a new mesh has been produced we delete the previous one
    return texMesh;
  }
  
  return NULL;
}

void TileImagesTileTexturizer::registerNewRequest(Tile *tile){
  if (getRegisteredTilePetitions(tile) == NULL) { //NOT YET REQUESTED
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
                                          Mesh* previousMesh) {
  //STORING CONTEXT
  _factory    = rc->getFactory();
  _texHandler = rc->getTexturesHandler();
  _downloader = rc->getDownloaderOLD();
  _renderContext = rc;
  
  //printf("TP SIZE: %lu\n", _tilePetitions.size());
  
  Mesh* mesh = getNewTextureMesh(tile, tessellator, tessellatorMesh, previousMesh);
  if (mesh == NULL){
    //REGISTERING PETITION AND SENDING TO THE NET IF NEEDED
    registerNewRequest(tile);
    mesh = getNewTextureMesh(tile, tessellator, tessellatorMesh, previousMesh);
    
    //If we still can't get a new TexturedMesh we try to get a FallBack Mesh
    if (mesh == NULL){
      mesh = getFallBackTexturedMesh(tile, tessellator, tessellatorMesh, previousMesh);
    }
  }
  
  return mesh;
}

void TileImagesTileTexturizer::tileToBeDeleted(Tile* tile) {
  
  TilePetitions* tp = getRegisteredTilePetitions(tile);
  
  if (tp != NULL){
//    if (tp->getTexID() > -1){
//      _texHandler->takeTexture(tp->getTexID());
//    }
    
    tp->cancelPetitions(*_downloader);
    
    removeRegisteredTilePetitions(tile);
  }
  
  
}

bool TileImagesTileTexturizer::tileMeetsRenderCriteria(Tile* tile) {
  // TODO: compare tile->level with maxLevel in WMS-layer definition
  return false;
}

void TileImagesTileTexturizer::justCreatedTopTile(Tile *tile) {
  int priority = 9999; //MAX PRIORITY
  TilePetitions *tp = createTilePetitions(tile);
  _tilePetitionsTopTile.push_back(tp); //STORED
  _tilePetitions.push_back(tp);
  tp->requestToNet(*_downloader, priority);
}

bool TileImagesTileTexturizer::isReady(const RenderContext *rc) {
  int todo_JM;
//  for (int i = 0; i < _tilePetitionsTopTile.size(); i++) {
//    if (!_tilePetitionsTopTile[i]->allFinished()) {
//      return false;
//    }
//  }
//  if (_tilePetitionsTopTile.size() > 0) {
//    _tilePetitionsTopTile.clear();
//  }
  
  return true;
}



TilePetitions* TileImagesTileTexturizer::getRegisteredTilePetitions(Tile* tile) const
{
//  for(std::list<TilePetitions*>::iterator it = _tilePetitions.begin();
//      it != _tilePetitions.end();
//      it++) {
//    TilePetitions* tp = *it;
//    
//    if (tile->getLevel()  == tp->getLevel() &&
//        tile->getRow()    == tp->getRow()   &&
//        tile->getColumn() == tp->getColumn()) {
//      return tp;
//    }
//  }
  
  for (int i = 0; i < _tilePetitions.size(); i++) {
    TilePetitions* tp = _tilePetitions[i];
    if (tile->getLevel()  == tp->getLevel() &&
        tile->getRow()    == tp->getRow()   &&
        tile->getColumn() == tp->getColumn()) {
      return tp;
    }
  }
  
  return NULL;
}

void TileImagesTileTexturizer::removeRegisteredTilePetitions(Tile* tile)
{
//  for(std::list<TilePetitions*>::iterator it = _tilePetitions.begin();
//      it != _tilePetitions.end();
//      it++) {
//    TilePetitions* tp = *it;
//    if (tile->getLevel()  == tp->getLevel() &&
//        tile->getRow()    == tp->getRow()   &&
//        tile->getColumn() == tp->getColumn()) {
//      _tilePetitions.erase(it);
//      delete tp;
//      break;
//    }
//  }
  
  TilePetitions* tp = NULL;
  for (int i = 0; i < _tilePetitions.size(); i++) {
    tp = _tilePetitions[i];
    if (tile->getLevel()  == tp->getLevel() &&
        tile->getRow()    == tp->getRow()   &&
        tile->getColumn() == tp->getColumn()){
      _tilePetitions.erase(_tilePetitions.begin() + i);
      delete tp;
      break;
    }
  }
}
