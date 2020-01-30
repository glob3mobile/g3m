//
//  StaticImageBuilder.h
//  G3M
//
//  Created by Diego Gomez Deck on 2/13/15.
//
//

#ifndef __G3M__StaticImageBuilder__
#define __G3M__StaticImageBuilder__

#include "AbstractImageBuilder.hpp"
#include <string>
class IImage;

class StaticImageBuilder : public AbstractImageBuilder {
private:
  const IImage*     _image;
  const std::string _imageName;

protected:
  ~StaticImageBuilder();

public:
  StaticImageBuilder(const IImage* image,
                     const std::string& imageName) :
  _image(image),
  _imageName(imageName)
  {

  }


  bool isMutable() const {
    return false;
  }

  void build(const G3MContext* context,
             IImageBuilderListener* listener,
             bool deleteListener);
  
};

#endif
