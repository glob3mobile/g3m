//
//  IImageBuilderListener.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/2/14.
//
//

#ifndef __G3MiOSSDK__IImageBuilderListener__
#define __G3MiOSSDK__IImageBuilderListener__

class IImage;

#include "IImageListener.hpp"
#include <string>

class IImageBuilderListener : public IImageListener {
public:
  virtual ~IImageBuilderListener() {
  }

  virtual void imageCreated(const IImage* image) = 0;

  virtual void onError(const std::string& error) = 0;

};

#endif
