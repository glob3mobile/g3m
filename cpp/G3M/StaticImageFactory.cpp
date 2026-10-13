//
//  StaticImageFactory.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 2/13/15.
//
//

#include "StaticImageFactory.hpp"

#include "IImage.hpp"
#include "IImageFactoryListener.hpp"

StaticImageFactory::~StaticImageFactory() {
  delete _image;

#ifdef JAVA_CODE
  super.dispose();
#endif
}

void StaticImageFactory::create(const G3MContext* context,
                               IImageFactoryListener* listener,
                               bool deleteListener) {
  listener->imageCreated(_image->shallowCopy(),
                         _imageName);

  if (deleteListener) {
    delete listener;
  }
}
