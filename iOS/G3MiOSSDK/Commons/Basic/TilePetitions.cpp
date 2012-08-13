//
//  TilePetitions.cpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 17/07/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#include "TilePetitions.hpp"

#include "TileImagesTileTexturizer.hpp"
#include "Downloader.hpp"
#include "TexturesHandler.hpp"

void TilePetitions::onDownload(const Response &response)
{
  _downloadsCounter++;
  
  std::string url = response.getURL().getPath();
  
  for (int j = 0; j < _petitions.size(); j++) {
    if (_petitions[j]->getURL() == url)
    {
      //STORING PIXEL DATA FOR RECEIVED URL
      ByteBuffer *bb = new ByteBuffer(*response.getByteBuffer());
      _petitions[j]->setByteBuffer(bb);
    }
  }
}


void TilePetitions::onError(const Response& e) {
  _errorsCounter++;
}

bool TilePetitions::allFinished() const {
  for (int i = 0; i < _petitions.size(); i++) {
    if (!_petitions[i]->isArrived()){
      return false;
    }
  }
  return true;
}

void TilePetitions::onCancel(const URL& url){
  _errorsCounter++;
}

std::string TilePetitions::createPetitionsID(const IFactory* fac) const
{
  std::string id;
  for (int j = 0; j < _petitions.size(); j++) {
    if (j > 0){
      id += "__";
    }
    id += _petitions[j]->getURL();
  }
  
  id += fac->stringFormat("_%f_%f_%f", _level, _row, _column);
  
  return id;
}

void TilePetitions::requestToNet(Downloader& downloader, int priority)
{
  for (int i = 0; i < getNumPetitions(); i++) {
    Petition* pet = getPetition(i);
    if (!pet->isArrived()) {
      const URL& url = URL(pet->getURL());
      long id = downloader.request(url, priority, this);
      pet->setDownloadID(id);
    }
  }
}

void TilePetitions::requestToCache(Downloader& downloader)
{
  for (int i = 0; i < getNumPetitions(); i++) {
    Petition* pet = getPetition(i);
    if (!pet->isArrived()) {
      const std::string& url = pet->getURL();
      ByteBuffer* bb = downloader.getByteBufferFromCache(url);
      if (bb != NULL){
        pet->setByteBuffer(bb);
      }
    }
  }
}

void TilePetitions::cancelPetitions(Downloader& downloader)
{
  for (int i = 0; i < _petitions.size(); i++) {
    long id = _petitions[i]->getDownloadID();
    if (id > -1) downloader.cancelRequest(id);
  }
}

Rectangle* TilePetitions::getImageRectangleInTexture(const Sector& wholeSector, 
                                                               const Sector& imageSector,
                                                               int texWidth, int texHeight) const
{
  Vector2D pos = wholeSector.getUVCoordinates(imageSector.lower().latitude(), imageSector.lower().longitude());
  
  double width = imageSector.getDeltaLongitude().degrees() / wholeSector.getDeltaLongitude().degrees();
  double height = imageSector.getDeltaLatitude().degrees() / wholeSector.getDeltaLatitude().degrees();
  
  Rectangle* r = new Rectangle(pos.x() * texWidth, 
                               (1.0 - pos.y()) * texHeight, 
                               width * texWidth, 
                               height * texHeight);
  return r;
}

void TilePetitions::createTexture(TexturesHandler* texHandler, const IFactory* factory, int width, int height)
{
  if (allFinished())
  {
    //Creating images (opaque one must be the first)
    std::vector<const IImage*> images;
    std::vector<const Rectangle*> rectangles;
    for (int i = 0; i < getNumPetitions(); i++) {
      const ByteBuffer* bb = getPetition(i)->getByteBuffer();
      IImage *im = factory->createImageFromData(*bb);
      
      Sector imSector = getPetition(i)->getSector();
      if (im != NULL) {
        images.push_back(im);
        Rectangle* rec = getImageRectangleInTexture(_tileSector, imSector, width, height);
        rectangles.push_back(rec);
      }
    }
    
    //Creating the texture
    const std::string& url = getPetitionsID();  
    //_texID = texHandler->getTextureId(images, url, width, height);
    _texID = texHandler->getTextureId(images, rectangles, url, width, height);
    
    //RELEASING MEMORY
    for (int i = 0; i < _petitions.size(); i++) {
      _petitions[i]->releaseData();
    }
    for (int i = 0; i < images.size(); i++) {
      factory->deleteImage(images[i]);
    }
    for (int i = 0; i < rectangles.size(); i++) {
      delete rectangles[i];
    }
  }
}

void TilePetitions::removeUnnecesaryPetitions(){
  //For each opaque Bbox, we delete every covered request beneath
  std::vector<Petition*> visiblePetitions;
  
  for(int i = 0; i < _petitions.size(); i++){
    bool isVisible = true;
    for (int j = i+1; j < _petitions.size(); j++) {
      if (!_petitions[j]->isTransparent() && 
          _petitions[j]->getSector().fullContains(_petitions[i]->getSector())){
        isVisible = false;
        break;
      }
    }
    
    if (isVisible){
      visiblePetitions.push_back(_petitions[i]);
    }
  }
  
  _petitions = visiblePetitions;
}

