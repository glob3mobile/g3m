
#ifndef Image_Emscripten_hpp
#define Image_Emscripten_hpp

#include "G3M/IImage.hpp"

#include <emscripten/val.h>

class IImageListener;


class Image_Emscripten : public IImage {
private:
  const emscripten::val _domImage;

  mutable emscripten::val _imageData;

  emscripten::val createImageData() const;


public:

  static void createFromURL(const std::string& url,
                            IImageListener* listener,
                            bool autodelete);

  Image_Emscripten(const emscripten::val& domImage);

  const emscripten::val getDOMImage() const;

  int getWidth() const;
  int getHeight() const;
  const Vector2I getExtent() const;

  const std::string description() const;

  bool isPremultiplied() const;

  IImage* shallowCopy() const;

  void getPixel(int x, int y,
                MutableColor255& pixel) const;


};

#endif
