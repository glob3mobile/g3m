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
#include "TilesRenderParameters.hpp"

void TileImagesTileTexturizer::initialize(const InitializationContext* ic,
                                          const TilesRenderParameters* parameters) {
  
}

TilePetitions* TileImagesTileTexturizer::createTilePetitions(const RenderContext* rc,
                                                             const Tile* tile) {
  std::vector<Petition*> pet = _layerSet->createTilePetitions(rc,
                                                              tile,
                                                              _parameters->_tileTextureWidth,
                                                              _parameters->_tileTextureHeight);
  
  return new TilePetitions(tile->getLevel(),
                           tile->getRow(),
                           tile->getColumn(),
                           tile->getSector(),
                           pet);
}

float* TileImagesTileTexturizer::getTextureCoordinates(const TileRenderContext* trc) const {
  if (_texCoordsCache == NULL) {
    std::vector<MutableVector2D>* texCoordsV = trc->getTessellator()->createUnitTextCoords();
    
    const int texCoordsSize = texCoordsV->size();
    float* texCoordsA = new float[2 * texCoordsSize];
    int p = 0;
    for (int i = 0; i < texCoordsSize; i++) {
      texCoordsA[p++] = (float) texCoordsV->at(i).x();
      texCoordsA[p++] = (float) texCoordsV->at(i).y();
    }
    
    delete texCoordsV;
    
    _texCoordsCache = texCoordsA;

  }
  return _texCoordsCache;
}

void TileImagesTileTexturizer::translateAndScaleFallBackTex(Tile* tile,
                                                            Tile* fallbackTile,
                                                            SimpleTextureMapping* tmap) const {
  const Sector tileSector         = tile->getSector();
  const Sector fallbackTileSector = fallbackTile->getSector();
  
  tmap->setTranslationAndScale(tileSector.getTranslationFactor(fallbackTileSector),
                               tileSector.getScaleFactor(fallbackTileSector));
}

Mesh* TileImagesTileTexturizer::getNewTextureMesh(Tile* tile,
                                                  const TileRenderContext* trc,
                                                  Mesh* tessellatorMesh,
                                                  Mesh* previousMesh) {
  //THE TEXTURE HAS BEEN LOADED???
  TilePetitions* tp = getRegisteredTilePetitions(tile);
  
  if (tp != NULL) {
    GLTextureID texID = tp->getTexID();
    if (!texID.isValid()){ //Texture has not been created
      if (tp->allFinished()){
        tp->createTexture(_texturesHandler, _factory,
                          _parameters->_tileTextureWidth, _parameters->_tileTextureHeight);
        texID = tp->getTexID();
      }
    }
    
    if (texID.isValid()) {
      tile->setTextureSolved(true);
      tile->setTexturizerDirty(false);

      //printf("TEXTURIZED %d, %d, %d\n", tile->getLevel(), tile->getRow(), tile->getColumn());
      
      TextureMapping * tMap = new SimpleTextureMapping(texID,
                                                       getTextureCoordinates(trc),
                                                       false);
      TexturedMesh* texMesh = new TexturedMesh(tessellatorMesh, false, tMap, true);
      delete previousMesh;   //If a new mesh has been produced we delete the previous one
      return texMesh;
    }
  }
  
  return NULL;
  
}

Mesh* TileImagesTileTexturizer::getFallBackTexturedMesh(Tile* tile,
                                                        const TileRenderContext* trc,
                                                        Mesh* tessellatorMesh,
                                                        Mesh* previousMesh) {
  const SimpleTextureMapping* fbTMap = NULL;
  GLTextureID texID = GLTextureID::invalid();
  Tile* ancestor = tile->getParent();
  while (ancestor != NULL && !texID.isValid()) {
    
    if (ancestor->isTextureSolved()) {
      TexturedMesh* texMesh = (TexturedMesh*) ancestor->getTexturizerMesh();
      if (texMesh != NULL){
        fbTMap = (SimpleTextureMapping*) texMesh->getTextureMapping();
        texID = fbTMap->getGLTextureID();
        if (texID.isValid()) {
          break;
        }
      }
    }
    ancestor = ancestor->getParent();       //TRYING TO CREATE FALLBACK TEXTURE FROM ANTECESOR
  }
  
  //CREATING MESH
  if (texID.isValid()) {
    _texturesHandler->retainGLTextureId(texID);

    SimpleTextureMapping* tMap = new SimpleTextureMapping(texID,
                                                          getTextureCoordinates(trc),
                                                          false);
    translateAndScaleFallBackTex(tile, ancestor, tMap);
    TexturedMesh* texMesh = new TexturedMesh(tessellatorMesh, false, tMap, true);
    delete previousMesh;   //If a new mesh has been produced we delete the previous one
    return texMesh;
  }
  
  return NULL;
}

void TileImagesTileTexturizer::registerNewRequest(const RenderContext* rc,
                                                  Tile *tile){
  if (getRegisteredTilePetitions(tile) == NULL) { //NOT YET REQUESTED
    int priority = tile->getLevel(); //DOWNLOAD PRIORITY SET TO TILE LEVEL
    TilePetitions *tp = createTilePetitions(rc, tile);
    _tilePetitions.push_back(tp); //STORED
    tp->requestToNet(_downloader, priority);
  }
}

Mesh* TileImagesTileTexturizer::texturize(const RenderContext* rc,
                                          const TileRenderContext* trc,
                                          Tile* tile,
                                          Mesh* tessellatorMesh,
                                          Mesh* previousMesh) {
  //STORING CONTEXT
  _factory    = rc->getFactory();
  _texturesHandler = rc->getTexturesHandler();
  _downloader = rc->getDownloader();
  
  //printf("TP SIZE: %lu\n", _tilePetitions.size());
  
  Mesh* mesh = getNewTextureMesh(tile, trc, tessellatorMesh, previousMesh);
  if (mesh == NULL){
    //REGISTERING PETITION AND SENDING TO THE NET IF NEEDED
    registerNewRequest(rc, tile);
    mesh = getNewTextureMesh(tile, trc, tessellatorMesh, previousMesh);
    
    //If we still can't get a new TexturedMesh we try to get a FallBack Mesh
    if (mesh == NULL){
      mesh = getFallBackTexturedMesh(tile, trc, tessellatorMesh, previousMesh);
    }
  }
  
  return mesh;
}

void TileImagesTileTexturizer::tileToBeDeleted(Tile* tile,
                                               Mesh* mesh) {
  
  if ((_texturesHandler != NULL) && (mesh != NULL)) {
    TexturedMesh* texturedMesh = (TexturedMesh*) mesh;
    
    SimpleTextureMapping* simpleTextureMapping = (SimpleTextureMapping*) texturedMesh->getTextureMapping();
    _texturesHandler->releaseGLTextureId( simpleTextureMapping->getGLTextureID() );
  }

  
  TilePetitions* tp = getRegisteredTilePetitions(tile);
  
  if (tp != NULL){
    tp->cancelPetitions(_downloader);
    removeRegisteredTilePetitions(tile);
  }
}

bool TileImagesTileTexturizer::tileMeetsRenderCriteria(Tile* tile) {
  // TODO: compare tile->level with maxLevel in WMS-layer definition
  return false;
}

void TileImagesTileTexturizer::justCreatedTopTile(const RenderContext* rc,
                                                  Tile *tile) {
  int priority = 9999; //MAX PRIORITY
  TilePetitions *tp = createTilePetitions(rc, tile);
  _tilePetitionsTopTile.push_back(tp); //STORED
  _tilePetitions.push_back(tp);
  tp->requestToNet(_downloader, priority);
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

TilePetitions* TileImagesTileTexturizer::getRegisteredTilePetitions(Tile* tile) const {
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

void TileImagesTileTexturizer::removeRegisteredTilePetitions(Tile* tile) {
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

void TileImagesTileTexturizer::ancestorTexturedSolvedChanged(Tile* tile,
                                                             Tile* ancestorTile,
                                                             bool textureSolved) {
  
}