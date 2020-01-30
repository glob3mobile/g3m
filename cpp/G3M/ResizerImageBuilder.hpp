//
//  ResizerImageBuilder.hpp
//  G3M
//
//  Created by DIEGO RAMIRO GOMEZ-DECK on 2/11/19.
//

#ifndef ResizerImageBuilder_hpp
#define ResizerImageBuilder_hpp


#include "AbstractImageBuilder.hpp"

#include <string>

class ImageSizer;
class IImage;


class ResizerImageBuilder : public AbstractImageBuilder {
private:
  IImageBuilder* _imageBuilder;
  ImageSizer*    _widthSizer;
  ImageSizer*    _heightSizer;

protected:
  ~ResizerImageBuilder();

public:
  ResizerImageBuilder(IImageBuilder* imageBuilder,
                      ImageSizer*    widthSizer,
                      ImageSizer*    heightSizer);

  bool isMutable() const {
    return false;
  }

  void build(const G3MContext* context,
             IImageBuilderListener* listener,
             bool deleteListener);

  void onError(const std::string& error,
               IImageBuilderListener* listener,
               bool deleteListener);

  void imageCreated(const IImage*      image,
                    const std::string& imageName,
                    const G3MContext* context,
                    IImageBuilderListener* listener,
                    bool deleteListener);

};

#endif
