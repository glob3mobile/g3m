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

#include <string>

class IImageListener {
public:
  virtual ~IImageListener() {
  }

  /**
   Callback method for image-creation.  The image has to be deleted in C++ / and disposed() en Java
   */
  virtual void imageCreated(const IImage* image) = 0;


};

#endif
