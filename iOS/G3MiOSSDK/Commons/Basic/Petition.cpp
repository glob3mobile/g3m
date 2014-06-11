//
//  Petition.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 20/08/12.
//
//

#include "Petition.hpp"

#include "IStringBuilder.hpp"
#include "IImage.hpp"
#include "TimeInterval.hpp"

Petition::Petition(const Sector& sector,
                   const URL& url,
                   const TimeInterval& timeToCache,
                   bool readExpired,
                   bool isTransparent,
                   float layerTransparency):
_sector(sector),
_url(url),
_timeToCacheInMS(timeToCache._milliseconds),
_readExpired(readExpired),
_isTransparent(isTransparent),
_image(NULL),
_layerTransparency(layerTransparency)
{
}

const TimeInterval Petition::getTimeToCache() const {
  return TimeInterval::fromMilliseconds(_timeToCacheInMS);
}


const std::string Petition::description() const {
  IStringBuilder* isb = IStringBuilder::newStringBuilder();
  isb->addString("Petition(url=");
  isb->addString(_url.description());
  isb->addString(", sector=");
  isb->addString(_sector.description());
  isb->addString(", image=");
  if (_image == NULL) {
    isb->addString("NULL");
  }
  else {
    isb->addString(_image->description());
  }

  const std::string s = isb->getString();
  delete isb;
  return s;
}

void Petition::releaseImage() {
  delete _image;
  _image = NULL;
}
