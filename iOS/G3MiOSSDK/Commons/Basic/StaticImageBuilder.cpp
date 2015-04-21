//
//  StaticImageBuilder.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/13/15.
//
//

#include "StaticImageBuilder.hpp"

#include "IImage.hpp"
#include "IImageBuilderListener.hpp"

StaticImageBuilder::~StaticImageBuilder() {
  delete _image;

#ifdef JAVA_CODE
  super.dispose();
#endif
}

void StaticImageBuilder::build(const G3MContext* context,
                               IImageBuilderListener* listener,
                               bool deleteListener) {
  listener->imageCreated(_image->shallowCopy(),
                         _imageName);

  if (deleteListener) {
    delete listener;
  }
}
