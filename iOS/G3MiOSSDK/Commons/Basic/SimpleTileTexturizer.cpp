//
//  SimpleTileTexturizer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 12/07/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

#include "SimpleTileTexturizer.hpp"

#include "Context.hpp"
#include "TextureMapping.hpp"
#include "TexturedMesh.hpp"

#include "TilePetitions.hpp"


TilePetitions* SimpleTileTexturizer::getTilePetitions(const Tile* tile)
{
  
  std::string url = "http://www.arkive.org/images/browse/world-map.jpg"; //FIXED

  //SAVING PETITION
  TilePetitions *tt = new TilePetitions(tile->getLevel(), tile->getRow(), tile->getColumn(), this);
  tt->add(url, tile->getSector());
  
  return tt;
}

std::vector<MutableVector2D> SimpleTileTexturizer::createTextureCoordinates() const
{
  std::vector<MutableVector2D> texCoor;

  const double lonRes1 = (double) (_resolution-1), latRes1 = (double) (_resolution-1);
  for(double i = 0.0; i < _resolution; i++){
    double u = (i / lonRes1);
    for (double j = 0.0; j < _resolution; j++) {
      const double v = (j / latRes1);
      MutableVector2D v2d(v,u);
      texCoor.push_back(v2d);
    }
  }
  
  return texCoor;
}

Mesh* SimpleTileTexturizer::getMesh(Tile* tile, Mesh* mesh)
{
  
  //THE TEXTURE HAS BEEN LOADED
  for (int i = 0; i < _finishedTiles.size(); i++) {
    FinishedTile& ft = _finishedTiles[i];
    if (tile->getLevel() == ft._level &&
        tile->getRow() == ft._row &&
        tile->getColumn() == ft._column){
      
      
      //Texture Solved
      tile->setTextureSolved(true);
      
      TextureMapping * tMap = new TextureMapping(ft._texID, createTextureCoordinates());
      return new TexturedMesh(mesh, false, tMap);
    }
  }
  
  return NULL;
}

Mesh* SimpleTileTexturizer::texturize(const RenderContext* rc,
                                      Tile* tile,
                                      Mesh* mesh,
                                      Mesh* previousMesh) {
  
  _rc = rc; //STORING CONTEXT
  
  Mesh* texMesh = getMesh(tile, mesh);
  if (texMesh != NULL) return texMesh;
  
  //THROWING AND CREATING THE PETITIONS
  int priority = 10;
  TilePetitions *tp = getTilePetitions(tile);
  Downloader* d = rc->getDownloader();
  for (int i = 0; i < tp->getNumPetitions(); i++) {
    const std::string& url = tp->getPetition(i).getURL();
    d->request(url, priority, tp);
  }
  
  return NULL;
}

void SimpleTileTexturizer::onTilePetitionsFinished(TilePetitions * tp)
{
  //TAKING JUST FIRST!!!
  const ByteBuffer* bb = tp->getPetition(0).getByteBuffer();
  IImage *im = _rc->getFactory()->createImageFromData(*bb);
  
  const std::string& url = tp->getPetition(0).getURL();   
  int texID = _rc->getTexturesHandler()->getTextureId(_rc, im, url, 256, 256);
  
  //RELEASING MEMORY
  _rc->getFactory()->deleteImage(im);

  //Tile finished
  FinishedTile ft;
  ft._column = tp->getColumn();
  ft._level = tp->getLevel();
  ft._row = tp->getRow();
  ft._texID = texID;
  _finishedTiles.push_back(ft);
  
  printf("%d, %d, %d\n", ft._level, ft._row, ft._level);

}
