//
//  IImageFactoryListener.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 1/2/14.
//
//

#ifndef __G3M__IImageFactoryListener__
#define __G3M__IImageFactoryListener__

class IImage;

#include <string>

class IImageFactoryListener {
public:
#ifdef C_CODE
  virtual ~IImageFactoryListener() {
  }
#endif
#ifdef JAVA_CODE
  void dispose();
#endif

  virtual void imageCreated(const IImage*      image,
                            const std::string& imageName) = 0;

  virtual void onError(const std::string& error) = 0;

};

#endif
