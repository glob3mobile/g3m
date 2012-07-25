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


TilePetitions* TileImagesTileTexturizer::getTilePetitions(const Tile* tile) {  
  std::vector<Petition*> pet = _layerSet->createTilePetitions(*tile, 
                                                     _parameters->_tileTextureWidth, 
                                                     _parameters->_tileTextureHeight);
  
  TilePetitions* tp = new TilePetitions(tile->getLevel(), tile->getRow(), tile->getColumn(), tile->getSector(), 
                                        pet, this);
  
  
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

Mesh* TileImagesTileTexturizer::getMesh(Tile* tile,
                                        const TileTessellator* tessellator,
                                        Mesh* tessellatorMesh,
                                        Mesh* previousMesh) {

  if (isTextureAvailable(tile)) {
    return getNewTextureMesh(tile, tessellator, tessellatorMesh);
  }
  else {
    return getFallBackTexturedMesh(tile, tessellator, tessellatorMesh);
  }
}

Mesh* TileImagesTileTexturizer::getNewTextureMesh(Tile* tile,
                                                  const TileTessellator* tessellator,
                                                  Mesh* tessellatorMesh) {
    //THE TEXTURE HAS BEEN LOADED???
    RequestedTile* ft = getRequestTile(tile->getLevel(), tile->getRow(), tile->getColumn());
    if (ft != NULL && ft->_texID > -1) { // Texture already solved
        tile->setTextureSolved(true);
        TextureMapping * tMap = new TextureMapping(ft->_texID, getTextureCoordinates(tessellator));
        return new TexturedMesh(tessellatorMesh, false, tMap, true);
    }
    return NULL;
}

Mesh* TileImagesTileTexturizer::getFallBackTexturedMesh(Tile* tile,
                                                        const TileTessellator* tessellator,
                                                        Mesh* tessellatorMesh) {
  int texID = -1;
  Tile* fbTile = tile;
  while (fbTile != NULL && texID < 0) {
    fbTile = fbTile->getParent();       //TRYING TO CREATE FALLBACK TEXTURE FROM ANTECESOR
    
    if (fbTile != NULL) {
      RequestedTile* ft = getRequestTile(fbTile->getLevel(), fbTile->getRow(), fbTile->getColumn());
      if (ft == NULL) {
        registerNewRequest(fbTile);
        ft = getRequestTile(fbTile->getLevel(), fbTile->getRow(), fbTile->getColumn());
      }
      if (ft != NULL) {
        texID = ft->_texID;
      }
    }
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
  //Tile finished
  RequestedTile ft;
  ft._column = tile->getColumn();
  ft._level = tile->getLevel();
  ft._row = tile->getRow();
  ft._texID = -1;
  
  //THROWING THE PETITIONS
  int priority = tile->getLevel(); //DOWNLOAD PRIORITY SET TO TILE LEVEL
  TilePetitions *tp = getTilePetitions(tile);
  
  _requestedTiles.push_back(ft); //STORED
  
  RequestedTile& ft2 = _requestedTiles[ _requestedTiles.size() -1 ]; //GETTING VECTOR INSTANCE
  for (int i = 0; i < tp->getNumPetitions(); i++) {
    Petition* pet = tp->getPetition(i);
    
    const std::string& url = pet->getURL();
    long id = _downloader->request(url, priority, tp);
    ft2._downloads.push_back(id);
  }
}

Mesh* TileImagesTileTexturizer::texturize(const RenderContext* rc,
                                          Tile* tile,
                                          const TileTessellator* tessellator,
                                          Mesh* tessellatorMesh,
                                          Mesh* previousMesh,
                                          ITimer* timer) {
  
  const bool dummy = false;
  if (dummy){
    //CHESSBOARD TEXTURE
    int texID = rc->getTexturesHandler()->getTextureIdFromFileName("NoImage.jpg", _parameters->_tileTextureWidth, _parameters->_tileTextureHeight);
    if (previousMesh != NULL) delete previousMesh;
    TextureMapping * tMap = new TextureMapping(texID, getTextureCoordinates(tessellator));
    return new TexturedMesh(tessellatorMesh, false, tMap, true);
  }
  
  //STORING CONTEXT
  _factory    = rc->getFactory();
  _texHandler = rc->getTexturesHandler();
  _downloader = rc->getDownloader();
  
  if (timer != NULL) {
    int __TODO_tune_TEXTURIZER_render_budget;
    if ( timer->elapsedTime().milliseconds() > 20 ) {
      return getFallBackTexturedMesh(tile, tessellator, tessellatorMesh);
    }
  }

  
  
  if (!isTextureAvailable(tile)) {
    if (!isTextureRequested(tile)) {
      //REGISTERING PETITION AND SENDING TO THE NET
      registerNewRequest(tile);
    }
  }
  
  Mesh *mesh = getMesh(tile, tessellator, tessellatorMesh, previousMesh);
  if (mesh != NULL && previousMesh != NULL) {
    delete previousMesh;
  }
  
  return mesh;
}

void TileImagesTileTexturizer::onTilePetitionsFinished(TilePetitions * tp)
{
  //Tile finished
  RequestedTile *rt = getRequestTile(tp->getLevel(), tp->getRow(), tp->getColumn());
  if (rt != NULL) { //If null means the tile is no longer visible
    
    //Creating images (opaque one must be the first)
    std::vector<const IImage*> images;
    for (int i = 0; i < tp->getNumPetitions(); i++) {
      const ByteBuffer* bb = tp->getPetition(i)->getByteBuffer();
      IImage *im = _factory->createImageFromData(*bb);
      if (im != NULL) {
        images.push_back(im);
      }
    }

    //Creating the texture
    const std::string& url = tp->getPetitionsID();  
    int texID = _texHandler->getTextureId(images,
                                          url, 
                                          _parameters->_tileTextureWidth,
                                          _parameters->_tileTextureHeight);
    
    //RELEASING MEMORY
    for (int i = 0; i < images.size(); i++) {
      _factory->deleteImage(images[i]);
    }

    //Texture
    rt->_texID = texID;
  }
}

void TileImagesTileTexturizer::tileToBeDeleted(Tile* tile) {
  int index = -1;
  for (int i = 0; i < _requestedTiles.size(); i++) {
    RequestedTile& ft = _requestedTiles[i];
    if (tile->getLevel() == ft._level &&
        tile->getRow() == ft._row &&
        tile->getColumn() == ft._column){
      index = i;
      break;
    }
  }
  
  if (index > -1){
    RequestedTile& rt = _requestedTiles[index];
    //DELETING TEXTURE
    if (rt._texID > -1) {
      _texHandler->takeTexture(rt._texID);
    }
    
    //CANCELING PETITIONS
    for (int i = 0; i < rt._downloads.size(); i++) {
      _downloader->cancelRequest( rt._downloads[i] );
    }
    
    _requestedTiles.erase( _requestedTiles.begin() + index );
  }
}

bool TileImagesTileTexturizer::tileMeetsRenderCriteria(Tile* tile) {
  // TODO: compare tile->level with maxLevel in WMS-layer definition
  return false;
}

void TileImagesTileTexturizer::justCreatedTopTile(Tile *tile) {
  
  TilePetitions *tp = getTilePetitions(tile);
  
  for (int i = 0; i < tp->getNumPetitions(); i++) {
    const std::string& url = tp->getPetition(i)->getURL();
    _downloader->request(url, 99999, NULL);
  }
  
}

bool TileImagesTileTexturizer::isReadyToRender(const RenderContext *rc) {
  int __TODO_check_for_level_0_loaded;

  return true;
}

Rectangle TileImagesTileTexturizer::getImageRectangleInTexture(const Sector& wholeSector, 
                                     const Sector& imageSector) const
{
  int _job_for_JM;
  Rectangle r(0,0,0,0);
  return r;
}
