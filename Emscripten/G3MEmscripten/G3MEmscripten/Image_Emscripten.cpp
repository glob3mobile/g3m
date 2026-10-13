

#include "Image_Emscripten.hpp"

#include <emscripten.h>
#include <emscripten/html5.h>

#include "G3M/IImageListener.hpp"
#include "G3M/IStringBuilder.hpp"
#include "G3M/MutableColor255.hpp"


using namespace emscripten;


Image_Emscripten::Image_Emscripten(const val& domImage) :
_domImage(domImage),
_imageData( val::null() )
{
}

const val Image_Emscripten::getDOMImage() const {
  return _domImage;
}

int Image_Emscripten::getWidth() const {
  return _domImage["width"].as<int>();
}

int Image_Emscripten::getHeight() const {
  return _domImage["height"].as<int>();
}

const Vector2I Image_Emscripten::getExtent() const {
  return Vector2I(getWidth(), getHeight());
}

const std::string Image_Emscripten::description() const {
  IStringBuilder* isb = IStringBuilder::newStringBuilder();
  isb->addString("Image_Emscripten ");
  isb->addInt(getWidth());
  isb->addString("x");
  isb->addInt(getHeight());
  isb->addString(", _domImage=(");
  isb->addString( _domImage.as<bool>() ? _domImage.call<std::string>("toString") : "NULL" );
  isb->addString(")");
  const std::string s = isb->getString();
  delete isb;
  return s;
}

bool Image_Emscripten::isPremultiplied() const {
  return true;
}

IImage* Image_Emscripten::shallowCopy() const {
  return new Image_Emscripten(_domImage);
}

val Image_Emscripten::createImageData() const {
  val document = val::global("document");

  val canvas        = document.call<val>("createElement", val("canvas"));
  val canvasContext = canvas.call<val>("getContext", val("2d"));

  val w = val(getWidth());
  val h = val(getHeight());
  canvas.set("width",  w);
  canvas.set("height", h);

  canvasContext.call<void>("drawImage", _domImage, val(0), val(0), w, h);

  return canvasContext.call<val>("getImageData", val(0), val(0), w, h);
}

void Image_Emscripten::getPixel(int x, int y,
                                MutableColor255& pixel) const {
  if (!_imageData.as<bool>()) {
    _imageData = createImageData();
  }
  const int i = ((y * getWidth()) + x) * 4;
  val data = _imageData["data"];
  pixel._red   = data[i    ].as<int>();
  pixel._green = data[i + 1].as<int>();
  pixel._blue  = data[i + 2].as<int>();
  pixel._alpha = data[i + 3].as<int>();
}


void Image_Emscripten::createFromCanvas(const val& domCanvas,
                                        IImageListener* listener,
                                        bool autodelete) {
  listener->imageCreated( new Image_Emscripten(domCanvas) );
  if (autodelete) {
    delete listener;
  }
}
