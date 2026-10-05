//
//  ResizerImageFactory.hpp
//  G3M
//
//  Created by DIEGO RAMIRO GOMEZ-DECK on 2/11/19.
//

#ifndef ResizerImageFactory_hpp
#define ResizerImageFactory_hpp


#include "AbstractImageFactory.hpp"

#include <string>

class ImageSizer;
class IImage;


class ResizerImageFactory : public AbstractImageFactory {
private:
  IImageFactory* _imageFactory;
  ImageSizer*    _widthSizer;
  ImageSizer*    _heightSizer;

protected:
  ~ResizerImageFactory();

public:
  ResizerImageFactory(IImageFactory* imageFactory,
                      ImageSizer*    widthSizer,
                      ImageSizer*    heightSizer);

  bool isMutable() const {
    return false;
  }

  void create(const G3MContext* context,
             IImageFactoryListener* listener,
             bool deleteListener);

  void onError(const std::string& error,
               IImageFactoryListener* listener,
               bool deleteListener);

  void imageCreated(const IImage*      image,
                    const std::string& imageName,
                    const G3MContext* context,
                    IImageFactoryListener* listener,
                    bool deleteListener);

};

#endif
