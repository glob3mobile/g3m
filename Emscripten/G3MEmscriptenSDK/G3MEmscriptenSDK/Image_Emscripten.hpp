
#ifndef Image_Emscripten_hpp
#define Image_Emscripten_hpp

#include "IImage.hpp"

#include <emscripten/val.h>

class IImageListener;


class Image_Emscripten : public IImage {
private:
  const emscripten::val _domImage;

public:

  static void createFromURL(const std::string& url,
                            IImageListener* listener,
                            bool autodelete);

  Image_Emscripten(const emscripten::val& domImage);

  const emscripten::val getDOMImage() const;

};

#endif
