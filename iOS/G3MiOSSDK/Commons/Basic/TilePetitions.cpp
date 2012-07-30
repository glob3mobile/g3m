//
//  TilePetitions.cpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 17/07/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#include "TilePetitions.hpp"

#include "TileImagesTileTexturizer.hpp"

void TilePetitions::onDownload(const Response &response)
{
  _downloadsCounter++;
  
  std::string url = response.getURL().getPath();
  
  for (int j = 0; j < _petitions.size(); j++) {
    if (_petitions[j]->getURL() == url)
    {
      //STORING PIXEL DATA FOR RECEIVED URL
#ifdef C_CODE
      ByteBuffer *bb = new ByteBuffer(*response.getByteBuffer());
#endif
#ifdef JAVA_CODE
        ByteBuffer bb = new ByteBuffer(response.getByteBuffer());
#endif
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

std::string TilePetitions::getPetitionsID() const
{
  std::string id;
  for (int j = 0; j < _petitions.size(); j++) {
    if (j > 0){
      id += "__";
    }
    id += _petitions[j]->getURL();
  }
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
  
  double width = wholeSector.getDeltaLongitude().degrees() / imageSector.getDeltaLongitude().degrees();
  double height = wholeSector.getDeltaLatitude().degrees() / imageSector.getDeltaLatitude().degrees();
  
  
  
  Rectangle* r = new Rectangle(pos.x() * texWidth, pos.y() * texHeight, width * texWidth, height * texHeight);
  return r;
}

void TilePetitions::createTexture(TexturesHandler* texHandler, const IFactory* factory, int width, int height)
{
  if (allFinished())
  {
    //Creating images (opaque one must be the first)
    std::vector<const IImage*> images;
    std::vector<Rectangle*> rectangles;
    for (int i = 0; i < getNumPetitions(); i++) {
      const ByteBuffer* bb = getPetition(i)->getByteBuffer();
      IImage *im = factory->createImageFromData(*bb);
      if (im != NULL) {
        images.push_back(im);
        Rectangle* rec = getImageRectangleInTexture(_tileSector, getPetition(i)->getSector(), width, height);
        rectangles.push_back(rec);
      }
    }
    
    //Creating the texture
    const std::string& url = getPetitionsID();  
    _texID = texHandler->getTextureId(images, url, width, height);
    
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

