//
//  StaticImageFactory.h
//  G3M
//
//  Created by Diego Gomez Deck on 2/13/15.
//
//

#ifndef __G3M__StaticImageFactory__
#define __G3M__StaticImageFactory__

#include "AbstractImageFactory.hpp"
#include <string>
class IImage;

class StaticImageFactory : public AbstractImageFactory {
private:
  const IImage*     _image;
  const std::string _imageName;

protected:
  ~StaticImageFactory();

public:
  StaticImageFactory(const IImage* image,
                     const std::string& imageName) :
  _image(image),
  _imageName(imageName)
  {

  }


  bool isMutable() const {
    return false;
  }

  void create(const G3MContext* context,
             IImageFactoryListener* listener,
             bool deleteListener);
  
};

#endif
