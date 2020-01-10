

#include "Image_Emscripten.hpp"


Image_Emscripten::Image_Emscripten(const emscripten::val& domImage) :
_domImage(domImage)
{

}

const emscripten::val Image_Emscripten::getDOMImage() const {
  return _domImage;
}
