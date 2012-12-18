//
//  IImageListener.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 12/5/12.
//
//

#ifndef __G3MiOSSDK__IImageListener__
#define __G3MiOSSDK__IImageListener__

class IImage;

class IImageListener {
public:
  virtual void imageCreated(IImage* image) = 0;

#ifdef C_CODE
  virtual ~IImageListener() {

  }
#endif
};

#endif
